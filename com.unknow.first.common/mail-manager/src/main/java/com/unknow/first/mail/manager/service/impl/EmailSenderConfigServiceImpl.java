package com.unknow.first.mail.manager.service.impl;

import com.unknow.first.mail.manager.domain.EmailSenderConfig;
import com.unknow.first.mail.manager.mapper.EmailSenderConfigMapper;
import com.unknow.first.mail.manager.service.IEmailSenderConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2022-12-10
 */
@Service
public class EmailSenderConfigServiceImpl extends ServiceImpl<EmailSenderConfigMapper, EmailSenderConfig> implements IEmailSenderConfigService {

}
