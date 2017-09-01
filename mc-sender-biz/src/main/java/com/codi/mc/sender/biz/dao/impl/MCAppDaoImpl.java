package com.codi.mc.sender.biz.dao.impl;

import com.codi.base.dao.BaseDAOImpl;
import com.codi.mc.sender.api.domain.MCApp;
import com.codi.mc.sender.biz.dao.MCAppDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Shangdu Lin on 2017/3/3 14:27.
 */
@Repository("mcAppDao")
public class MCAppDaoImpl extends BaseDAOImpl<MCApp> implements MCAppDao {
    /**
     * 获取所有的应用
     *
     * @return
     */
    @Override
    public List<MCApp> getAll() {
        return this.findList(this.generateStatement("getAll"));
    }
}
