package com.longyou.comm.conntroller;

import com.longyou.comm.mapper.TFrameUserRefMapper;
import com.longyou.comm.model.TFrameUserRef;
import com.longyou.comm.service.FrameUserRefService;
import com.longyou.comm.vo.FrameUserRefVO;
import org.cloud.constant.CoreConstant;
import org.cloud.exception.BusinessException;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userRef")
public class FrameUserRefController {
    @Autowired
    private FrameUserRefService frameUserRefService;

    @Autowired
    private TFrameUserRefMapper tFrameUserRefMapper;

    /**
     * 增加
     *
     * @param vo
     * @return
     * @throws BusinessException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addUserRef")
    public ResponseResult addUserRef(@RequestBody FrameUserRefVO vo) throws BusinessException {
        return frameUserRefService.create(vo);
    }

    /**
     * 查询
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/selectUserRef")
    public TFrameUserRef selectUserRef(Long id) {
        TFrameUserRef list = tFrameUserRefMapper.select(id);
        return list;
    }

    /**
     * 修改
     * @param vo
     * @return
     * @throws BusinessException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/updateUserRef")
    public ResponseResult updateUserRef(@RequestBody FrameUserRefVO vo) throws BusinessException{
        return frameUserRefService.update(vo);
    }

    /**
     * 删除
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deleteUserRef")
    public ResponseResult deleteUserRef(Long id) throws BusinessException{
         int count = tFrameUserRefMapper.delete(id);
         if (count > 0){
             return new ResponseResult(CoreConstant.RestStatus.SUCCESS.value(),"删除成功");
         }
        throw new BusinessException("删除失败或不存在");
    }

}
