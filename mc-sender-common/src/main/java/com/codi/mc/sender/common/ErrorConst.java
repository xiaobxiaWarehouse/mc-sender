package com.codi.mc.sender.common;

/**
 * 错误常量
 *
 * @author shi.pengyan
 * @date 2017-01-04 17:19
 */
public interface ErrorConst {
    /**
     * XML加载出错
     */
    String MC_XML_LOAD_EXCEPTION="MC_XML_LOAD_EXCEPTION";
    /**
     * Http状态非法
     */
    String MC_HTTP_STATUS_INVALID="MC_HTTP_STATUS_INVALID";
    /**
     * Http请求异常
     */
    String MC_HTTP_REQUEST_EXCEPTION="MC_HTTP_REQUEST_EXCEPTION";
    /**
     * 创蓝发送错误
     */
    String MC_CL_SEND_ERROR="MC_CL_SEND_ERROR";
}
