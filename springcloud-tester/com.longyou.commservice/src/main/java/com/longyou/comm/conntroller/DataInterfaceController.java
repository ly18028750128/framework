package com.longyou.comm.conntroller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.longyou.comm.service.DataInterfaceService;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.vo.QueryParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * 已经迁移到mongo数据库中,这个类不再使用
 * **/
@RestController
@RequestMapping("/common/config/datainterface")
@SystemResource(path = "系统管理/接口配置", index = 1,
        menuName = "接口配置", menuCode = "menu_datainterface_config",
        parentMenuCode = "menu_system_manager", parentMenuName = "系统管理")
@Deprecated
public class DataInterfaceController {
    @Autowired
    DataInterfaceService dataInterfaceService;

    @SystemResource(value = "datainterface_page_query", description = "查询接口列表分页", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION, index = 1)
    @RequestMapping(value = "/page/list/{page}/{pageSize}", method = RequestMethod.POST)
    public PageInfo<?> selectPageList(@PathVariable("page") int pageNum, @PathVariable("pageSize") int pageSize, @RequestBody QueryParamVO queryParamVO) throws Exception {
        queryParamVO.setPageNum(pageNum);
        queryParamVO.setPageSize(pageSize);
        Page<?> result = dataInterfaceService.selectPageList(queryParamVO);
        return new PageInfo<>(result);
    }

    @RequestMapping(value = "/params/page/list/{page}/{pageSize}", method = RequestMethod.POST)
    @SystemResource(value = "datainterface_params_page_query", description = "查询接口参数列表分页", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION, index = 1)
    public PageInfo<?> selectDataInterfaceParamsPageList(@PathVariable("page") int pageNum, @PathVariable("pageSize") int pageSize, @RequestBody QueryParamVO queryParamVO) throws Exception {

        queryParamVO.setPageNum(pageNum);
        queryParamVO.setPageSize(pageSize);
        Page<?> result = dataInterfaceService.selectDataInterfaceParamsPageList(queryParamVO);

        return new PageInfo<>(result);
    }
}
