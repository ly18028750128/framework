package com.unknow.first.mail.manager.service;

import com.unknow.first.mail.manager.domain.EmailTemplate;
import com.unknow.first.mail.manager.vo.MailVO;
import com.unknow.first.mail.manager.vo.MailVO.EmailParams;

public interface IEmailSenderService {

    public void sendEmail(MailVO mailVO) throws Exception;

    public void sendEmail(String templateCode, EmailParams params) throws Exception;

    public void sendEmail(String templateCode, EmailParams params,String language) throws Exception;
}
