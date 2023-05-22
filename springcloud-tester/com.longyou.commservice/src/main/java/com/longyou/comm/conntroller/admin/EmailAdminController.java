package com.longyou.comm.conntroller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.longyou.comm.dto.EmailSenderConfigQueryDTO;
import com.longyou.comm.dto.EmailTemplateQueryDTO;
import com.unknow.first.api.common.CommonPage;
import com.unknow.first.api.common.CommonParam;
import com.unknow.first.mail.manager.domain.EmailSenderConfig;
import com.unknow.first.mail.manager.domain.EmailTemplate;
import com.unknow.first.mail.manager.service.EmailTemplateService;
import com.unknow.first.mail.manager.service.IEmailSenderConfigService;
import com.unknow.first.mail.manager.util.EmailUtil;
import com.unknow.first.mail.manager.vo.MailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.utils.AES128Util;
import org.cloud.utils.MyBatisPlusUtil;
import org.cloud.vo.CommonApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Future;

import static com.longyou.comm.CommonMenuConst.*;


@RestController
@Api(tags = "管理员:邮件管理", value = "管理员:邮件管理")
@RequestMapping("/email/admin")
@SystemResource(path = "/email/admin", parentMenuCode = MENU_MAIL_MANGER, parentMenuName = "邮件配置管理")
@Slf4j
public class EmailAdminController {

    @Autowired
    IEmailSenderConfigService emailSenderConfigService;

    @SystemResource(value = "/config/list", description = "管理员查询邮件配置", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION, menuName = "邮件发送配置", menuCode = MENU_MAIL_SENDER_CONFIG)
    @GetMapping("/config")
    @ApiOperation("管理员查询邮件配置")
    public CommonApiResult<CommonPage<EmailSenderConfig>> listConfig(EmailSenderConfigQueryDTO emailSenderConfigQueryDTO, CommonParam pageParam) {
        QueryWrapper<EmailSenderConfig> queryWrapper = MyBatisPlusUtil.single().getPredicate(emailSenderConfigQueryDTO);
        PageHelper.startPage(pageParam.getPage(), pageParam.getLimit(), pageParam.getSorts());

        List<EmailSenderConfig> emailSenderConfigList = emailSenderConfigService.list(queryWrapper);

        for (EmailSenderConfig emailSenderConfig : emailSenderConfigList) {
            try {
                emailSenderConfig.setPassword(AES128Util.single().decrypt(emailSenderConfig.getPassword()));
            } catch (Exception e) {
                log.error("邮件密码解密失败，请检查！{}", e.getMessage());
            }
        }
        return CommonApiResult.createSuccessResult(CommonPage.restPage(emailSenderConfigList));
    }

    @SystemResource(value = "/config/create", description = "管理员增加邮件配置", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @PostMapping("/config")
    @ApiOperation("管理员增加邮件配置")
    public CommonApiResult<Boolean> createConfig(@RequestBody EmailSenderConfig emailSenderConfig) throws Exception {
        emailSenderConfig.setEmailSenderId(null);
        emailSenderConfig.setPassword(AES128Util.single().encrypt(emailSenderConfig.getPassword()));
        emailSenderConfigService.save(emailSenderConfig);
        EmailUtil.single().refreshJavaMailSender(emailSenderConfig.getUserName());
        return CommonApiResult.createSuccessResult(true);
    }

    @SystemResource(value = "/config/update", description = "管理员更新邮件配置", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @PutMapping("/config")
    @ApiOperation("管理员更新邮件配置")
    public CommonApiResult<Boolean> updateConfig(@RequestBody EmailSenderConfig emailSenderConfig) throws Exception {
        emailSenderConfig.setPassword(AES128Util.single().encrypt(emailSenderConfig.getPassword()));
        emailSenderConfig.setUserName(null);// username不能更新
        emailSenderConfigService.updateById(emailSenderConfig);
        EmailUtil.single().refreshJavaMailSender(emailSenderConfigService.getById(emailSenderConfig.getEmailSenderId()).getUserName());
        return CommonApiResult.createSuccessResult(true);
    }

    @SystemResource(value = "/config/test", description = "管理员发送测试邮件", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @GetMapping("/config/test")
    @ApiOperation("管理员发送测试邮件")
    public CommonApiResult<String> configTest(@RequestParam("userName") String userName, @RequestParam("to") List<String> to) throws Exception {

        MailVO mailVO = new MailVO();
        mailVO.setSubject("邮件发送测试！");
        mailVO.setTo(to);
        mailVO.setText(String.format("来自【%s】的测试邮件", userName));

        Future<String> future = EmailUtil.single().sendEmail(userName, mailVO);
        return CommonApiResult.createSuccessResult(future.get());
    }


    @Autowired
    EmailTemplateService emailTemplateService;

    @SystemResource(value = "/config/template", description = "管理员查询邮件模板", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION, menuName = "邮件模板配置", menuCode = MENU_MAIL_TEMPLATE_CONFIG)
    @GetMapping("/template")
    @ApiOperation("管理员查询邮件模板")
    public CommonApiResult<CommonPage<EmailTemplate>> listTemplate(EmailTemplateQueryDTO emailTemplateQueryDTO, CommonParam pageParam) {
        QueryWrapper<EmailTemplate> queryWrapper = MyBatisPlusUtil.single().getPredicate(emailTemplateQueryDTO);
        PageHelper.startPage(pageParam.getPage(), pageParam.getLimit(), pageParam.getSorts());
        return CommonApiResult.createSuccessResult(CommonPage.restPage(emailTemplateService.list(queryWrapper)));
    }

    @SystemResource(value = "/template/create", description = "管理员增加邮件模板", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @PostMapping("/template")
    @ApiOperation("管理员增加邮件模板")
    public CommonApiResult<Boolean> createTemplate(@RequestBody EmailTemplate emailTemplate) throws Exception {
        emailTemplate.setTemplateId(null);
        return CommonApiResult.createSuccessResult(emailTemplateService.save(emailTemplate));
    }

    @SystemResource(value = "/template/update", description = "管理员更新邮件模板", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @PutMapping("/template")
    @ApiOperation("管理员更新邮件模板")
    public CommonApiResult<Boolean> updateTemplate(@RequestBody EmailTemplate emailTemplate) throws Exception {
        return CommonApiResult.createSuccessResult(emailTemplateService.updateById(emailTemplate));
    }
}
