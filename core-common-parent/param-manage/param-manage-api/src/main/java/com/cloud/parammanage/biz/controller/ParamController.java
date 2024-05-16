package com.cloud.parammanage.biz.controller;

import com.cloud.parammanage.common.constants.ParamFieldConstants;
import com.cloud.parammanage.common.entity.ParamConfig;
import com.cloud.parammanage.common.service.ParamConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.cloud.vo.CommonApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "ParamController", tags = "配置参数管理")
@RestController
@RequestMapping("/param")
public class ParamController {

    @Autowired
    private ParamConfigService paramConfigService;

    @ApiOperation(value = "获取参数", notes = "{POS挖矿日产出比例: POS_MINING_DAYS_OUTPUT_RADIO}")
    @ApiImplicitParam(value = "要获取的参数code列表", name = "codeList", required = true, paramType = "query", dataType = "List")
    @RequestMapping(method = RequestMethod.GET, value = "/get")
    public CommonApiResult<Map<String, Object>> get(@RequestParam List<String> codeList){

        HashMap<String, Object> res = new HashMap<>();
        for (String code : codeList) {
            ParamConfig paramConfig = paramConfigService.get(code);
            if (paramConfig != null && paramConfig.getConfigType() == ParamFieldConstants.ConfigType.PUBLIC.getValue()){
                res.put(paramConfig.getConfigCode(), paramConfig.getConfigValue());
            }
        }
        return CommonApiResult.createSuccessResult(res);
    }



}
