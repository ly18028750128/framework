package com.longyou.aihelper.controller;

import com.longyou.aihelper.model.Responsetable;
import com.longyou.aihelper.service.ResponsetableService;
import com.longyou.aihelper.util.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/responseTable")
public class ResponsetableController {

  @Autowired
  ResponsetableService responsetableService;

  @ApiOperation(value = "新增问答")
  @Transactional(rollbackFor = Exception.class)
  @PostMapping("/insert")
  public CommonResult<Responsetable> insert(@RequestBody Responsetable rest ) throws Exception {
    responsetableService.insert(rest);
    return CommonResult.success();
  }

  

}
