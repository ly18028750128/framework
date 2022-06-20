package com.unknow.first.mail.manager.service;

import com.unknow.first.mail.manager.vo.MailVO;

public interface IEmailSenderService {

    public void sendEmail(MailVO mailVO) throws Exception;

    public void sendEmail(MailVO mailVO, Integer templateId) throws Exception;
}
