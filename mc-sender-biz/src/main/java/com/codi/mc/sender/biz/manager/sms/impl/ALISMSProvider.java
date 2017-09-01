package com.codi.mc.sender.biz.manager.sms.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.codi.mc.sender.api.domain.MCApp;
import com.codi.mc.sender.api.domain.SMSChannel;
import com.codi.mc.sender.api.domain.SMSMessage;
import com.codi.mc.sender.api.domain.SMSTemplate;
import com.codi.mc.sender.api.result.SMSResult;
import com.codi.mc.sender.biz.manager.sms.SMSProvider;
import com.codi.mc.sender.biz.sms.model.ALIConfig;
import com.codi.mc.sender.common.ErrorConst;

/**
 * Created by Shangdu Lin on 2017/3/3 17:48.
 */
public class ALISMSProvider implements SMSProvider {

    private ALIConfig config=new ALIConfig();

    private ALISMSProvider(String xml){
        config.loadFromXML(xml);
    }

    public static SMSProvider instanceOf(String configXML){
        return new ALISMSProvider(configXML);
    }

    /**
     * 发送短信
     *
     * @param app
     * @param channel
     * @param template
     * @param message
     * @return
     */
    @Override
    public SMSResult send(MCApp app, SMSChannel channel, SMSTemplate template, SMSMessage message) {
        try {
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", config.getAccessKeyId(),
                config.getSecret());
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms", "sms.aliyuncs.com");
            IAcsClient client = new DefaultAcsClient(profile);
            SingleSendSmsRequest request = new SingleSendSmsRequest();

            // 控制台创建的签名名称
            request.setSignName(template.getSmsSignName());
            // 控制台创建的模板CODE
            request.setTemplateCode(template.getExternalTemplateCode());
            // 要替换的参数值 - {"code":"123456"}
            request.setParamString(message.getContent());
            // 接收号码
            request.setRecNum(message.getMobile());
            SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
            String requestId = httpResponse.getRequestId();

            SMSResult result=new SMSResult();
            result.setSuccess(true);
            result.setExternalRequestId(requestId);

            return result;

        }catch (Exception ex){
            SMSResult result=new SMSResult();
            result.setSuccess(false);
            result.setErrorCode(ErrorConst.MC_HTTP_REQUEST_EXCEPTION);
            result.setErrorMessage(ex.getMessage());

            return result;
        }
    }
}
