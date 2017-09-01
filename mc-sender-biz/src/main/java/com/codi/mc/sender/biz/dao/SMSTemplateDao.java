package com.codi.mc.sender.biz.dao;

import com.codi.base.dao.BaseDAO;
import com.codi.mc.sender.api.domain.SMSTemplate;

import java.util.List;

/**
 * 亲，写个类注释呗
 * @author admin
 * @date 2017-03-02 13:34
 */
public interface SMSTemplateDao extends BaseDAO<SMSTemplate> {
    /**
     * 获取所有的模板
     * @return
     */
    List<SMSTemplate> getAll();
}
