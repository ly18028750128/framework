package com.longyou.comm.conntroller.inner;

import com.longyou.comm.service.FrameUserRefService;
import org.cloud.vo.FrameUserRefVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 内部调用的controller
 */
@RestController
@RequestMapping("/inner")
public class InnerCallController {

    @Autowired
    private FrameUserRefService frameUserRefService;

    /**
     * 内部调用的增加用户扩展信息的类，无权限控制，仅支持微服务间的调用
     * @param vo
     * @return
     * @throws Exception
     */
    @PostMapping("/userinfo/addUserRef")
    public Integer addUserRef(@RequestBody FrameUserRefVO vo) throws Exception {
        return frameUserRefService.create(vo);
    }
}
