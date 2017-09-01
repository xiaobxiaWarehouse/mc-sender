package com.codi.mc.sender.biz.manager.sms;

import com.codi.mc.sender.api.domain.MCApp;
import com.codi.mc.sender.api.domain.SMSChannel;
import com.codi.mc.sender.api.domain.SMSMessage;
import com.codi.mc.sender.api.domain.SMSTemplate;
import com.codi.mc.sender.api.result.SMSResult;

/**
 * Created by Shangdu Lin on 2017/3/2 14:18.
 */
public interface SMSProvider {

    /**
     * 发送短信
     * @param app
     * @param channel
     * @param template
     * @param message
     * @return
     */
    SMSResult send(MCApp app, SMSChannel channel, SMSTemplate template, SMSMessage message);

}
