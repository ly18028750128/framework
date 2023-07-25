package org.cloud.mybatisplus.mybatis.dynamic;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.cloud.mybatisplus.vo.JavaBeanResultMap;

import java.util.Map;

public interface DynamicSqlMapper {


    Page<JavaBeanResultMap> paged(@Param("sql") String sql, @Param("params") Map<String, Object> params);

    /**
     * 执行脚本
     *
     * @param params
     * @return
     */
    Page<JavaBeanResultMap> list(@Param("sql") String sql, @Param("params") Map<String, Object> params);

}
