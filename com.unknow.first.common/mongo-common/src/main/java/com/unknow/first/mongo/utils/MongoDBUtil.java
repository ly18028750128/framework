package com.unknow.first.mongo.utils;


import static com.unknow.first.mongo.vo.MongoDBEnum.metadataContentTypeKey;
import static com.unknow.first.mongo.vo.MongoDBEnum.metadataFileAuthRangeFieldName;
import static com.unknow.first.mongo.vo.MongoDBEnum.metadataFileAuthRangePersonal;
import static com.unknow.first.mongo.vo.MongoDBEnum.metadataFilesSuffixFieldName;
import static com.unknow.first.mongo.vo.MongoDBEnum.metadataKey;
import static com.unknow.first.mongo.vo.MongoDBEnum.metadataOwnerFullNameKey;
import static com.unknow.first.mongo.vo.MongoDBEnum.metadataOwnerKey;
import static com.unknow.first.mongo.vo.MongoDBEnum.metadataOwnerNameKey;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.unknow.first.mongo.vo.MetadataDTO;
import com.unknow.first.mongo.vo.MongoDBEnum;
import com.unknow.first.mongo.vo.MongoDbGridFsVO;
import com.unknow.first.mongo.vo.MongoEnumVO;
import com.unknow.first.mongo.vo.MongoEnumVO.DataType;
import com.unknow.first.mongo.vo.MongoEnumVO.MongoOperatorEnum;
import com.unknow.first.mongo.vo.MongoEnumVO.RelationalOperator;
import com.unknow.first.mongo.vo.MongoGridFsQueryDTO;
import com.unknow.first.mongo.vo.MongoPagedParam;
import com.unknow.first.mongo.vo.MongoQuery;
import com.unknow.first.mongo.vo.MongoQueryOrder;
import com.unknow.first.mongo.vo.MongoQueryParam;
import com.unknow.first.mongo.vo.MongoQueryParamsDTO;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public final class MongoDBUtil {

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
        Assert.notNull(gridFSFile, "未查询到文件");
        return gridFSFile;
    }

    public GridFSFile getPersonalGridFSFileByObjectId(final String _id, Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(_id)));
        query.addCriteria(Criteria.where(metadataKey.value() + "." + metadataOwnerKey.value()).is(userId));
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        Assert.notNull(gridFSFile, "未查询到文件");
        return gridFSFile;
    }

    public InputStream getInputStreamByObjectId(final String _id) {
        GridFSFile gridFSFile = getGridFSFileByObjectId(_id);
        GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            log.error(_id + "，获取文件流失败！");
        }
        return inputStream;
    }

    public ObjectId storePersonFile(String fileName, Long userId, String userName) throws Exception {
        return this.storePersonFile(fileName, null, userId, userName);
    }

    public ObjectId storePersonFile(String fileName, String suffix, Long userId, String userName) throws Exception {
        MetadataDTO params = new MetadataDTO();
        params.setOwner(userId);
        params.setOwnerName(userName);
        params.setOwnerFullName(userName);
        final Path path = Paths.get(fileName);
        params.setContentType(Files.probeContentType(path));
        params.setFileAuthRange(metadataFileAuthRangePersonal.value());
        if (!StringUtils.hasLength(suffix)) {
            final int suffixIndex = Objects.requireNonNull(fileName).lastIndexOf(".");
            if (suffixIndex > -1) {
                params.setSuffix(fileName.substring(suffixIndex));
            }
        }
        InputStream in = Files.newInputStream(path);
        try {
            String storeFileName = path.getFileName().toString();
            return gridFsTemplate.store(in, storeFileName, Files.probeContentType(path), params);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public ObjectId storeFile(Object user, String fileAuthRange, MultipartFile file) throws IOException {
        return storeFile(user, MetadataDTO.builder().fileAuthRange(fileAuthRange).build(), file);
    }

    public ObjectId storeFile(MetadataDTO params, MultipartFile file) throws IOException {
        return storeFile(null, params, file);
    }

    public ObjectId storeFile(Object user, MetadataDTO params, MultipartFile file) throws IOException {
        if (user != null) {
            setUserInfo(user, params);
        } else {
            params.setOwner(MongoDBEnum.defaultFileOwnerId.getLong());
        }
        final int suffixIndex = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");
        if (suffixIndex > -1) {
            params.setSuffix(file.getOriginalFilename().substring(suffixIndex));
        }
        String contentType = file.getContentType() == null ? "unknown" : file.getContentType();
        params.setContentType(contentType);
        return gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), contentType, params);
    }

    public ObjectId storeFile(Object user, MetadataDTO params, String filePath) throws IOException {
        if (user != null) {
            setUserInfo(user, params);
        } else {
            params.setOwner(MongoDBEnum.defaultFileOwnerId.getLong());
        }
        final int suffixIndex = Objects.requireNonNull(filePath).lastIndexOf(".");
        if (suffixIndex > -1) {
            params.setSuffix(filePath.substring(suffixIndex));
        }
        Path path = Paths.get(filePath);
        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "unknown";
        }
        params.setContentType(contentType);
        return gridFsTemplate.store(Files.newInputStream(path), path.getFileName().toString(), contentType, params);
    }

    private static void setUserInfo(Object user, MetadataDTO params) {
        try {
            params.setOwner((Long) BeanUtil.getFieldValue(user, "id"));
        } catch (Exception ignored) {

        }
        try {
            params.setOwnerFullName((String) BeanUtil.getFieldValue(user, "fullName"));
        } catch (Exception ignored) {

        }

        try {
            params.setOwner((Long) BeanUtil.getFieldValue(user, "id"));
        } catch (Exception ignored) {

        }
        try {
            params.setOwnerName((String) BeanUtil.getFieldValue(user, "username"));
        } catch (Exception ignored) {

        }
    }

    /**
     * 分布搜索文件
     */
    public PageInfo<MongoDbGridFsVO> listFilePage(@NotNull int page, @NotNull int pageSize, MongoGridFsQueryDTO mongoGridFsQueryDTO) throws Exception {

        final Query query = new Query();

        if (!ObjectUtils.isEmpty(mongoGridFsQueryDTO.getFilename())) {
            query.addCriteria(Criteria.where("filename").regex("(?i)(" + mongoGridFsQueryDTO.getFilename() + ")"));
        }
        Criteria lengthWhere = null;
        if (!ObjectUtils.isEmpty(mongoGridFsQueryDTO.getMinSize())) {
            lengthWhere = Criteria.where("length");
            lengthWhere.gte(mongoGridFsQueryDTO.getMinSize());
        }
        if (!ObjectUtils.isEmpty(mongoGridFsQueryDTO.getMaxSize())) {
            if (lengthWhere == null) {
                lengthWhere = Criteria.where("length");
            }
            lengthWhere.lte(mongoGridFsQueryDTO.getMaxSize());
        }
        if (lengthWhere != null) {
            query.addCriteria(lengthWhere);
        }

        if (!ObjectUtils.isEmpty(mongoGridFsQueryDTO.getUploadDate())) {

            query.addCriteria(Criteria.where("uploadDate").gte(mongoGridFsQueryDTO.getUploadDate().get(0)).lte(mongoGridFsQueryDTO.getUploadDate().get(1)));
        }
        if (!ObjectUtils.isEmpty(mongoGridFsQueryDTO.get_id())) {
            query.addCriteria(Criteria.where("_id").is(new ObjectId(mongoGridFsQueryDTO.get_id())));
        }

        MetadataDTO metaData = mongoGridFsQueryDTO.getMetadata();

        if (!ObjectUtils.isEmpty(metaData.getOwner())) {
            query.addCriteria(Criteria.where(metadataKey.value() + "." + metadataOwnerKey.value()).is(metaData.getOwner()));
        }

        if (!ObjectUtils.isEmpty(metaData.getContentType())) {
            query.addCriteria(Criteria.where(metadataKey.value() + "." + metadataContentTypeKey.value()).regex("(?i)(" + metaData.getContentType() + ")"));
        }

        if (!ObjectUtils.isEmpty(metaData.getOwnerName())) {
            query.addCriteria(Criteria.where(metadataKey.value() + "." + metadataOwnerNameKey.value()).regex("(?i)(" + metaData.getOwnerName() + ")"));
        }

        if (!ObjectUtils.isEmpty(metaData.getOwnerFullName())) {
            query.addCriteria(Criteria.where(metadataKey.value() + "." + metadataOwnerFullNameKey.value()).regex("(?i)(" + metaData.getOwnerFullName() + ")"));
        }

        if (!ObjectUtils.isEmpty(metaData.getSuffix())) {
            query.addCriteria(Criteria.where(metadataKey.value() + "." + metadataFilesSuffixFieldName.value()).regex("(?i)(" + metaData.getSuffix() + ")"));
        }

        if (!ObjectUtils.isEmpty(metaData.getRemark())) {
            query.addCriteria(Criteria.where(metadataKey.value() + ".remark").regex("(?i)(" + metaData.getRemark() + ")"));
        }

        if (!ObjectUtils.isEmpty(metaData.getTag())) {
            query.addCriteria(Criteria.where(metadataKey.value() + ".tag").regex("(?i)(" + metaData.getTag() + ")"));
        }

        if (!ObjectUtils.isEmpty(metaData.getFileAuthRangeList())) {
            query.addCriteria(Criteria.where(metadataKey.value() + "." + metadataFileAuthRangeFieldName.value()).in(metaData.getFileAuthRangeList()));
        } else if (!ObjectUtils.isEmpty(metaData.getFileAuthRange())) {
            query.addCriteria(Criteria.where(metadataKey.value() + "." + metadataFileAuthRangeFieldName.value()).is(metaData.getFileAuthRange()));
        }

        List<MongoDbGridFsVO> listData = mongoTemplate.find(query.skip((page - 1L) * pageSize).limit(pageSize), MongoDbGridFsVO.class, "fs.files");
        PageInfo<MongoDbGridFsVO> pageInfo = new PageInfo<>(listData);
        pageInfo.setPageNum(page);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(mongoTemplate.getCollection("fs.files").countDocuments(query.getQueryObject()));
        return pageInfo;
    }

    public void downloadOrShowFile(GridFSFile gridFSFile, ServletResponse response, Boolean isDownLoad) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {

            GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
            inputStream = resource.getInputStream();
            outputStream = response.getOutputStream();
            String contentType = "application/octet-stream";
            Assert.isTrue(gridFSFile.getMetadata() != null, "Metadata为空，文件格式不正确，请检查!");
            if (gridFSFile.getMetadata().get(metadataContentTypeKey.value()) != null) {
                contentType = gridFSFile.getMetadata().get(metadataContentTypeKey.value()).toString();
            }
            response.setContentLength(Long.valueOf(gridFSFile.getLength()).intValue());
            response.setContentType(contentType);
            if (response instanceof HttpServletResponse) {
                HttpServletResponse ssResponse = ((HttpServletResponse) response);
                if (isDownLoad) {
                    ssResponse.setHeader("Content-disposition", "attachment;filename=" + resource.getFilename());
                }
                ssResponse.setHeader("Cache-Control", "public,max-age=31536000");
            }
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * 判断文件类型是否为个人文件，文件的权限范围为null或者为personal的情况下，认为是个人文件
     *
     * @param gridFSFile
     * @return
     */
    public boolean isPersonalFile(GridFSFile gridFSFile) {
        Assert.isTrue(gridFSFile.getMetadata() != null, "Metadata为空，文件格式不正确，请检查!");
        return gridFSFile.getMetadata().get(metadataFileAuthRangeFieldName.value()) == null || metadataFileAuthRangePersonal.value()
            .equals(gridFSFile.getMetadata().get(metadataFileAuthRangeFieldName.value()));
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
                if (criteria != null) {
                    andCriteria.add(criteria);
                }
            } else if (MongoEnumVO.RelationalOperator.OR.equals(param.getRelationalOperator())) {
                if (criteria != null) {
                    orCriteria.add(buildCriteria(param));
                }
            } else if (MongoEnumVO.RelationalOperator.NOR.equals(param.getRelationalOperator())) {
                if (criteria != null) {
                    norCriteria.add(buildCriteria(param));
                }
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

        if (!MyCollectionUtil.single().isEmpty(fields)) {
            org.bson.Document fieldsObj = new org.bson.Document();
            fieldsObj.putAll(fields);
            return new BasicQuery(query.getQueryObject(), fieldsObj);
        }
        return query;

    }

    private Criteria buildCriteria(MongoQueryParam param) throws Exception {
        if (MyCollectionUtil.single().isEmpty(param.getValue())) {
            return null;
        }
        Criteria criteria = Criteria.where(param.getName());
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
            criteria = criteria.nin(MyCollectionUtil.single().toArray(param.getValue()));
        } else if (MongoEnumVO.MongoOperatorEnum.IN.equals(param.getOperator())) {
            criteria = criteria.in(MyCollectionUtil.single().toArray(param.getValue()));
        } else if (MongoEnumVO.MongoOperatorEnum.ALL.equals(param.getOperator())) {
            criteria = criteria.all(MyCollectionUtil.single().toArray(param.getValue()));
        } else if (MongoEnumVO.MongoOperatorEnum.EXISTS.equals(param.getOperator())) {
            criteria = criteria.exists((Boolean) param.getValue());
        } else if (MongoEnumVO.MongoOperatorEnum.type.equals(param.getOperator())) {
            criteria = criteria.type(Integer.parseInt(param.getValue().toString()));
        } else if (MongoEnumVO.MongoOperatorEnum.BETWEEN.equals(param.getOperator())) {
            List<?> values = (List<?>) param.getValue();
            if (MongoEnumVO.DataType.Date.equals(param.getDataType())) {
                if (!ObjectUtils.isEmpty(values.get(0))) {
                    criteria = criteria.gte(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(values.get(0).toString()));
                }
                if (values.size() > 1 && !ObjectUtils.isEmpty(values.get(1))) {
                    criteria = criteria.lte(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(values.get(1).toString()));
                }
            } else if (DataType.UNIX_TIMESTAMP.equals(param.getDataType())) {
                if (!ObjectUtils.isEmpty(values.get(0))) {
                    criteria = criteria.gte(new Date(Long.parseLong(values.get(0).toString())));
                }
                if (values.size() > 1 && !ObjectUtils.isEmpty(values.get(1))) {
                    criteria = criteria.lte(new Date(Long.parseLong(values.get(1).toString())));
                }
            } else {
                criteria = criteria.gte(values.get(0)).lte(values.get(1));
            }
        } else if (MongoEnumVO.MongoOperatorEnum.REGEX.equals(param.getOperator())) {
            criteria = criteria.regex(param.getValue().toString());
        } else {
            log.info("您的操作暂时不支持");
        }
        if (criteria.getCriteriaObject().size() > 0) {
            return criteria;
        }
        return null;
    }

    public <T> PageInfo<T> paged(Long pageNum, Long pageSize, MongoQueryParamsDTO queryParamsDTO, Class<T> cls, String collectionName) throws Exception {
        Query query = MongoDBUtil.single().buildQuery(queryParamsDTO.getParams(), queryParamsDTO.getFields());
        PageInfo<T> pageInfo = new PageInfo<>();

        if (!queryParamsDTO.getOrders().isEmpty()) {
            query.with(Sort.by(queryParamsDTO.getOrders().stream().map(this::toOrder).collect(Collectors.toList())));
        }

        if (collectionName == null) {
            pageInfo.setTotal(mongoTemplate.count(query, cls));
        } else {
            pageInfo.setTotal(mongoTemplate.count(query, collectionName));
        }

        query.skip((pageNum - 1) * pageSize).limit(pageSize.intValue());
        if (collectionName == null) {
            pageInfo.setList(mongoTemplate.find(query, cls));
        } else {
            pageInfo.setList(mongoTemplate.find(query, cls, collectionName));
        }
        return pageInfo;
    }

    public <T> PageInfo<T> paged(Long pageNum, Long pageSize, MongoQueryParamsDTO queryParamsDTO, Class<T> cls) throws Exception {
        return paged(pageNum, pageSize, queryParamsDTO, cls, null);
    }

    public <T> List<T> list(MongoQueryParamsDTO queryParamsDTO, Class<T> cls, String collectionName) throws Exception {
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


    public <T> List<T> list(MongoQueryParamsDTO queryParamsDTO, Class<T> cls) throws Exception {
        return list(queryParamsDTO, cls, null);
    }

    public void updateByObjectIds(List<String> objectIds, Update update, Class<?> cls, String collectionName) {

    }

    public List<ObjectId> convertStrIdsToObjectIds(Collection<String> ids) {
        return ids.stream().map(ObjectId::new).collect(Collectors.toList());
    }

    public List<ObjectId> convertStrIdsToObjectIds(String[] ids) {
        return Arrays.stream(ids).map(ObjectId::new).collect(Collectors.toList());
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

    public <T> MongoQueryParamsDTO buildQueryParamsDTO(T query, MongoPagedParam mongoPagedParam) {
        if (query == null) {
            return new MongoQueryParamsDTO();
        }

        MongoQueryParamsDTO queryParams = new MongoQueryParamsDTO();

        PropertyDescriptor[] properties = BeanUtils.getPropertyDescriptors(query.getClass());

        for (PropertyDescriptor propertyDescriptor : properties) {
            String proName = propertyDescriptor.getName();

            if ("class".equals(proName)) {
                continue;
            }
            try {
                MongoQuery mongoQuery = query.getClass().getDeclaredField(propertyDescriptor.getName()).getAnnotation(MongoQuery.class);
                MongoOperatorEnum operator = MongoOperatorEnum.IS;
                if (mongoQuery != null && mongoQuery.operator() != null) {
                    operator = mongoQuery.operator();
                }
                if (mongoQuery != null && StringUtils.hasLength(mongoQuery.propName())) {
                    proName = mongoQuery.propName();
                }
                DataType dataType = DataType.String;
                if (mongoQuery != null && mongoQuery.dateType() != null) {
                    dataType = mongoQuery.dateType();
                }
                Object value = propertyDescriptor.getReadMethod().invoke(query);
                if (MyCollectionUtil.single().isNotEmpty(value)) {
                    queryParams.getParams().add(
                        MongoQueryParam.builder().relationalOperator(RelationalOperator.AND).name(proName).dataType(dataType).operator(operator).value(value)
                            .build());
                }
            } catch (Exception e) {
                log.error("{}生成查询条件时失败。{}", proName, e);
            }
        }

        if (MyCollectionUtil.single().isNotEmpty(mongoPagedParam.getSorts())) {
            queryParams.setOrders(buildSort(mongoPagedParam.getSorts()));
        }

        if (MyCollectionUtil.single().isNotEmpty(mongoPagedParam.getColumns())) {
            queryParams.setFields(buildShowColumn(mongoPagedParam.getColumns()));
        }
        return queryParams;
    }

    public Map<String, Boolean> buildShowColumn(String columns) {
        Map<String, Boolean> columnsMap = new LinkedHashMap<>();
        Arrays.stream(columns.split(",")).forEach(item -> columnsMap.put(item, true));
        return columnsMap;
    }

    public List<MongoQueryOrder> buildSort(String orderStr) {
        List<MongoQueryOrder> orders = new ArrayList<>();
        Arrays.stream(orderStr.split(",")).forEach(item -> {
            String[] colOrderArray = item.split(" ");
            if (colOrderArray.length == 1) {
                orders.add(MongoQueryOrder.builder().direction("ASC").property(colOrderArray[0]).build());
            } else {
                orders.add(MongoQueryOrder.builder().direction(colOrderArray[1].toUpperCase(Locale.ROOT)).property(colOrderArray[0]).build());
            }
        });
        return orders;
    }

    private static MongoDBUtil instance;

    private static MongoTemplate mongoTemplate;

    private static GridFsTemplate gridFsTemplate;

    public MongoDBUtil(MongoTemplate mongoTemplate, GridFsTemplate gridFsTemplate) {
        MongoDBUtil.mongoTemplate = mongoTemplate;
        MongoDBUtil.gridFsTemplate = gridFsTemplate;
        MongoDBUtil.instance = this;
    }

    public static MongoDBUtil single() {
        return MongoDBUtil.instance;
    }
}
