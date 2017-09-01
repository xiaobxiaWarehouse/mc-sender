package com.codi.mc.sender.web.controller;

import com.codi.base.domain.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 项目基类
 *
 * @author shi.pengyan
 * @date 2016年11月8日 上午10:54:39
 */
public abstract class AbstractController extends BaseController {

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

}
