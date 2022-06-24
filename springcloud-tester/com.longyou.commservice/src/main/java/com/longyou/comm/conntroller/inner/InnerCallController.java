package com.longyou.comm.conntroller.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.longyou.comm.model.TFrameUserRef;
import com.longyou.comm.service.FrameUserRefService;
import com.unknow.first.mail.manager.domain.EmailTemplate;
import com.unknow.first.mail.manager.service.EmailTemplateService;
import org.cloud.annotation.SystemResource;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.vo.FrameUserRefVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 内部调用的controller
 */
@RestController
@RequestMapping("/inner")
@SystemResource(path = "/commonServiceInner")  //commonservice内部调用相关api
public class InnerCallController {

    @Autowired
    private FrameUserRefService frameUserRefService;

    /**
     * 内部调用的增加用户扩展信息的类，无权限控制，仅支持微服务间的调用
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @PostMapping("/userinfo/addUserRef")
    public Integer addUserRef(@RequestBody FrameUserRefVO vo) throws Exception {
        return frameUserRefService.create(vo);
    }

    /**
     * 内部调用的增加用户扩展信息的类，无权限控制，仅支持微服务间的调用
     *
     * @param attributeName 属性名称
     * @return
     * @throws Exception
     */
    @GetMapping("/userinfo/getCurrentUserRefByAttributeName")
    @SystemResource("内部使用的获取当前用户扩展属性")
    public FrameUserRefVO getCurrentUserRefByAttributeName(@RequestParam("name") String attributeName) throws Exception {
        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        return frameUserRefService.getUserRefByAttributeName(loginUserDetails.getId(), attributeName);
    }

    @Autowired
    EmailTemplateService emailTemplateService;

    @GetMapping("/email/getEmailTemplateByCode")
    public EmailTemplate getEmailTemplateByCode(@RequestParam("templateCode") String templateCode,
        @RequestParam(value = "language", defaultValue = "zh_CN") String language) {
        QueryWrapper<EmailTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_code", templateCode);
        queryWrapper.eq("language", language);

        return emailTemplateService.getOne(queryWrapper);
    }

}
