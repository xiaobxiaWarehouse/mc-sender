package com.codi.mc.sender.api.domain;

import com.codi.base.domain.BaseDomain;
import java.util.Date;

/**
 * 亲，写个类注释呗
 * @author admin
 * @date 2017-03-03 12:22
 */
public class SMSMessage extends BaseDomain {
    private Long messageId;

    private Integer templateId;

    private String mobile;

    private String content;

    private Integer sendPlan;

    private Integer sendStatus;

    private String failReason;

    private Date planTime;

    private Boolean isDeleted;

    private String localRequestid;

    private String externalRequestid;

    private String requestIp;

    private Date createDate;

    private String createUserid;

    private Date updateDate;

    private String updateUserid;

    private static final long serialVersionUID = 1L;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getSendPlan() {
        return sendPlan;
    }

    public void setSendPlan(Integer sendPlan) {
        this.sendPlan = sendPlan;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason == null ? null : failReason.trim();
    }

    public Date getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getLocalRequestid() {
        return localRequestid;
    }

    public void setLocalRequestid(String localRequestid) {
        this.localRequestid = localRequestid == null ? null : localRequestid.trim();
    }

    public String getExternalRequestid() {
        return externalRequestid;
    }

    public void setExternalRequestid(String externalRequestid) {
        this.externalRequestid = externalRequestid == null ? null : externalRequestid.trim();
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp == null ? null : requestIp.trim();
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