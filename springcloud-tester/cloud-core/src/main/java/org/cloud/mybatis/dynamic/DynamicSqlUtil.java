package org.cloud.mybatis.dynamic;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.bson.types.ObjectId;
import org.cloud.exception.BusinessException;
import org.cloud.mongo.DataInterFaceVO;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.DynamicSqlQueryParamsVO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DynamicSqlUtil {
    MongoTemplate mongoTemplate = null;
    DynamicSqlMapper dynamicSqlMapper = null;
    public final ThreadLocal<String> localSql = new ThreadLocal<String>();

    private static class Instance {
        private Instance() {
        }

        private static DynamicSqlUtil instance = new DynamicSqlUtil();
    }

    private DynamicSqlUtil() {
        mongoTemplate = SpringContextUtil.getBean(MongoTemplate.class);
        dynamicSqlMapper = SpringContextUtil.getBean(DynamicSqlMapper.class);
    }

    public static DynamicSqlUtil single() {
        return Instance.instance;
    }

    public String getListSql(final String objectId) throws Exception {
        if (mongoTemplate == null) {
            throw new BusinessException("找不到mongodb的配置", "不能执行动态SQL");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(objectId)));
        DataInterFaceVO dataInterFaceVO = mongoTemplate.findOne(query, DataInterFaceVO.class);
        return dataInterFaceVO.getUrlOrSqlContent();
    }

    public Page<?> listData(String objectId, final DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO) throws Exception {
        createDynamicSql(objectId, dynamicSqlQueryParamsVO.getParams());
        if (!StringUtils.isEmpty(dynamicSqlQueryParamsVO.getSorts())) {
            PageHelper.orderBy(dynamicSqlQueryParamsVO.getSorts());
        }
        return dynamicSqlMapper.list(localSql.get(), dynamicSqlQueryParamsVO.getParams());
    }

    public PageInfo<?> pagedData(String objectId, int pageNum, int pageSize, DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO) throws Exception {
        createDynamicSql(objectId, dynamicSqlQueryParamsVO.getParams());
        if (StringUtils.isEmpty(dynamicSqlQueryParamsVO.getSorts())) {
            PageHelper.startPage(pageNum, pageSize);
        } else {
            PageHelper.startPage(pageNum, pageSize, dynamicSqlQueryParamsVO.getSorts());
        }
        Page<?> listPage = dynamicSqlMapper.paged(localSql.get(), dynamicSqlQueryParamsVO.getParams());
        return new PageInfo<>(listPage);
    }


    public void createDynamicSql(String objectId, final Map<String, Object> params) throws Exception {
        XMLScriptBuilderService xmlScriptBuilderService = SpringContextUtil.getBean(XMLScriptBuilderService.class);
        String sqlContext = "<script>" + getListSql(objectId) + "</script>";
        XMLScriptBuilder xmlScriptBuilder = xmlScriptBuilderService.getXMLScriptBuilder(sqlContext, "//script", params.getClass());
        BoundSql boundSql = xmlScriptBuilder.parseScriptNode().getBoundSql(params);
        String sql = boundSql.getSql();
        for (ParameterMapping param : boundSql.getParameterMappings()) {
            String jdbcType = param.getJdbcType() == null ? "" : (",jdbcType=" + param.getJdbcType().name());
            String strParm = "#{params." + param.getProperty() + jdbcType + "}";
            sql = sql.replaceFirst("\\?", strParm);
            Object additionValue = boundSql.getAdditionalParameter(param.getProperty());
            if (additionValue != null) {
                params.put(param.getProperty(), additionValue);
            }
        }
        localSql.set(sql);
    }

}
