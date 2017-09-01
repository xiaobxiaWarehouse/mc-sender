package com.codi.mc.sender.biz.sms.model;

import com.codi.mc.sender.biz.utils.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * Created by Shangdu Lin on 2017/3/3 18:07.
 */
public class ALIConfig {

    private static final Logger logger = LoggerFactory.getLogger(ALIConfig.class);

    private String accessKeyId;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * 加载XML
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
            // key
            this.setAccessKeyId(XMLUtil.getXMLValue(element, ALIXMLTag.ACCESSKEYID));
            // secret
            this.setSecret(XMLUtil.getXMLValue(element, ALIXMLTag.SECRET));
        } catch (Exception ex) {
            logger.error("{}", ex);
            throw new RuntimeException(ex);
        }
    }
}
