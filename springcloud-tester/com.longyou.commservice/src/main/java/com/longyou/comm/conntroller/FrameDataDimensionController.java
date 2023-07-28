package com.longyou.comm.conntroller;

import com.longyou.comm.service.FrameDataDimensionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.exception.BusinessException;
import org.cloud.model.FrameDataDimension;
import org.cloud.utils.CollectionUtil;
import org.cloud.validator.GroupForUpdate;
import org.cloud.validator.utils.ValidateUtil;
import org.cloud.vo.ResponseResult;
import org.cloud.vo.ValidateResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value="FrameDataDimensionController",tags = "数据权限配置接口")
@RestController
@SystemResource(path = "/admin/dataDimension")
@RequestMapping("/admin/dataDimension")
public class FrameDataDimensionController {

    @Autowired
    FrameDataDimensionService frameDataDimensionService;

    /**
     * 通过类型和ID查询
     *
     * @param dataDimensionType 数据权限类型，10：角色 20：用户数据权限
     * @param referId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询用户的数据权限点")
    @SystemResource(value = "queryDataDimensionListByTypeAndId", description = "按类型查询数据权限", authMethod = AuthMethod.BYUSERPERMISSION)
    @GetMapping("/queryDataDimensionListByTypeAndId")
    public ResponseResult<FrameDataDimension> queryDataDimensionListByTypeAndId(@RequestParam("dataDimensionType") String dataDimensionType,
        @RequestParam("referId") Long referId, @RequestParam(value = "status", required = false) Integer status) throws Exception {
        return ResponseResult.createSuccessResult(frameDataDimensionService.selectDataDimensionByUserId(dataDimensionType, referId, status));
    }

    @ApiOperation(value = "批量更新数据权限")
    @SystemResource(value = "insertOrUpdateBatch", description = "批量更新数据权限", authMethod = AuthMethod.BYUSERPERMISSION)
    @PostMapping("/insertOrUpdateBatch")
    public ResponseResult<Integer> insertOrUpdateBatch(@RequestBody List<FrameDataDimension> records)
        throws Exception {
        Map<Integer, List<ValidateResultVO>> validateResult = ValidateUtil.single().validate(records, GroupForUpdate.class);
        if (CollectionUtil.single().isNotEmpty(validateResult)) {
            throw new BusinessException("参数校验失败!", validateResult);
        }
        return ResponseResult.createSuccessResult(frameDataDimensionService.insertOrUpdateBatch(records));
    }

    /**
     * 查询用户的全部有效的数据权限列表
     *
     * @param userId 用户userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询用户有效的数据权限列表")
    @SystemResource(value = "selectDataDimensionByUserId", description = "查询用户有效的数据权限列表", authMethod = AuthMethod.BYUSERPERMISSION)
    @GetMapping("/selectDataDimensionByUserId")
    public ResponseResult<FrameDataDimension> selectDataDimensionByUserId(@RequestParam("userId") Long userId) throws Exception {
        return ResponseResult.createSuccessResult(frameDataDimensionService.selectDataDimensionByUserId(userId));
    }
}
