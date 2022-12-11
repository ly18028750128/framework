package com.unknow.first.mail.manager.service;

import com.unknow.first.mail.manager.vo.MailVO;
import com.unknow.first.mail.manager.vo.MailVO.EmailParams;
import java.io.Serializable;
import java.util.concurrent.Future;

public interface IEmailSenderService extends Serializable {

    public Future<String> sendEmail(MailVO mailVO) throws Exception;

    default Future<String> sendEmail(String templateCode, EmailParams params) throws Exception {
        return sendEmail(templateCode, params, "zh_CN");
    }

    default Future<String> sendEmail(String templateCode, EmailParams params, String language) throws Exception {
        MailVO mailVO = new MailVO();
        mailVO.setParams(params);
        return sendEmail(templateCode, mailVO, language);
    }

    public Future<String> sendEmail(String templateCode, MailVO mailVO, String language) throws Exception;
}
