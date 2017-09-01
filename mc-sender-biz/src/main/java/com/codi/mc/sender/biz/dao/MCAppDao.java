package com.codi.mc.sender.biz.dao;

import com.codi.base.dao.BaseDAO;
import com.codi.mc.sender.api.domain.MCApp;

import java.util.List;

/**
 * 亲，写个类注释呗
 * @author admin
 * @date 2017-03-02 13:34
 */
public interface MCAppDao extends BaseDAO<MCApp> {
    /**
     * 获取所有的应用
     * @return
     */
    List<MCApp> getAll();
}
