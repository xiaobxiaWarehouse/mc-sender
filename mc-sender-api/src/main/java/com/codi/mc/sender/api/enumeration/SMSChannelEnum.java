package com.codi.mc.sender.api.enumeration;

/**
 * Created by Shangdu Lin on 2017/2/20 14:48.
 */
public enum SMSChannelEnum {
    CHUANGLAN("CL"),
    ALIBABA("ALI");

    private String value;
    SMSChannelEnum(String value){
        this.value=value;
    }

    public String value(){
        return this.value;
    }
}
