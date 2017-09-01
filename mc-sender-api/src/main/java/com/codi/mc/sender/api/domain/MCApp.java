package com.codi.mc.sender.api.domain;

import com.codi.base.domain.BaseDomain;
import java.util.Date;

/**
 * 亲，写个类注释呗
 * @author admin
 * @date 2017-03-03 12:22
 */
public class MCApp extends BaseDomain {
    private String appCode;

    private String appName;

    private Integer appState;

    private Integer smsChannelId;

    private Boolean isDeleted;

    private Date createDate;

    private String createUserid;

    private Date updateDate;

    private String updateUserid;

    private static final long serialVersionUID = 1L;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode == null ? null : appCode.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public Integer getAppState() {
        return appState;
    }

    public void setAppState(Integer appState) {
        this.appState = appState;
    }

    public Integer getSmsChannelId() {
        return smsChannelId;
    }

    public void setSmsChannelId(Integer smsChannelId) {
        this.smsChannelId = smsChannelId;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid == null ? null : createUserid.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserid() {
        return updateUserid;
    }

    public void setUpdateUserid(String updateUserid) {
        this.updateUserid = updateUserid == null ? null : updateUserid.trim();
    }
}