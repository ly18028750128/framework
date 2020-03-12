package org.cloud.mybatis.dynamic;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.cloud.vo.JavaBeanResultMap;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

public interface DynamicSqlMapper {
    public Page<JavaBeanResultMap<Object>> paged(@Param("sql") String sql,@Param("params") Map<String, Object> params);
    /**
     * 执行脚本
     *
     * @param params
     * @return
     */
    public Page<JavaBeanResultMap<Object>> list(@Param("sql") String sql,@Param("params") Map<String, Object> params);

}
