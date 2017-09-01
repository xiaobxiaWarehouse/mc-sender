package com.codi.mc.sender.biz.dao.impl;

import com.codi.base.dao.BaseDAOImpl;
import com.codi.mc.sender.api.domain.SMSMessage;
import com.codi.mc.sender.biz.dao.SMSMessageDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Shangdu Lin on 2017/3/2 13:44.
 */
@Repository("smsMessageDao")
public class SMSMessageDaoImpl extends BaseDAOImpl<SMSMessage> implements SMSMessageDao {
    /**
     * 查询未发送的短信
     *
     * @return 短信列表
     */
    @Override
    public List<SMSMessage> queryUnsentMessages() {
        return this.findList(this.generateStatement("queryUnsentMessages"));
    }

    /**
     * 更新短信发送状态
     *
     * @param message
     */
    @Override
    public void updateState(SMSMessage message) {
        this.update(this.generateStatement("updateState"), message);
    }

    /**
     * 根据短信ID获取该短信
     *
     * @param messageId
     * @return
     */
    @Override
    public SMSMessage getMessage(Long messageId) {
        return this.getObject(this.generateStatement("selectByPrimaryKey"),messageId);
    }
}
