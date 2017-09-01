package com.codi.mc.sender.biz.dao;

import com.codi.base.dao.BaseDAO;
import com.codi.mc.sender.api.domain.SMSChannel;

import java.util.List;

/**
 * 亲，写个类注释呗
 * @author admin
 * @date 2017-03-03 12:28
 */
public interface SMSChannelDao extends BaseDAO<SMSChannel> {
    /**
     * 查询所有的渠道
     * @return
     */
    List<SMSChannel> getAll();
}
