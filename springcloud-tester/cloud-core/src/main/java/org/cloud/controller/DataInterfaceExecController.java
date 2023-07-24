package org.cloud.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cloud.context.RequestContextManager;
import org.cloud.mongo.DataInterFaceVO;
import org.cloud.mybatis.dynamic.DynamicSqlUtil;
import org.cloud.utils.AuthCheckUtils;
import org.cloud.vo.DynamicSqlQueryParamsVO;
import org.cloud.vo.JavaBeanResultMap;
import org.cloud.vo.ResponseResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 动态Sql接口执行controller
 */
@Api(value = "DataInterfaceExecController",tags = "动态SQL执行接口")
@RestController
@RequestMapping("/common/dynamic/sql")
public class DataInterfaceExecController {

    @ApiOperation(value = "分页查询",notes = "分页查询,传入接口的MD5值")
    @PostMapping("/paged/{md5}/{pageNum}/{pageSize}")
    public ResponseResult<PageInfo<JavaBeanResultMap<Object>>> paged(@PathVariable("md5") String md5,
        @PathVariable("pageNum") int pageNum,
        @PathVariable("pageSize") int pageSize,
        @RequestBody DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO
    ) throws Exception {
        final DataInterFaceVO dataInterFaceVO = AuthCheckUtils.single().checkDataInterface(md5);
        addCurrentUserId(dataInterFaceVO, dynamicSqlQueryParamsVO.getParams());
        return ResponseResult.createSuccessResult(DynamicSqlUtil.single().pagedData(md5, pageNum, pageSize, dynamicSqlQueryParamsVO));
    }

    @ApiOperation(value = "不分页查询",notes = "分页查询,传入接口的MD5值")
    @PostMapping("/list/{md5}")
    public ResponseResult<JavaBeanResultMap<Object>> list(@PathVariable("md5") String md5,
        @RequestBody DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO
    ) throws Exception {
        final DataInterFaceVO dataInterFaceVO = AuthCheckUtils.single().checkDataInterface(md5);
        addCurrentUserId(dataInterFaceVO, dynamicSqlQueryParamsVO.getParams());
        return ResponseResult.createSuccessResult(DynamicSqlUtil.single().listData(md5, dynamicSqlQueryParamsVO));
    }

    /**
     * 根据当前的sql增加当前用户的查询条件
     *
     * @param dataInterFaceVO  接口定义
     * @param params 接口参数
     */
    private void addCurrentUserId(DataInterFaceVO dataInterFaceVO, Map<String, Object> params) {
        final String _CURRENT_USER_KEY = "paramsCurrentUserId";    // 如果SQL中包包含了这个查询参数，那么增加一个当前用户ID的参数
        if (dataInterFaceVO.getUrlOrSqlContent().contains(_CURRENT_USER_KEY)) {
            params.put(_CURRENT_USER_KEY, RequestContextManager.single().getRequestContext().getUser().getId());
        }
    }

}
