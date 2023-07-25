package org.cloud.mybatisplus.mybatis.dynamic;

import com.github.pagehelper.Page;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.cloud.mybatisplus.vo.JavaBeanResultMap;

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
