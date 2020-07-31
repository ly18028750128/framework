package org.cloud.controller;

import org.cloud.context.RequestContextManager;
import org.cloud.mongo.DataInterFaceVO;
import org.cloud.mybatis.dynamic.DynamicSqlUtil;
import org.cloud.utils.AuthCheckUtils;
import org.cloud.vo.DynamicSqlQueryParamsVO;
import org.cloud.vo.ResponseResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 动态Sql接口执行controller
 */
@RestController
@RequestMapping("/common/dynamic/sql")
public class DataInterfaceExecController {
    @PostMapping("/paged/{md5}/{pageNum}/{pageSize}")
    public ResponseResult paged(@PathVariable("md5") String md5,
                                @PathVariable("pageNum") int pageNum,
                                @PathVariable("pageSize") int pageSize,
                                @RequestBody DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO
    ) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        final DataInterFaceVO dataInterFaceVO = AuthCheckUtils.single().checkDataInterface(md5);
        addCurrentUserId(dataInterFaceVO, dynamicSqlQueryParamsVO.getParams());
        responseResult.setData(DynamicSqlUtil.single().pagedData(md5, pageNum, pageSize, dynamicSqlQueryParamsVO));
        return responseResult;
    }

    @PostMapping("/list/{md5}")
    public ResponseResult list(@PathVariable("md5") String md5,
                               @RequestBody DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO
    ) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        final DataInterFaceVO dataInterFaceVO = AuthCheckUtils.single().checkDataInterface(md5);
        addCurrentUserId(dataInterFaceVO, dynamicSqlQueryParamsVO.getParams());
        responseResult.setData(DynamicSqlUtil.single().listData(md5, dynamicSqlQueryParamsVO));
        return responseResult;
    }

    /**
     * 根据当前的sql增加当前用户的查询条件
     *
     * @param dataInterFaceVO
     * @param params
     */
    private void addCurrentUserId(DataInterFaceVO dataInterFaceVO, Map<String, Object> params) {
        final String _CURRENT_USER_KEY = "paramsCurrentUserId";    // 如果SQL中包包含了这个查询参数，那么增加一个当前用户ID的参数
        if (dataInterFaceVO.getUrlOrSqlContent().indexOf(_CURRENT_USER_KEY) > -1) {
            params.put(_CURRENT_USER_KEY, RequestContextManager.single().getRequestContext().getUser().getId());
        }
    }

}
