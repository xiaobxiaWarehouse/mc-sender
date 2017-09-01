package com.codi.mc.sender.biz.manager.sms.impl;

import com.codi.mc.sender.api.domain.MCApp;
import com.codi.mc.sender.api.domain.SMSChannel;
import com.codi.mc.sender.api.domain.SMSMessage;
import com.codi.mc.sender.api.domain.SMSTemplate;
import com.codi.mc.sender.api.enumeration.SMSTypeEnum;
import com.codi.mc.sender.api.result.SMSResult;
import com.codi.mc.sender.biz.manager.sms.SMSProvider;
import com.codi.mc.sender.biz.sms.model.ChuangLanConfig;
import com.codi.mc.sender.biz.sms.model.ChuangLanXMLTag;
import com.codi.mc.sender.biz.utils.SMSUtil;
import com.codi.mc.sender.common.ErrorConst;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;


/**
 * Created by Shangdu Lin on 2017/3/2 14:25.
 */
public class ChuangLanSMSProvider implements SMSProvider {

    private static final Logger logger = LoggerFactory.getLogger(ChuangLanSMSProvider.class);

    private ChuangLanConfig config = new ChuangLanConfig();

    private ChuangLanSMSProvider(String xml) {
        config.loadFromXML(xml);
    }

    public static SMSProvider instanceOf(String configXML) {
        return new ChuangLanSMSProvider(configXML);
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

        SMSResult result = new SMSResult();

        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod();

        try {
            method = getMethod(template, message);
            int status = client.executeMethod(method);
            if (status == HttpStatus.SC_OK) {
                /**
                 * 结果： 20110725160412,0 1234567890100
                 */
                InputStream inputStream = method.getResponseBodyAsStream();
                String response = getResponse(inputStream);
                String[] lines = response.split("\n");
                String[] items = lines[0].split(",");
                String resultCode = items[1];
                if (resultCode.equals("0")) {
                    result.setSuccess(true);
                    result.setExternalRequestId(items[0]);
                } else {
                    result.setSuccess(false);
                    result.setErrorCode(ErrorConst.MC_CL_SEND_ERROR);
                    if (config.getErrorMsgMap().containsKey(resultCode)) {
                        result.setErrorMessage(config.getErrorMsgMap().get(resultCode));
                    }
                }

            } else {
                logger.error("创蓝HTTP请求状态异常，HTTP ERROR Status: {}:{}", method.getStatusCode(), method.getStatusText());
                result.setSuccess(false);
                result.setErrorCode(ErrorConst.MC_HTTP_STATUS_INVALID);
            }
        } catch (Exception ex) {
            logger.error("创蓝异常：{0}", ex);
            result.setSuccess(false);
            result.setErrorCode(ErrorConst.MC_HTTP_REQUEST_EXCEPTION);
        } finally {
            method.releaseConnection();
        }

        return result;
    }


    /**
     * 拼接Get请求
     *
     * @param template
     * @param message
     * @return
     * @throws URIException
     */
    private GetMethod getMethod(SMSTemplate template, SMSMessage message) throws URIException {

        GetMethod method = new GetMethod();

        String smsAccount = config.getIndustryAccount();
        String smsPassword = config.getIndustryAccountPassword();
        String smsUrl = config.getIndustryApiUrl();
        if (template.getSmsType().equals(SMSTypeEnum.MARKETING.value())) {
            smsAccount = config.getMarketingAccount();
            smsPassword = config.getMarketingAccountPassword();
            smsUrl = config.getMarketingApiUrl();
        }

        URI uri = new URI(smsUrl, false);
        NameValuePair[] pairs = new NameValuePair[6];
        pairs[0] = new NameValuePair(ChuangLanXMLTag.CL_ACCOUNT, smsAccount);
        pairs[1] = new NameValuePair(ChuangLanXMLTag.CL_PSWD, smsPassword);
        pairs[2] = new NameValuePair(ChuangLanXMLTag.CL_MOBILE, message.getMobile());
        pairs[3] = new NameValuePair(ChuangLanXMLTag.CL_NEEDSTATUS, String.valueOf(true));
        pairs[4] = new NameValuePair(ChuangLanXMLTag.CL_MSG, SMSUtil.buildContent(template.getSmsSignName(), template.getTemplateContent(), message.getContent()));
        pairs[5] = new NameValuePair(ChuangLanXMLTag.CL_EXTNO, "");

        method.setURI(uri);
        method.setQueryString(pairs);

        return method;
    }


    /**
     * 获取返回结果
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private String getResponse(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        String response = URLDecoder.decode(outputStream.toString(), "UTF-8");

        return response;
    }
}
