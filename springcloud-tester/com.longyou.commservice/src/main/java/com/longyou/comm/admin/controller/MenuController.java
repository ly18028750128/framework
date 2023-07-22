package com.longyou.comm.admin.controller;

import static com.longyou.comm.admin.service.IMenuService._ALL_MENUS_CACHE_KEY;

import com.longyou.comm.admin.service.IMenuService;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.model.TFrameMenu;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/menu")
@SystemResource(path = "/admin/menu")
public class MenuController {

    @Autowired
    IMenuService menuService;

    @Autowired
    RedisUtil redisUtil;

    @SystemResource(value = "listAllTreeMenuByParentId", description = "根据ID查询菜单及子菜单", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @GetMapping("/listAllTreeMenuByParentId")
    public ResponseResult listAllTreeMenuByParentId(
        @RequestParam(name = "parentId", required = false) Integer parentId,
        @RequestParam(name = "maxLevel", required = false) Integer maxLevel,
        @RequestParam(name = "isAll", required = false,defaultValue = "true") boolean isAll // 是否显示全部
    ) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(menuService.listAllTreeMenu(parentId, ((maxLevel == null || maxLevel == 0) ? 10 : maxLevel), isAll));
        return responseResult;
    }

    @SystemResource(value = "updateMenuById", description = "根据ID更新菜单", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @PostMapping("/updateMenuById")
    public ResponseResult updateMenuById(@RequestBody TFrameMenu tFrameMenu) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(menuService.updateMenuById(tFrameMenu));
        return responseResult;
    }

    @SystemResource(value = "insertMenu", description = "插入菜单", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @PostMapping("/insertMenu")
    public ResponseResult insertMenu(@RequestBody TFrameMenu tFrameMenu) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        tFrameMenu.setMenuId(null);
        menuService.insertMenu(tFrameMenu);
        responseResult.setData(tFrameMenu.getMenuId());
        return responseResult;
    }

    //    @MfaAuth
    @SystemResource(value = "refreshCache", description = "刷新菜单缓存", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @PostMapping("/refreshCache")
    public ResponseResult refreshCache() throws Exception {
        ResponseResult result = ResponseResult.createSuccessResult();
        result.setData(redisUtil.set(_ALL_MENUS_CACHE_KEY, menuService.listAllTreeMenu(null, 5, false), 0L));
        return result;
    }


}
