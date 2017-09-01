package com.codi.mc.sender.web.job;

import com.codi.mc.sender.api.service.QuartzService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shi.pengyan
 * @date 2016年11月15日 下午3:10:36
 */
public class JobTest extends BaseTest {

    @Autowired
    private QuartzService quartzService;

    @Test
    public void simpleJobTest() throws Exception {
        executeCodiJob("SIMPLE_JOB");
    }

    @Test
    public void smsJobTest() throws Exception {
        executeCodiJob("SMS_JOB");
    }

    @Test
    public void deleteSimpleTest() throws Exception {
        quartzService.removeJob("SIMPLE_JOB", "CODI_JOBS");
    }


    private void executeCodiJob(String jobName) throws Exception {
        executeJob(jobName, "CODI_JOBS");
    }

    private void executeJob(String jobName, String groupName) throws Exception {
        quartzService.triggerJob(jobName, groupName);
        System.in.read();
    }
}
