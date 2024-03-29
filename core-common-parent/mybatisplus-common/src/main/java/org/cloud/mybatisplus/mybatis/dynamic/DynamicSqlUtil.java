package org.cloud.mybatisplus.mybatis.dynamic;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.unknow.first.mongo.vo.DataInterFaceVO;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.cloud.exception.BusinessException;
import org.cloud.mybatisplus.vo.DynamicSqlQueryParamsVO;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.SpringContextUtil;
import org.cloud.mybatisplus.vo.JavaBeanResultMap;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

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

    public String getListSql(final String md5) throws Exception {
        if (mongoTemplate == null) {
            throw new BusinessException("找不到mongodb的配置", "不能执行动态SQL");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("md5").is(md5));
        DataInterFaceVO dataInterFaceVO = mongoTemplate.findOne(query, DataInterFaceVO.class);
        return dataInterFaceVO.getUrlOrSqlContent();
    }

    /**
     * 根据后台的数据接口定义查询数据，不分页
     *
     * @param md5
     * @param dynamicSqlQueryParamsVO
     * @return
     * @throws Exception
     */
    public Page<JavaBeanResultMap> listData(String md5, final DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO) throws Exception {
        return listDataBySqlContext(this.getListSql(md5), dynamicSqlQueryParamsVO);
    }


    /**
     * 根据SQL内容查询列表，不分页
     *
     * @param sqlContext
     * @param dynamicSqlQueryParamsVO
     * @return
     * @throws Exception
     */
    public Page<JavaBeanResultMap> listDataBySqlContext(String sqlContext, final DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO) throws Exception {
        createDynamicSqlBySqlContext(sqlContext, dynamicSqlQueryParamsVO.getParams());
        if (!StringUtils.isEmpty(dynamicSqlQueryParamsVO.getSorts())) {
            PageHelper.orderBy(dynamicSqlQueryParamsVO.getSorts());
        }
        return dynamicSqlMapper.list(localSql.get(), dynamicSqlQueryParamsVO.getParams());
    }

    public <T> T getSingleDataVO(String sqlContext, final DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO, Class<T> cls) throws Exception {
        Page<JavaBeanResultMap> resultMaps = this.listDataBySqlContext(sqlContext, dynamicSqlQueryParamsVO);

        if (CollectionUtil.single().isEmpty(resultMaps)) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(resultMaps.get(0)), cls);

    }

    /**
     * 根据dataInterfacevo的查询配置列表，分页
     *
     * @param md5
     * @param pageNum
     * @param pageSize
     * @param dynamicSqlQueryParamsVO
     * @return
     * @throws Exception
     */
    public PageInfo<JavaBeanResultMap> pagedData(String md5, int pageNum, int pageSize, DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO) throws Exception {
        return pagedDataBySqlContext(this.getListSql(md5), pageNum, pageSize, dynamicSqlQueryParamsVO);
    }

    /**
     * 根据sql内容查询配置，分页
     *
     * @param sqlContext
     * @param pageNum
     * @param pageSize
     * @param dynamicSqlQueryParamsVO
     * @return
     * @throws Exception
     */
    public PageInfo<JavaBeanResultMap> pagedDataBySqlContext(String sqlContext, int pageNum, int pageSize, DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO) throws Exception {
        createDynamicSqlBySqlContext(sqlContext, dynamicSqlQueryParamsVO.getParams());
        if (StringUtils.isEmpty(dynamicSqlQueryParamsVO.getSorts())) {
            PageHelper.startPage(pageNum, pageSize);
        } else {
            PageHelper.startPage(pageNum, pageSize, dynamicSqlQueryParamsVO.getSorts());
        }
        Page<JavaBeanResultMap> listPage = dynamicSqlMapper.paged(localSql.get(), dynamicSqlQueryParamsVO.getParams());
        return new PageInfo<>(listPage);
    }

    public void createDynamicSql(String md5, final Map<String, Object> params) throws Exception {
        createDynamicSqlBySqlContext(getListSql(md5), params);
    }

    /**
     * 按SQL内容查询数据，这里不能开放API使用，仅内部谨慎使用
     *
     * @param sqlContext sql脚本，mysql格式
     * @param params     参数
     * @throws Exception
     */
    public void createDynamicSqlBySqlContext(String sqlContext, final Map<String, Object> params) throws Exception {
        XMLScriptBuilderService xmlScriptBuilderService = SpringContextUtil.getBean(XMLScriptBuilderService.class);
        sqlContext = "<script>" + sqlContext + "</script>";
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
