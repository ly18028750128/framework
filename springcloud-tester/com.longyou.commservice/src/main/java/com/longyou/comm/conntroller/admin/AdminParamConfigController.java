package com.longyou.comm.conntroller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.longyou.comm.model.ParamConfig;
import com.longyou.comm.service.ParamConfigService;
import com.unknow.first.api.common.CommonPage;
import com.unknow.first.api.common.CommonParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.cloud.constant.CoreConstant;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.logs.annotation.AuthLog;
import org.cloud.vo.CommonApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "ParamConfigController", tags = "后台管理-参数配置管理")
@RestController
@RequestMapping("/paramConfig")
@SystemResource(path = "/paramConfig")
public class AdminParamConfigController {

    @Autowired
    private ParamConfigService paramConfigService;

    @ApiOperation(value = "管理员获取参数配置列表", nickname = "管理员获取参数配置列表nick", notes = "支持模糊查询")
    @SystemResource(value = "/paramConfigList", description = "管理员获取参数配置列表", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public CommonApiResult<CommonPage<ParamConfig>> list(String code, String name, CommonParam commonParam) {
        LambdaQueryWrapper<ParamConfig> queryWrapper = Wrappers.lambdaQuery(ParamConfig.class)
            .eq(!ObjectUtils.isEmpty(code), ParamConfig::getConfigCode, code)
            .like(!ObjectUtils.isEmpty(name), ParamConfig::getConfigName, name);
        PageHelper.startPage(commonParam.getPage(), commonParam.getLimit(), commonParam.getSorts());
        List<ParamConfig> list = paramConfigService.list(queryWrapper);
        CommonPage<ParamConfig> page = CommonPage.restPage(list);
        return CommonApiResult.createSuccessResult(page);
    }

    @AuthLog(bizType = "admin.update.paramConfig", desc = "管理员更新参数配置")
    @ApiOperation(value = "管理员更新参数配置", notes = "后台管理用，ID必传")
    @SystemResource(value = "/updateParamConfig", description = "管理员更新参数配置", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.POST, value = "/updateParamConfig")
    public CommonApiResult<Boolean> updateParamConfig(@RequestBody ParamConfig paramConfig) {
        Assert.isTrue(paramConfig.getId() != null, "system.error.paramConfig.id.isNull");
        paramConfigService.updateByPrimaryKeySelective(paramConfig);
        return CommonApiResult.createSuccessResult(true);
    }
}
