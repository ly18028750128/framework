package com.unknow.first.mail.manager.service;

import com.unknow.first.mail.manager.vo.MailVO;
import com.unknow.first.mail.manager.vo.MailVO.EmailParams;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Future;
import org.cloud.utils.CollectionUtil;

public interface IEmailSenderService extends Serializable {

    Future<String> sendEmail(MailVO mailVO) throws Exception;

    default Future<String> sendEmail(String templateCode, EmailParams params) throws Exception {
        return sendEmail(templateCode, params, "zh_CN");
    }

    default Future<String> sendEmail(String templateCode, EmailParams params, String language) throws Exception {
        MailVO mailVO = new MailVO();
        mailVO.setParams(params);
        return sendEmail(templateCode, mailVO, language);
    }

    default Future<String> sendEmail(String templateCode, List<String> tos, EmailParams params, String language) throws Exception {
        MailVO mailVO = new MailVO();
        if (CollectionUtil.single().isNotEmpty(tos)) {
            mailVO.getTo().addAll(tos);
        }
        mailVO.setParams(params);
        return sendEmail(templateCode, mailVO, language);
    }

    Future<String> sendEmail(String templateCode, MailVO mailVO, String language) throws Exception;
}
