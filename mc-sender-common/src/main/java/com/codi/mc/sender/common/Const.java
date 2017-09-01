package com.codi.mc.sender.common;

/**
 * 常量
 *
 * @author shi.pengyan
 * @date 2017-01-04 17:19
 */
public interface Const {
    /**
     * 短信分布式锁名字的前缀
     */
    public final static String REDIS_KEY_SMS_LOCK ="SMS_LOCK_";
    /**
     *
     */
    public final static String REDIS_KEY_SMS_RESIS_LOCK_JOB ="SMS_RESIS_LOCK_JOB";
    /**
     * 短信分布式锁的过期时间
     */
    public final static int SMS_EXPIRED_SECONDS=60;
}
