package com.longyou.comm.conntroller;

import com.longyou.comm.service.FrameDataDimensionService;
import java.util.List;
import java.util.Map;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.exception.BusinessException;
import org.cloud.model.FrameDataDimension;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.ValidateUtil;
import org.cloud.validator.GroupForUpdate;
import org.cloud.vo.ResponseResult;
import org.cloud.vo.ValidateResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @SystemResource(value = "queryDataDimensionListByTypeAndId", description = "按类型查询数据权限", authMethod = AuthMethod.BYUSERPERMISSION)
    @GetMapping("/queryDataDimensionListByTypeAndId")
    public ResponseResult queryDataDimensionListByTypeAndId(@RequestParam("dataDimensionType") String dataDimensionType,
        @RequestParam("referId") Long referId, @RequestParam(value = "status", required = false) Integer status) throws Exception {
        ResponseResult successResponseResult = ResponseResult.createSuccessResult();
        successResponseResult.setData(frameDataDimensionService.selectDataDimensionByUserId(dataDimensionType, referId, status));
        return successResponseResult;
    }


    @SystemResource(value = "insertOrUpdateBatch", description = "批量更新数据权限", authMethod = AuthMethod.BYUSERPERMISSION)
    @PostMapping("/insertOrUpdateBatch")
    public ResponseResult insertOrUpdateBatch(@RequestBody List<FrameDataDimension> records)
        throws Exception {

        Map<Integer, List<ValidateResultVO>> validateResult = ValidateUtil.single().validate(records, GroupForUpdate.class);
        if (CollectionUtil.single().isNotEmpty(validateResult)) {
            throw new BusinessException("", validateResult);
        }

        ResponseResult successResponseResult = ResponseResult.createSuccessResult();
        successResponseResult.setData(frameDataDimensionService.insertOrUpdateBatch(records));
        return successResponseResult;
    }

    /**
     * 查询用户的全部有效的数据权限列表
     *
     * @param userId 用户userId
     * @return
     * @throws Exception
     */
    @SystemResource(value = "selectDataDimensionByUserId", description = "查询用户有效的数据权限列表", authMethod = AuthMethod.BYUSERPERMISSION)
    @GetMapping("/selectDataDimensionByUserId")
    public ResponseResult selectDataDimensionByUserId(@RequestParam("userId") Long userId) throws Exception {
        ResponseResult successResponseResult = ResponseResult.createSuccessResult();
        successResponseResult.setData(frameDataDimensionService.selectDataDimensionByUserId(userId));
        return successResponseResult;
    }
}
