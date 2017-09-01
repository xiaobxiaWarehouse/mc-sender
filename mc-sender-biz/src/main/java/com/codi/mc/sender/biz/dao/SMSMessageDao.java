package com.codi.mc.sender.biz.dao;

import com.codi.base.dao.BaseDAO;
import com.codi.mc.sender.api.domain.SMSMessage;

import java.util.List;

/**
 * SMS短信
 * @author Shangdu Lin
 * @date 2017-03-02 13:34
 */
public interface SMSMessageDao extends BaseDAO<SMSMessage> {
    /**
     * 查询未发送的短信
     * @return 短信列表
     */
    List<SMSMessage> queryUnsentMessages();

    /**
     * 更新短信发送状态
     * @param message
     */
    void updateState(SMSMessage message);

    /**
     * 根据短信ID获取该短信
     * @param messageId
     * @return
     */
    SMSMessage getMessage(Long messageId);
}
