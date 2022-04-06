package org.cloud.utils.mongo;

import com.alibaba.fastjson.util.IOUtils;
import com.github.pagehelper.PageInfo;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.cloud.constant.CoreConstant;
import org.cloud.mongo.*;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.MongoDbGridFsVO;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
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
    public PageInfo listFilePage(@NotNull int page, @NotNull int pageSize, Map<String, Object> params) throws Exception {
        final Query query = new Query();
        if (!StringUtils.isEmpty(params.get("filename"))) {
            query.addCriteria(Criteria.where("filename").regex("(?i)(" + params.get("filename") + ")"));
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
                    query.addCriteria(Criteria.where(MongoDBEnum.metadataKey.value() + "." + key).regex(value.toString()));
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

    public Query buildQuery(final List<MongoQueryParam> paramsVOS, Map<String, Boolean> fields) throws Exception {
        Query query = new Query();
        Criteria criteriaAll = new Criteria();
        List<Criteria> andCriteria = new ArrayList<>();
        List<Criteria> orCriteria = new ArrayList<>();
        List<Criteria> norCriteria = new ArrayList<>();
        for (MongoQueryParam param : paramsVOS) {
            Criteria criteria = buildCriteria(param);
            if (MongoEnumVO.RelationalOperator.AND.equals(param.getRelationalOperator())) {
                if (criteria != null)
                    andCriteria.add(criteria);
            } else if (MongoEnumVO.RelationalOperator.OR.equals(param.getRelationalOperator())) {
                if (criteria != null)
                    orCriteria.add(buildCriteria(param));
            } else if (MongoEnumVO.RelationalOperator.NOR.equals(param.getRelationalOperator())) {
                if (criteria != null)
                    norCriteria.add(buildCriteria(param));
            }
        }
        // 优先级 and>or>nor ,正常情况下都是and，如果查询很复杂还是要自己编写脚本，这里只是解决一部分问题
        if (!andCriteria.isEmpty()) {
            // 如果有and条件，那么将nor,or条件拼装到and条件中,暂时按这个规则处理。如果有特殊的情况，需写代码实现，这里只实现一些通用的查询
            if (!orCriteria.isEmpty()) {
                andCriteria.add(new Criteria().orOperator(orCriteria.toArray(new Criteria[]{})));
            }
            if (!norCriteria.isEmpty()) {
                andCriteria.add(new Criteria().norOperator(norCriteria.toArray(new Criteria[]{})));
            }
            criteriaAll.andOperator(andCriteria.toArray(new Criteria[]{}));
        } else if (!orCriteria.isEmpty()) {
            // 如果有or条件，先将nor条件接到or条件里
            if (!norCriteria.isEmpty()) {
                orCriteria.add(new Criteria().andOperator(norCriteria.toArray(new Criteria[]{})));
            }
            criteriaAll.orOperator(orCriteria.toArray(new Criteria[]{}));
        } else if (!norCriteria.isEmpty()) {
            criteriaAll.norOperator(norCriteria.toArray(new Criteria[]{}));
        }
        query.addCriteria(criteriaAll);

        if (!CollectionUtil.single().isEmpty(fields)) {
            org.bson.Document fieldsObj = new org.bson.Document();
            fieldsObj.putAll(fields);
            return new BasicQuery(query.getQueryObject(), fieldsObj);
        }
        return query;

    }

    private Criteria buildCriteria(MongoQueryParam param) throws Exception {
        Criteria criteria = new Criteria();
        if (CollectionUtil.single().isEmpty(param.getValue())) {
            return null;
        }
        criteria = criteria.where(param.getName());
        if (MongoEnumVO.MongoOperatorEnum.IS.equals(param.getOperator())) {
            criteria = criteria.is(param.getValue());
        } else if (MongoEnumVO.MongoOperatorEnum.NE.equals(param.getOperator())) {
            criteria = criteria.ne(param.getValue());
        } else if (MongoEnumVO.MongoOperatorEnum.GT.equals(param.getOperator())) {
            criteria = criteria.gt(param.getValue());
        } else if (MongoEnumVO.MongoOperatorEnum.LT.equals(param.getOperator())) {
            criteria = criteria.lt(param.getValue());
        } else if (MongoEnumVO.MongoOperatorEnum.GTE.equals(param.getOperator())) {
            criteria = criteria.gte(param.getValue());
        } else if (MongoEnumVO.MongoOperatorEnum.LTE.equals(param.getOperator())) {
            criteria = criteria.lte(param.getValue());
        } else if (MongoEnumVO.MongoOperatorEnum.NIN.equals(param.getOperator())) {
            criteria = criteria.nin(CollectionUtil.single().toArray(param.getValue()));
        } else if (MongoEnumVO.MongoOperatorEnum.IN.equals(param.getOperator())) {
            criteria = criteria.in(CollectionUtil.single().toArray(param.getValue()));
        } else if (MongoEnumVO.MongoOperatorEnum.ALL.equals(param.getOperator())) {
            criteria = criteria.all(CollectionUtil.single().toArray(param.getValue()));
        } else if (MongoEnumVO.MongoOperatorEnum.EXISTS.equals(param.getOperator())) {
            criteria = criteria.exists((Boolean) param.getValue());
        } else if (MongoEnumVO.MongoOperatorEnum.type.equals(param.getOperator())) {
            criteria = criteria.type(Integer.parseInt(param.getValue().toString()));
        } else if (MongoEnumVO.MongoOperatorEnum.BETWEEN.equals(param.getOperator())) {
            List values = (List) param.getValue();
            if (MongoEnumVO.DataType.Date.equals(param.getDataType())) {
                if (!StringUtils.isEmpty(values.get(0))) {
                    criteria = criteria.gte(CoreConstant.DateTimeFormat.ISODATE.getDateFormat().parse(values.get(0).toString()));
                }
                if (!StringUtils.isEmpty(values.get(1))) {
                    criteria = criteria.lte(CoreConstant.DateTimeFormat.ISODATE.getDateFormat().parse(values.get(1).toString()));
                }
            } else {
                criteria = criteria.gte(values.get(0)).lte(values.get(1));
            }
        } else if (MongoEnumVO.MongoOperatorEnum.REGEX.equals(param.getOperator())) {
            criteria = criteria.regex(param.getValue().toString());
        } else {
            logger.info("您的操作暂时不支持");
        }
        if (criteria.getCriteriaObject().size() > 0) {
            return criteria;
        }
        return null;
    }

    public <T> PageInfo<T> paged(Long pageNum, Long pageSize, MongoQueryParamsDTO queryParamsDTO, Class cls, String collectionName) throws Exception {
        Query query = MongoDBUtil.single().buildQuery(queryParamsDTO.getParams(), queryParamsDTO.getFields());
        PageInfo<T> pageInfo = new PageInfo<>();
        Long count = 0L;
        if (collectionName == null) {
            count = mongoTemplate.count(query, DataInterFaceVO.class);
        } else {
            count = mongoTemplate.count(query, collectionName);
        }
        pageInfo.setTotal(count);

        if (!queryParamsDTO.getOrders().isEmpty()) {
            query.with(Sort.by(queryParamsDTO.getOrders().stream().map(this::toOrder).collect(Collectors.toList())));
        }
        query.skip((pageNum - 1) * pageSize).limit(pageSize.intValue());
        if (collectionName == null) {
            pageInfo.setList(mongoTemplate.find(query, cls));
        } else {
            pageInfo.setList(mongoTemplate.find(query, cls, collectionName));
        }
        return pageInfo;
    }

    public <T> PageInfo<T> paged(Long pageNum, Long pageSize, MongoQueryParamsDTO queryParamsDTO, Class cls) throws Exception {
        return paged(pageNum, pageSize, queryParamsDTO, cls, null);
    }

    public <T> List<T> list(MongoQueryParamsDTO queryParamsDTO, Class cls, String collectionName) throws Exception {
        Query query = new Query();
        if (!queryParamsDTO.getParams().isEmpty()) {
            query = MongoDBUtil.single().buildQuery(queryParamsDTO.getParams(), queryParamsDTO.getFields());
        }
        if (!queryParamsDTO.getOrders().isEmpty()) {
            query.with(Sort.by(queryParamsDTO.getOrders().stream().map(this::toOrder).collect(Collectors.toList())));
        }
        List<T> dataList = null;
        if (collectionName == null) {
            dataList = mongoTemplate.find(query, cls);
        } else {
            dataList = mongoTemplate.find(query, cls, collectionName);
        }
        return dataList;
    }


    private Sort.Order toOrder(MongoQueryOrder order) {
        return new Sort.Order(Sort.Direction.valueOf(order.getDirection()), order.getProperty());
    }


    public <T> List<T> list(MongoQueryParamsDTO queryParamsDTO, Class cls) throws Exception {
        return list(queryParamsDTO, cls, null);
    }

    public void updateByObjectIds(List<String> objectIds, Update update, Class cls, String collectionName) {

    }

    public List<ObjectId> convertStrIdsToObjectIds(Collection<String> ids) {
        return ids.stream().map(ObjectId::new).collect(Collectors.toList());
    }

    public List<ObjectId> convertStrIdsToObjectIds(String[] ids) {
        return Arrays.asList(ids).stream().map(ObjectId::new).collect(Collectors.toList());
    }

    public Update buildUpdateByObject(Object value) throws InvocationTargetException, IllegalAccessException {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(value.getClass());
        Update update = new Update();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if ("class".equals(propertyDescriptor.getName())) {
                continue;
            }
            Object fieldValue = propertyDescriptor.getReadMethod().invoke(value);
            update.set(propertyDescriptor.getName(), fieldValue);
        }

        return update;
    }

}
