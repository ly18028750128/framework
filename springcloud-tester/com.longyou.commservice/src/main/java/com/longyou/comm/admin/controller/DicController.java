package com.longyou.comm.admin.controller;

import com.github.pagehelper.PageInfo;
import com.longyou.comm.admin.service.IDicService;
import org.cloud.vo.QueryParamVO;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 数据字典服务类
 */
@RestController
@RequestMapping(value = "/admin/dic",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DicController {

    @Autowired
    IDicService dicService;

    @RequestMapping(value = "/page/list/{page}/{pageSize}", method = RequestMethod.POST)
    public ResponseResult listPage(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize,@RequestBody Map<String,Object> queryParams) throws Exception{
        ResponseResult responseResult =  ResponseResult.createSuccessResult();
        QueryParamVO queryParamVO = new QueryParamVO(page,pageSize);
        queryParamVO.setParams(queryParams);
        responseResult.setData(new PageInfo(dicService.listPage(queryParamVO)));
        return responseResult;
    }

}
