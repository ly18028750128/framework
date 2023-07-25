package org.cloud.mybatis.dynamic;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.cloud.vo.JavaBeanResultMap;

import java.util.Map;

public interface DynamicSqlMapper {


    Page<JavaBeanResultMap<Object>> paged(@Param("sql") String sql, @Param("params") Map<String, Object> params);

    /**
     * 执行脚本
     *
     * @param params
     * @return
     */
    Page<JavaBeanResultMap<Object>> list(@Param("sql") String sql, @Param("params") Map<String, Object> params);

}
