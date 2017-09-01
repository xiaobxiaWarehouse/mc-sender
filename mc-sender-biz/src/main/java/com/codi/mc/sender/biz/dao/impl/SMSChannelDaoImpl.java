package com.codi.mc.sender.biz.dao.impl;

import com.codi.base.dao.BaseDAOImpl;
import com.codi.mc.sender.api.domain.SMSChannel;
import com.codi.mc.sender.biz.dao.SMSChannelDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Shangdu Lin on 2017/3/3 14:32.
 */
@Repository("smsChannelDao")
public class SMSChannelDaoImpl extends BaseDAOImpl<SMSChannel> implements SMSChannelDao {
    /**
     * 查询所有的渠道
     *
     * @return
     */
    @Override
    public List<SMSChannel> getAll() {
        return this.findList(this.generateStatement("getAll"));
    }
}
