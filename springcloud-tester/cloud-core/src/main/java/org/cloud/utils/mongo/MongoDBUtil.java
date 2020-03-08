package org.cloud.utils.mongo;

import com.alibaba.fastjson.util.IOUtils;
import com.github.pagehelper.PageInfo;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.cloud.constant.CoreConstant;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.MongoDbGridFsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class MongoDBUtil {
    private Logger logger = LoggerFactory.getLogger(MongoDBUtil.class);

    private MongoDBUtil() {
    }

    private static MongoDBUtil instance = new MongoDBUtil();

    private MongoTemplate mongoTemplate = SpringContextUtil.getBean(MongoTemplate.class);

    private GridFsTemplate gridFsTemplate = SpringContextUtil.getBean(GridFsTemplate.class);

    public static MongoDBUtil single() {
        return instance;
    }

    public List<ObjectId> convertIdsToObjectId(List<String> ids) {
        return ids.stream().map(ObjectId::new).collect(Collectors.toList());
    }

    public List<ObjectId> convertIdsToObjectId(String[] ids) {
        return convertIdsToObjectId(Arrays.asList(ids));
    }

    public GridFSFile getGridFSFileByObjectId(final String _id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(_id)));
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        return gridFSFile;
    }

    public InputStream getInputStreamByObjectId(final String _id) {
        GridFSFile gridFSFile = getGridFSFileByObjectId(_id);
        GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            logger.error(_id + "，获取文件流失败！");
        }
        return inputStream;
    }

    /**
     * 分布搜索文件
     */
    public PageInfo listPage(@NotNull int page, @NotNull int pageSize, Map<String, Object> params) throws Exception {
        final Query query = new Query();
        if (!StringUtils.isEmpty(params.get("filename"))) {
            query.addCriteria(Criteria.where("filename").regex("(?i)("+params.get("filename")+")"));
        }
        Criteria lengthWhere = null;
        if (!StringUtils.isEmpty(params.get("minSize"))) {
            lengthWhere = Criteria.where("length");
            lengthWhere.gte(params.get("minSize"));
        }
        if (!StringUtils.isEmpty(params.get("maxSize"))) {
            if (lengthWhere == null) {
                lengthWhere = Criteria.where("length");
            }
            lengthWhere.lte(params.get("maxSize"));
        }
        if (lengthWhere != null) {
            query.addCriteria(lengthWhere);
        }

        if (params.get("uploadDate") != null) {
            List<String> dateRange = (List<String>) params.get("uploadDate");
            query.addCriteria(Criteria.where("uploadDate")
                    .gte(CoreConstant.DateTimeFormat.ISODATE.getDateFormat().parse(dateRange.get(0)))
                    .lte(CoreConstant.DateTimeFormat.ISODATE.getDateFormat().parse(dateRange.get(1))));
        }
        if (!StringUtils.isEmpty(params.get("_id"))) {
            query.addCriteria(Criteria.where("_id").is(new ObjectId(params.get("_id").toString())));
        }
        if (!StringUtils.isEmpty(params.get(MongoDBEnum.metadataOwnerKey.value()))) {
            query.addCriteria(Criteria.where(MongoDBEnum.metadataKey.value() + "." + MongoDBEnum.metadataOwnerKey.value())
                    .is(params.get(MongoDBEnum.metadataOwnerKey.value())));
        }

        //处理metadata参数
        if ((params.get(MongoDBEnum.metadataKey.value()) != null) && (params.get(MongoDBEnum.metadataKey.value()) instanceof Map)) {
            Map metaDataQuery = (Map) params.get(MongoDBEnum.metadataKey.value());
            metaDataQuery.forEach((key, value) -> {
                if (!StringUtils.isEmpty(value)) {
                    query.addCriteria(Criteria.where(MongoDBEnum.metadataKey.value() + "." + key)
                            .regex(value.toString()));
                }
            });
        }
        List<MongoDbGridFsVO> listData = mongoTemplate.find(query.skip((page - 1) * pageSize).limit(pageSize), MongoDbGridFsVO.class, "fs.files");
        PageInfo pageInfo = new PageInfo(listData);
        pageInfo.setPageNum(page);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(mongoTemplate.getCollection("fs.files").countDocuments(query.getQueryObject()));
        return pageInfo;
    }

    public void downloadOrShowFile(GridFSFile gridFSFile, ServletResponse response, Boolean isDownLoad) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte bs[] = new byte[1024];
            GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
            inputStream = resource.getInputStream();
            outputStream = response.getOutputStream();
            String contentType = "application/octet-stream";
            if (gridFSFile.getMetadata().get(MongoDBEnum.metadataContentTypeKey.value()) != null) {
                contentType = gridFSFile.getMetadata().get(MongoDBEnum.metadataContentTypeKey.value()).toString();
            }
            response.setContentLength(new Long(gridFSFile.getLength()).intValue());
            response.setContentType(contentType);
            if (response instanceof HttpServletResponse && isDownLoad) {
                ((HttpServletResponse) response).setHeader("Content-disposition", "attachment;filename=" + resource.getFilename());
            }
            while (inputStream.read(bs) > 0) {
                outputStream.write(bs);
            }
            outputStream.flush();
        } finally {
            IOUtils.close(inputStream);
            IOUtils.close(outputStream);
        }
    }

    /**
     * 判断文件类型是否为个人文件，文件的权限范围为null或者为personal的情况下，认为是个人文件
     *
     * @param gridFSFile
     * @return
     */
    public boolean isPersonalFile(GridFSFile gridFSFile) {
        return gridFSFile.getMetadata().get(MongoDBEnum.metadataFileAuthRangeFieldName.value()) == null ||
                MongoDBEnum.metadataFileAuthRangePersonal.equals(gridFSFile.getMetadata().get(MongoDBEnum.metadataFileAuthRangeFieldName.value()))
                ;
    }

}
