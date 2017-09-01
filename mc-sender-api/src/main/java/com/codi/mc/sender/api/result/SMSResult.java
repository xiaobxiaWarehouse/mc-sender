package com.codi.mc.sender.api.result;

import com.codi.base.domain.BaseResult;

/**
 * Created by Shangdu Lin on 2017/3/2 14:33.
 */
public class SMSResult extends BaseResult {

    private String externalRequestId;

    public String getExternalRequestId() {
        return externalRequestId;
    }

    public void setExternalRequestId(String externalRequestId) {
        this.externalRequestId = externalRequestId;
    }
}
