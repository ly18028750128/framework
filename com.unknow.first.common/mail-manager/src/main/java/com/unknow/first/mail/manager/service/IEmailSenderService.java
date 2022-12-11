package com.unknow.first.mail.manager.service;

import com.unknow.first.mail.manager.vo.MailVO;
import com.unknow.first.mail.manager.vo.MailVO.EmailParams;
import java.util.concurrent.Future;

public interface IEmailSenderService {

    public Future<String> sendEmail(MailVO mailVO) throws Exception;

    public void sendEmail(String templateCode, EmailParams params) throws Exception;

    public void sendEmail(String templateCode, EmailParams params,String language) throws Exception;
}
