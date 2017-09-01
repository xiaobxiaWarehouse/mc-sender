package com.codi.mc.sender.api.enumeration;

/**
 * Created by Shangdu Lin on 2017/2/17 10:08.
 */
public enum SMSTypeEnum {
    /**
     * 行业短信
     */
    INDUSTRY(1),
    /**
     * 营销短信
     */
    MARKETING(2);

    private int value;
    private SMSTypeEnum(int value){
        this.value=value;
    }

    public int value(){
        return this.value;
    }
}
