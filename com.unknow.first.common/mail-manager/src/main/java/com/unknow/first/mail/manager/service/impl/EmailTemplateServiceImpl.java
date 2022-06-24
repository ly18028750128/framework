package com.unknow.first.mail.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unknow.first.mail.manager.domain.EmailTemplate;
import com.unknow.first.mail.manager.service.EmailTemplateService;
import com.unknow.first.mail.manager.mapper.EmailTemplateMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_email_template】的数据库操作Service实现
* @createDate 2022-06-24 10:09:46
*/
@Service
public class EmailTemplateServiceImpl extends ServiceImpl<EmailTemplateMapper, EmailTemplate>
    implements EmailTemplateService{

}




