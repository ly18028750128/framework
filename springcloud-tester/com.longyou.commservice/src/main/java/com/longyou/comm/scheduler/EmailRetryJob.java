package com.longyou.comm.scheduler;

import static com.unknow.first.mail.manager.service.IEmailSenderService.RETRY_QUEUE_KEY;

import com.unknow.first.mail.manager.util.EmailUtil;
import com.unknow.first.mail.manager.vo.MailVO;
import org.cloud.core.redis.RedisUtil;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class EmailRetryJob extends BaseQuartzJobBean {

    @Override
    protected void init() {
        this.jobName = "Email失败邮件重试";
        jobData.put("description", "Email失败邮件重试，默认10分种一次！");
        this.jobTime = "0 0/10 * * * ? *";  //10分钟刷新一次
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        retry();
    }

    private static void retry() {
        final MailVO mailVO = RedisUtil.single().listRightPop(RETRY_QUEUE_KEY);
        try {
            if (mailVO == null) {
                return;
            }
            EmailUtil.single().sendEmail(mailVO.getFrom(), mailVO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        retry();
    }
}
