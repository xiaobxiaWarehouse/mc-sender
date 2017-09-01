package com.codi.mc.sender.biz.sms.model;

import com.codi.mc.sender.biz.utils.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shangdu Lin on 2017/3/2 17:08.
 */
public class ChuangLanConfig {

    private static final Logger logger = LoggerFactory.getLogger(ChuangLanConfig.class);


    /**
     * 错误代码与错误消息的字典表
     */
    private Map<String, String> errorMsgMap;

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    /**
     *  行业短信
     */
    /**
     * 行业短信服务地址
     */
    private String industryApiUrl;

    public String getIndustryApiUrl() {
        return industryApiUrl;
    }

    public void setIndustryApiUrl(String industryApiUrl) {
        this.industryApiUrl = industryApiUrl;
    }

    /**
     * 行业短信账户
     */
    private String industryAccount;

    public String getIndustryAccount() {
        return industryAccount;
    }

    public void setIndustryAccount(String industryAccount) {
        this.industryAccount = industryAccount;
    }


    /**
     * 行业短信账户密码
     */
    private String industryAccountPassword;

    public String getIndustryAccountPassword() {
        return industryAccountPassword;
    }

    public void setIndustryAccountPassword(String industryAccountPassword) {
        this.industryAccountPassword = industryAccountPassword;
    }

    /**
     * 营销短信
     */
    /**
     * 营销短信账户
     */
    private String marketingAccount;

    public String getMarketingAccount() {
        return marketingAccount;
    }

    public void setMarketingAccount(String marketingAccount) {
        this.marketingAccount = marketingAccount;
    }

    /**
     * 营销短信账户密码
     */
    private String marketingAccountPassword;

    public String getMarketingAccountPassword() {
        return marketingAccountPassword;
    }

    public void setMarketingAccountPassword(String marketingAccountPassword) {
        this.marketingAccountPassword = marketingAccountPassword;
    }

    /**
     * 营销短信服务地址
     */
    private String marketingApiUrl;

    public String getMarketingApiUrl() {
        return marketingApiUrl;
    }

    public void setMarketingApiUrl(String marketingApiUrl) {
        this.marketingApiUrl = marketingApiUrl;
    }

    /**
     * 根据XML字符串初始化创蓝短信配置对象
     *
     * @param xml
     */
    public void loadFromXML(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            StringReader reader = new StringReader(xml);
            InputSource source = new InputSource(reader);
            Document document = builder.parse(source);
            Element element = document.getDocumentElement();
            //行业短信服务地址
            this.setIndustryApiUrl(XMLUtil.getXMLValue(element, ChuangLanXMLTag.INDUSTRY_API_URL));
            // 行业短信账户
            this.setIndustryAccount(XMLUtil.getXMLValue(element, ChuangLanXMLTag.INDUSTRY_ACCOUNT));
            // 行业短信账户密码
            this.setIndustryAccountPassword(XMLUtil.getXMLValue(element, ChuangLanXMLTag.INDUSTRY_ACCOUNT_PASSWORD));
            //营销短信服务地址
            this.setMarketingApiUrl(XMLUtil.getXMLValue(element, ChuangLanXMLTag.MARKETING_API_URL));
            // 营销短信账户
            this.setMarketingAccount(XMLUtil.getXMLValue(element, ChuangLanXMLTag.MARKETING_ACCOUNT));
            // 营销短信账户密码
            this.setMarketingAccountPassword(XMLUtil.getXMLValue(element, ChuangLanXMLTag.MARKETING_ACCOUNT_PASSWORD));

            // 读取并初始化错误标识的错误消息列表
            this.errorMsgMap = new HashMap<>();
            NodeList errorElems = element.getElementsByTagName(ChuangLanXMLTag.ERROR);
            for (int i = 0; i < errorElems.getLength(); i++) {
                Element errorElem = (Element) errorElems.item(i);
                String errorCode = errorElem.getAttribute(ChuangLanXMLTag.KEY);
                String errorMessage = errorElem.getAttribute(ChuangLanXMLTag.VALUE);
                this.errorMsgMap.put(errorCode, errorMessage);
            }
        } catch (Exception ex) {
            logger.error("{}", ex);
            throw new RuntimeException(ex);
        }
    }


}
