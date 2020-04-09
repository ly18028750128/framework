package com.longyou.comm.admin.controller;

import com.github.pagehelper.Constant;
import com.github.pagehelper.PageInfo;
import com.longyou.comm.admin.service.IDicService;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.exception.BusinessException;
import org.cloud.model.TSystemDicMaster;
import org.cloud.utils.SystemDicUtil;
import org.cloud.vo.QueryParamVO;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据字典服务类
 */
@RestController
@RequestMapping(value = "/admin/dic", produces = MediaType.APPLICATION_JSON_VALUE)
@SystemResource(path="系统管理/数据字典")
public class DicController {

    @Autowired
    IDicService dicService;

    @RequestMapping(value = "/page/list/{page}/{pageSize}", method = RequestMethod.POST)
    public ResponseResult listPage(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize, @RequestBody Map<String, Object> queryParams) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        QueryParamVO queryParamVO = new QueryParamVO(page, pageSize);
        queryParamVO.setParams(queryParams);
        responseResult.setData(new PageInfo(dicService.listPage(queryParamVO)));
        return responseResult;
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @SystemResource(value = "保存或者更新",description = "保存或者更新数据字典",authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public ResponseResult saveOrUpdate(@RequestBody List<TSystemDicMaster> systemDicMasterList) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        List<String> errorResultData = new ArrayList<>();
        for (TSystemDicMaster systemDicMaster : systemDicMasterList) {
            try {
                int count = dicService.SaveOrUpdate(systemDicMaster);
                if (count == 0) {
                    errorResultData.add(String.format(systemDicMaster.getDicMasterId() + ",未更新或者保存任何数据。"));
                }
            } catch (Exception e) {
                errorResultData.add(String.format(systemDicMaster.getDicMasterId() + ",更新或者保存失败！异常信息为[%s]", e.getMessage()));
            }
        }

        if (errorResultData.size() == systemDicMasterList.size()) {
            throw new BusinessException("更新或者保存失败", errorResultData);
        } else if (errorResultData.size() == 0) {
            responseResult.setMessage("全部成功");
        } else {
            responseResult.setMessage("部分成功");
            responseResult.setStatus(CoreConstant.RestStatus.PARTSUCCESS);
            responseResult.setErrResultData(errorResultData); // 部分成功时，将提示信息显示给前台
        }
//        responseResult.setData(systemDicMasterList);  // 返回数据重新覆盖掉页面的数据,页面还是重新查询一下吧
        return responseResult;
    }

        @RequestMapping(value = "/selectDicByDicMasterId/{dicMasterId}", method = RequestMethod.GET)
    public ResponseResult selectDicByDicMasterId(@PathVariable("dicMasterId") Long dicMasterId) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(dicService.getDicMasterById(dicMasterId));
        return responseResult;
    }

    @RequestMapping(value = "/selectDicItemsByDicMasterId/{dicMasterId}", method = RequestMethod.GET)
    public ResponseResult selectDicItemsByDicMasterId(@PathVariable("dicMasterId") Long dicMasterId) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(dicService.getDicItemsByMasterId(dicMasterId));
        return responseResult;
    }

    /**
     * 按diccode查询出数据字典项的列表
     *
     * @param params 其它参数
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selectDicItemsByDicCode", method = RequestMethod.GET)
    public ResponseResult selectDicItemsByDicCode(@RequestParam Map<String, Object> params)
            throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(dicService.getDicItemsByDicCode(params));
        return responseResult;
    }

    /**
     * 刷新缓存
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/refreshCache", method = RequestMethod.GET)
    @SystemResource(value = "刷新缓存",description = "刷新缓存，需授权",authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public ResponseResult refreshCache()
            throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        dicService.refreshCache();
        return responseResult;
    }

    @RequestMapping(value = "/selectDicItemsByDicCodeFromCache", method = RequestMethod.GET)
    public ResponseResult selectDicItemsByDicCodeFromCache(@RequestParam("microServiceName") String microServiceName,
                                                           @RequestParam("dicCode") String dicCode,
                                                           @RequestParam(name = "language", required = false) String language
    ) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        language = (language == null ? CoreConstant.SystemSupportLanguage.ZH_CN.value() : language);
        responseResult.setData(SystemDicUtil.single().getDicItemList(microServiceName, dicCode, language));
        return responseResult;
    }

    @RequestMapping(value = "/getDicItem", method = RequestMethod.GET)
    public ResponseResult getDicItem(@RequestParam("microServiceName") String microServiceName,
                                     @RequestParam("dicCode") String dicCode,
                                     @RequestParam(name = "language", required = false) String language
    ) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        language = (language == null ? CoreConstant.SystemSupportLanguage.ZH_CN.value() : language);
        responseResult.setData(SystemDicUtil.single().getDicItemList(microServiceName, dicCode, language));
        return responseResult;
    }

}
