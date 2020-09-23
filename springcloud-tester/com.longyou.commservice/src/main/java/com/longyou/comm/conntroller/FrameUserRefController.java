package com.longyou.comm.conntroller;

import com.baomidou.mybatisplus.core.metadata.PageList;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.longyou.comm.model.TFrameUserRef;
import com.longyou.comm.service.FrameUserRefService;
import com.longyou.comm.vo.FrameUserRefVO;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.model.TFrameUser;
import org.cloud.vo.QueryParamVO;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/userRef")
public class FrameUserRefController {
    @Autowired
    private FrameUserRefService frameUserRefService;

    /**
     * 增加
     * @param vo
     * @return
     * @throws BusinessException
     */
    @SystemResource(value = "addUserRef", description = "增加", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.POST, value = "/addUserRef")
    public ResponseResult addUserRef(@RequestBody FrameUserRefVO vo) throws BusinessException {
        TFrameUserRef userRef = frameUserRefService.selectUserList(vo.getUserId());
        if (userRef == null) {
            ResponseResult tFrameUserRaf = frameUserRefService.create(vo);
            ResponseResult responseResult = ResponseResult.createSuccessResult();
            responseResult.setData(tFrameUserRaf);
            return new ResponseResult(CoreConstant.RestStatus.SUCCESS.value(),"添加成功");
        }
        return new ResponseResult(CoreConstant.RestStatus.NOAUTH.value(),"用户已存在");
    }

    /**
     * 根据当前用户增加
     * @param vo
     * @return
     * @throws BusinessException
     */
    @SystemResource(value = "userCreate", description = "根据当前用户增加", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.POST, value = "/userCreate")
    public ResponseResult userCreate(@RequestBody FrameUserRefVO vo) throws BusinessException {
        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        TFrameUserRef userRef = frameUserRefService.selectUserList(loginUserDetails.getId().toString());
        if (userRef == null) {
            ResponseResult tFrameUserRaf = frameUserRefService.userCreate(vo);
            ResponseResult responseResult = ResponseResult.createSuccessResult();
            responseResult.setData(tFrameUserRaf);
            return new ResponseResult(CoreConstant.RestStatus.SUCCESS.value(),"添加成功");
        }
        return new ResponseResult(CoreConstant.RestStatus.NOAUTH.value(),"用户已存在");
    }

    /**
     * 查询
     *
     * @return
     */
    @SystemResource(value = "selectUserRef", description = "查询", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.GET, value = "/selectUserRef")
    public PageInfo<?> selectUserRef(int pageNum, int pageSize, @RequestBody QueryParamVO queryParamVO) {
        queryParamVO.setPageNum(pageNum);
        queryParamVO.setPageSize(pageSize);
        Page<?> result = frameUserRefService.select(queryParamVO);
        return new PageInfo<>(result);
    }

    /**
     * 修改
     * @param vo
     * @return
     * @throws BusinessException
     */
    @SystemResource(value = "updateUserRef", description = "修改", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.POST, value = "/updateUserRef")
    public ResponseResult updateUserRef(@RequestBody FrameUserRefVO vo) throws BusinessException{
        ResponseResult tFrameUser = frameUserRefService.update(vo);
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(tFrameUser);
        return new ResponseResult(CoreConstant.RestStatus.SUCCESS.value(),"修改成功");
    }

    /**
     * 根据当前用户修改
     * @param vo
     * @return
     * @throws BusinessException
     */
    @SystemResource(value = "updateUser", description = "根据当前用户修改", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.POST, value = "/updateUser")
    public ResponseResult updateUser(@RequestBody FrameUserRefVO vo) throws BusinessException{
        ResponseResult tFrameUser = frameUserRefService.userUpdate(vo);
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(tFrameUser);
        return new ResponseResult(CoreConstant.RestStatus.SUCCESS.value(),"修改成功");
    }

    /**
     * 删除
     * @param id
     * @return
     * @throws BusinessException
     */
    @SystemResource(value = "deleteUserRef", description = "删除", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.GET, value = "/deleteUserRef")
    public ResponseResult deleteUserRef(Long id) throws BusinessException{
         int count = frameUserRefService.delete(id);
         if (count > 0){
             return new ResponseResult(CoreConstant.RestStatus.SUCCESS.value(),"删除成功");
         }
        throw new BusinessException("删除失败或不存在");
    }

    /**
     * 获取当前登陆用户查询
     * @return
     */
    @SystemResource(value = "selectUserRefList", description = "根据id查询用户", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.GET, value = "/selectUserRefList")
    public ResponseResult selectUserRefList(){
        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        if (loginUserDetails != null){
            TFrameUserRef tFrameUserRef = frameUserRefService.selectUserList(loginUserDetails.getId().toString());
            ResponseResult responseResult = ResponseResult.createSuccessResult();
            responseResult.setData(tFrameUserRef);
            return responseResult;
        }
        return new ResponseResult(CoreConstant.RestStatus.NOAUTH.value(),"未授权");
    }

}
