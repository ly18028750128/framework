package com.longyou.comm.admin.controller;

import com.longyou.comm.admin.service.IMenuService;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/menu")
public class MenuController {

    @Autowired
    IMenuService menuService;

    @GetMapping("/listAllTreeMenuByParentId")
    public ResponseResult listAllTreeMenuByParentId(
            @RequestParam(name = "parentId", required = false) Integer parentId,
            @RequestParam(name = "maxLevel", required = false) Integer maxLevel
    ) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(menuService.listAllTreeMenu(parentId, (maxLevel == null || maxLevel == 0) ? 10 : maxLevel));
        return responseResult;
    }

}
