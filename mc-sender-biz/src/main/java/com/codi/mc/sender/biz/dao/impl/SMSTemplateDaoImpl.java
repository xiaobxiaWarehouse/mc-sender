package com.codi.mc.sender.biz.dao.impl;

import com.codi.base.dao.BaseDAOImpl;
import com.codi.mc.sender.api.domain.SMSTemplate;
import com.codi.mc.sender.biz.dao.SMSTemplateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Shangdu Lin on 2017/3/3 14:34.
 */
@Repository("smsTemplateDao")
public class SMSTemplateDaoImpl extends BaseDAOImpl<SMSTemplate> implements SMSTemplateDao {
    /**
     * 获取所有的模板
     *
     * @return
     */
    @Override
    public List<SMSTemplate> getAll() {
        return this.findList(this.generateStatement("getAll"));
    }
}
