package com.codi.mc.sender.web.job.sms;

import com.codi.mc.sender.api.service.SMSMessageService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by Shangdu Lin on 2017/3/2 10:48.
 */
@DisallowConcurrentExecution
public class SMSJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(SMSJob.class);

    @Autowired
    private SMSMessageService smsMessageService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try {
            smsMessageService.sendSMS();
        }catch (Exception ex){
            logger.error("SMS Job Error:{}", ex);
            ex.printStackTrace();
        }
    }
}
