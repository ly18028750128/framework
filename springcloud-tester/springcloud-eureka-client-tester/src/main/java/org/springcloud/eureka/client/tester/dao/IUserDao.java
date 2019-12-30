package org.springcloud.eureka.client.tester.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.vo.JavaBeanResultMap;

import java.util.Map;
@Mapper
public interface IUserDao {
    @Select({
            "<script>",
            "select * from t_frame_user where user_name = #{userName}",
            "</script>"
    })
    public JavaBeanResultMap<Object> getUserByName(@Param("userName") String userName);
}
