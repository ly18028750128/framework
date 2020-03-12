package org.cloud.core.mongo;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.longyou.comm.starter.CommonServiceApplication;
import org.cloud.mybatis.dynamic.DynamicSqlUtil;
import org.cloud.vo.DynamicSqlQueryParamsVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommonServiceApplication.class}, properties = "classpath:testYml.yml")
public class DataiterfaceTest {

    Logger logger = LoggerFactory.getLogger(DataiterfaceTest.class);

    @Test
    public void testDynamicSql() throws Exception {

//        PageHelper.startPage(1,2);

//        Page<?> pageResult = DynamicSqlUtil.single().pagedData("5e697fe30eccd006879fab6b", new HashMap<>());

//        PageInfo pageInfo = new PageInfo(pageResult);

//        select * from t_frame_user
//                <where>
//	<if test="userName!=null">
//                user_name = #{userName,jdbcType=VARCHAR}
//	</if>
//	<if test="password!=null">
//                and	password = #{password,jdbcType=VARCHAR}
//	</if>
//	<if test="sex!=null">
//                and sex in
//		  <foreach item="item" collection="sexList" separator="," open="(" close=")" index="">
//		   #{item.id, jdbcType=VARCHAR}
//		 </foreach>
//	</if>
//</where>

        DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO = new DynamicSqlQueryParamsVO();

        PageInfo<?> result = null;
        Map<String, Object> params = new HashMap<>();
        dynamicSqlQueryParamsVO.setParams(params);
        logger.info("1:"+ JSON.toJSONString(DynamicSqlUtil.single().pagedData("5e697fe30eccd006879fab6b", 2, 2, dynamicSqlQueryParamsVO)));
        logger.info("1:listData:"+ JSON.toJSONString(DynamicSqlUtil.single().listData("5e697fe30eccd006879fab6b", dynamicSqlQueryParamsVO)));
        logger.info("1:"+JSON.toJSONString(dynamicSqlQueryParamsVO));
        logger.info("1:"+DynamicSqlUtil.single().localSql.get());

        params.put("userName","admin");
        dynamicSqlQueryParamsVO.setParams(params);
        dynamicSqlQueryParamsVO.setParams(params);
        logger.info("2:"+ JSON.toJSONString(DynamicSqlUtil.single().pagedData("5e697fe30eccd006879fab6b", 1, 2, dynamicSqlQueryParamsVO)));
        logger.info("2:listData:"+ JSON.toJSONString(DynamicSqlUtil.single().listData("5e697fe30eccd006879fab6b", dynamicSqlQueryParamsVO)));
        logger.info("2:"+JSON.toJSONString(dynamicSqlQueryParamsVO));
        logger.info("2:"+DynamicSqlUtil.single().localSql.get());

        params.put("password","3e2345598780429e4758006e801f4b88");
        logger.info("3:"+ JSON.toJSONString(DynamicSqlUtil.single().pagedData("5e697fe30eccd006879fab6b", 1, 2, dynamicSqlQueryParamsVO)));
        logger.info("3:listData:"+ JSON.toJSONString(DynamicSqlUtil.single().listData("5e697fe30eccd006879fab6b", dynamicSqlQueryParamsVO)));
        logger.info("3:"+JSON.toJSONString(dynamicSqlQueryParamsVO));
        logger.info("3:"+DynamicSqlUtil.single().localSql.get());


        params.put("sexList",new String[]{"F","M"});
        logger.info("5:"+ JSON.toJSONString(DynamicSqlUtil.single().pagedData("5e697fe30eccd006879fab6b", 1, 2, dynamicSqlQueryParamsVO)));
        logger.info("5:listData:"+ JSON.toJSONString(DynamicSqlUtil.single().listData("5e697fe30eccd006879fab6b", dynamicSqlQueryParamsVO)));
        logger.info("5:"+JSON.toJSONString(dynamicSqlQueryParamsVO));
        logger.info("5:"+DynamicSqlUtil.single().localSql.get());




    }
}
