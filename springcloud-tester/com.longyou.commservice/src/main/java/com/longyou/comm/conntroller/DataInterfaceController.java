package com.longyou.comm.conntroller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.longyou.comm.model.TFrameDataInterface;
import com.longyou.comm.service.DataInterfaceService;
import org.cloud.vo.QueryParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/common/config/datainterface")
public class DataInterfaceController {
    @Autowired
    DataInterfaceService dataInterfaceService;


    @RequestMapping(value = "/page/list/{page}/{pageSize}", method = RequestMethod.POST)
    public PageInfo<?> selectPageList(@PathVariable("page") int pageNum, @PathVariable("pageSize") int pageSize, @RequestBody QueryParamVO queryParamVO) {

        queryParamVO.setPageNum(pageNum);
        queryParamVO.setPageSize(pageSize);
        Page<?> result = dataInterfaceService.selectPageList(queryParamVO);

        return new PageInfo<>(result);
    }

    @RequestMapping(value = "/params/page/list/{page}/{pageSize}", method = RequestMethod.POST)
    public PageInfo<?> selectDataInterfaceParamsPageList(@PathVariable("page") int pageNum, @PathVariable("pageSize") int pageSize, @RequestBody QueryParamVO queryParamVO) {

        queryParamVO.setPageNum(pageNum);
        queryParamVO.setPageSize(pageSize);
        Page<?> result = dataInterfaceService.selectDataInterfaceParamsPageList(queryParamVO);

        return new PageInfo<>(result);
    }
}
