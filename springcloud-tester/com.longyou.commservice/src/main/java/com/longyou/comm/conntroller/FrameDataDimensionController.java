package com.longyou.comm.conntroller;

import com.longyou.comm.service.FrameDataDimensionService;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
        @RequestParam("referId") Long referId) throws Exception {

        ResponseResult successResponseResult = ResponseResult.createSuccessResult();

        successResponseResult.setData(frameDataDimensionService.selectDataDimensionByUserId(dataDimensionType, referId));

        return successResponseResult;


    }

}
