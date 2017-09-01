package com.codi.mc.sender.biz.utils;

import org.w3c.dom.Element;

/**
 * Created by Shangdu Lin on 2017/3/3 18:19.
 */
public class XMLUtil {

    /**
     * 获取XML节点值
     *
     * @param element
     * @param tagName
     * @return
     */
    public static String getXMLValue(Element element, String tagName) {
        return element.getElementsByTagName(tagName).getLength() > 0
            ? element.getElementsByTagName(tagName).item(0).getTextContent() : "";
    }
}
