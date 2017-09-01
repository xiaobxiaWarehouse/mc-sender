package com.codi.mc.sender.api.enumeration;

/**
 * Created by Shangdu Lin on 2017/2/20 14:38.
 */
public enum SendStatusEnum {
    /**
     * 未发送
     */
    UNSEND(0),
    /**
     * 已发送
     */
    SENT(1),
    /**
     * 发送失败
     */
    FAIL(2);

    private int value;
    SendStatusEnum(int value){
       this.value=value;
    }

    public int value(){
        return this.value;
    }

}
