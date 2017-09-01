package com.codi.mc.sender.biz.service.impl;

import com.codi.base.cache.RedisConnectionFactory;
import com.codi.base.i18n.I18NMgr;
import com.codi.base.util.DateUtils;
import com.codi.base.util.StringUtil;
import com.codi.mc.sender.api.domain.MCApp;
import com.codi.mc.sender.api.domain.SMSChannel;
import com.codi.mc.sender.api.domain.SMSMessage;
import com.codi.mc.sender.api.domain.SMSTemplate;
import com.codi.mc.sender.api.enumeration.SendStatusEnum;
import com.codi.mc.sender.api.result.SMSResult;
import com.codi.mc.sender.api.service.SMSMessageService;
import com.codi.mc.sender.biz.dao.MCAppDao;
import com.codi.mc.sender.biz.dao.SMSChannelDao;
import com.codi.mc.sender.biz.dao.SMSMessageDao;
import com.codi.mc.sender.biz.dao.SMSTemplateDao;
import com.codi.mc.sender.biz.manager.SMSProviderFactory;
import com.codi.mc.sender.biz.manager.sms.SMSProvider;
import com.codi.mc.sender.biz.utils.SMSUtil;
import com.codi.mc.sender.common.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shangdu Lin on 2017/3/2 13:41.
 */
@Service("smsMessageService")
public class SMSMessageServiceImpl implements SMSMessageService {

    private static final Logger logger = LoggerFactory.getLogger(SMSMessageServiceImpl.class);

    // jedis不能定义成类的局部变量，否则也会产生[B cannot be cast to java.lang.Long 异常
    //private Jedis jedis;

    @Autowired
    private SMSMessageDao smsMessageDao;
    @Autowired
    private SMSChannelDao smsChannelDao;
    @Autowired
    private SMSTemplateDao smsTemplateDao;
    @Autowired
    private MCAppDao mcAppDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 所有的应用
     */
    private List<MCApp> apps = new ArrayList<>();
    /**
     * 所有的渠道
     */
    private List<SMSChannel> channels = new ArrayList<>();
    /**
     * 所有的模板
     */
    private List<SMSTemplate> templates = new ArrayList<>();

    @PostConstruct
    public void initData() {
        apps = mcAppDao.getAll();
        channels = smsChannelDao.getAll();
        templates = smsTemplateDao.getAll();
        /*redis 多线程调用时抛出 [B cannot be cast to java.lang.Long 异常
        原因：多个线程同时调用了同一个jedis对象，导致内存数据被多个线程竞争，产生数据混乱
        解决方案：每个线程都new出一个自己的jedis对象。*/
        /*jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();*/
    }

    /**
     * 单进程单线程发送短信
     * <p>
     * 遍历短信表，找出所有未发送的短信，通过短信渠道发送短信，此方法遇到不合法的短信将被忽略，继续发送其他短信，但会写入错误日志
     * 先从短信模板中获取短信渠道,如果短信模板中未配置短信渠道，则从应用配置中获取短信渠道
     */
    @Override
    public void sendSMS() {
        //查询所有未发送的短信
        List<SMSMessage> unSentMessages = smsMessageDao.queryUnsentMessages();
        if (CollectionUtils.isEmpty(unSentMessages)) {
            return;
        }

        //遍历发送短信
        for (SMSMessage message :
            unSentMessages) {
            //发送
            send(message);
        }
    }

    public void dbLockSendSMS(){

    }

    /**
     * 分布式并发发送短信
     * <p>
     * 此方法实现的redis分布式锁的缺陷在锁可能会没及时被删除或不会被删除或过期被删除但错误，可能引起的后果:
     * 1. send(message)调用成功但发送失败，当调用jedis.del(lockKey)服务器宕机，导致删除失败，且锁还未过期， 如果此时通过更新数据库此短信记录状态为未发送，以此来重发短信，
     * 会发现在同一个messageId的短信记录不能被及时重复发送，锁过期后才会被发送，所以用此方法时当想要立即重复发送一条短信时，为避免发送没有及时发送出去的情况，需要重新插入一条一样的短信记录
     * <p>
     * 2. send(message)调用成功但发送失败，当调用jedis.del(lockKey)服务器宕机，导致删除失败，此锁会被一直保存在redis，
     * 因为发送失败的短信不会被取出来处理，所有就算锁过期也不会被删除，但如果不重发此短信，此种情况对业务没影响
     * <p>
     * 3. send(message)执行期间服务器挂了，此时短信还未被发送出去，锁未被删除且未过期，导致短信未被及时发送出去，只有锁过期后才会被发送出去
     * <p>
     * 4. 当过了过期时间（现在是1分钟），但短信还没发送出去，锁可能会被其他线程删除，会导致短信会被重复发送，1分钟的过期时间理论上不会出现此问题
     *
     * 为什么要删除锁呢？是否可以每次发完短信不删除锁？
     * 不删除锁方案的缺陷：
     * 1. 当获取锁成功，但send(message)调用失败（服务器升级/宕机），短信未发送出去，还是属于未发送状态，则此短信永远不会被发送
     * 2. 同一短信不能重复发送，必须新建一条短信来发送
     */
    @Override
    public void redisLockSendSMS() {
        //查询所有未发送的短信
        List<SMSMessage> unSentMessages = smsMessageDao.queryUnsentMessages();
        if (CollectionUtils.isEmpty(unSentMessages)) {
            return;
        }

        // Make sure to use pooling or giving each thread its own client if using jedis from multiple threads.
        RedisConnectionFactory factory=(RedisConnectionFactory)redisTemplate.getConnectionFactory();
        // jedis不能定义成类的局部变量，否则也会产生[B cannot be cast to java.lang.Long 异常
        Jedis jedis=factory.fetchJedisConnector();

        //遍历发送短信
        for (SMSMessage message :
            unSentMessages) {

            // 锁的名称
            String lockKey = Const.REDIS_KEY_SMS_LOCK + String.valueOf(message.getMessageId());
            //时间统一问题:各个客户端加锁时需要获取时间，而这个时间都不应当从本地获取，
            //因为各个客户端的时间可能并不是一致的，因此需要提供一个TimeServer提供获取时间的服务
            Date now = new Date();
            //设置超时时间，60秒超时
            String expiredTime = DateUtils.formatDateTime(DateUtils.addSecond(now, Const.SMS_EXPIRED_SECONDS));
            /**
             * setnx:
             * 设置成功，返回 1
             * 设置失败，返回 0
             */
            //原子性操作,若给定的 key 已经存在，则SETNX不做任何动作，且返回0
            if (jedis.setnx(lockKey, expiredTime) == 1) {
                logger.warn("Thread={} MessageId={}, LockKey={} Get the key", Thread.currentThread().getId(), message.getMessageId(), lockKey);
                //获取到锁后需要再次判断下该短信是否已发送，因为有可能此条短信已被另外的线程发送成功且锁已被删除
                SMSMessage updatedMessage = smsMessageDao.getMessage(message.getMessageId());
                if (updatedMessage.getSendStatus().equals(SendStatusEnum.UNSEND.value())) {
                    /**
                     * 发送短信
                     */
                    send(message);
                    logger.warn("Thread={} MessageId={}, LockKey={} send the message", Thread.currentThread().getId(), message.getMessageId(), lockKey);
                } else {
                    logger.warn("Thread={} MessageId={}, LockKey={} The message had been sent already", Thread.currentThread().getId(),
                        message.getMessageId(), lockKey);
                }
                //删除锁，如果这步删除锁失败，短信不会重复发送，因为短信状态已变成已发送状态，该记录不会被取出来
                jedis.del(lockKey);
                logger.warn("Thread={} MessageId={}, LockKey={} delete the key", Thread.currentThread().getId(), message.getMessageId(), lockKey);
            } else {
                logger.warn("Thread={} MessageId={}, LockKey={} Have not gotten the key", Thread.currentThread().getId(), message.getMessageId(), lockKey);
                //判断锁是否已超时，如果已超时则删除锁，用于防止死锁
                String previousDate = jedis.get(lockKey);
                // previousDate可能为空，因为被另一线程处理完后删除
                if (previousDate != null && SMSUtil.isTimeExpired(previousDate, now)) {
                    logger.warn("Thread={} MessageId={}, LockKey={}, PreviousDate={}, The key has expired", Thread.currentThread().getId(),
                        message.getMessageId(), lockKey, previousDate);
                    //锁已过期
                    // 假设多个线程(非单jvm)同时走到这里，getSet是原子性操作
                    previousDate = jedis.getSet(lockKey, expiredTime);
                    // 如果previousDate依然是过期的，说明当前线程已拿到锁，因为如果previousDate没过期，说明在当前线程之前previousDate被其他线程拿到锁更新过
                    if (previousDate != null && SMSUtil.isTimeExpired(previousDate, now)) {
                        //成功获取到锁，删除该锁，该短信会被下一循环处理掉
                        jedis.del(lockKey);
                        logger.warn("Thread={} MessageId={}, LockKey={}, PreviousDate={}, The expired key has been deleted", Thread.currentThread().getId(),
                            message.getMessageId(), lockKey, previousDate);
                    }
                }
            }

        }
    }

    /**
     * 分布式并发发送短信，不删除锁
     * 缺陷：
     * 1. 当jedis.setnx获取锁成功，但send(message)调用失败（服务器升级/宕机），短信未发送出去，还是属于未发送状态，则此短信永远不会被发送
     * 2. 同一短信不能重复发送，必须新建一条短信来发送
     */
    @Override
    public void redisLockNoDeleteSendSMS() {
        //查询所有未发送的短信
        List<SMSMessage> unSentMessages = smsMessageDao.queryUnsentMessages();
        if (CollectionUtils.isEmpty(unSentMessages)) {
            return;
        }

        // Make sure to use pooling or giving each thread its own client if using jedis from multiple threads.
        RedisConnectionFactory factory=(RedisConnectionFactory)redisTemplate.getConnectionFactory();
        // jedis不能定义成类的局部变量，否则也会产生[B cannot be cast to java.lang.Long 异常
        Jedis jedis=factory.fetchJedisConnector();

        //遍历发送短信
        for (SMSMessage message :
            unSentMessages) {

            // 锁的名称
            String lockKey = Const.REDIS_KEY_SMS_LOCK + String.valueOf(message.getMessageId());
            //时间统一问题:各个客户端加锁时需要获取时间，而这个时间都不应当从本地获取，
            //因为各个客户端的时间可能并不是一致的，因此需要提供一个TimeServer提供获取时间的服务
            Date now = new Date();
            //设置超时时间，60秒超时
            String expiredTime = DateUtils.formatDateTime(DateUtils.addSecond(now, Const.SMS_EXPIRED_SECONDS));
            /**
             * setnx:
             * 设置成功，返回 1
             * 设置失败，返回 0
             */
            //原子性操作,若给定的 key 已经存在，则SETNX不做任何动作，且返回0
            //拿到锁的线程发送短信
            if (jedis.setnx(lockKey, expiredTime) == 1) {
                logger.warn("Thread={} MessageId={}, LockKey={} Get the key", Thread.currentThread().getId(), message.getMessageId(), lockKey);
                //获取到锁后需要再次判断下该短信是否已发送，因为有可能此条短信已被另外的线程发送成功且锁已被删除
                SMSMessage updatedMessage = smsMessageDao.getMessage(message.getMessageId());
                if (updatedMessage.getSendStatus().equals(SendStatusEnum.UNSEND.value())) {
                    /**
                     * 发送短信
                     */
                    send(message);
                    logger.warn("Thread={} MessageId={}, LockKey={} send the message", Thread.currentThread().getId(), message.getMessageId(), lockKey);
                } else {
                    logger.warn("Thread={} MessageId={}, LockKey={} The message had been sent already", Thread.currentThread().getId(),
                        message.getMessageId(), lockKey);
                }
            }
        }
    }

    /**
     * 发送一条短信
     *
     * @param message
     */
    private void send(SMSMessage message) {
        try {
            //获取模板基本信息
            SMSTemplate template = getTemplate(message.getTemplateId());
            if (template == null) {
                logger.error("{}-未找到该短信模板：{}", message.getTemplateId(), message.toString());
                return;
            }
            //获取APP
            MCApp app = getApp(template.getAppCode());
            if (app == null) {
                logger.error("{}-未找到该应用:{}", template.getAppCode(), message.toString());
                return;
            }
            //先从短信模板中获取短信渠道
            SMSChannel channel = getChannel(template.getSmsChannelId());
            if (channel == null) {
                //如果短信模板中未配置短信渠道，则从应用配置中获取短信渠道
                channel = getChannel(app.getSmsChannelId());
                if (channel == null) {
                    logger.error("{}-未找到该短信渠道:{}", app.getSmsChannelId(), message.toString());
                    return;
                }
            }

            /**
             * 通过短信渠道发送短信
             */
            SMSProvider provider = SMSProviderFactory.getProvider(channel);
            if (provider == null) {
                logger.error("未找到该短信渠道:{}", channel.toString());
                return;
            }
            SMSResult result = provider.send(app, channel, template, message);
            /**
             * 更新短信发送状态
             */
            message.setExternalRequestid(result.getExternalRequestId());
            if (result.getSuccess()) {
                message.setSendStatus(SendStatusEnum.SENT.value());
                message.setFailReason("");
            } else {
                message.setSendStatus(SendStatusEnum.FAIL.value());
                String errorMessage = result.getErrorMessage();
                if (StringUtil.isEmpty(errorMessage)) {
                    errorMessage = I18NMgr.getInstance().getValue(result.getErrorCode());
                }
                message.setFailReason(errorMessage);

                logger.error("短信发送失败:{}", errorMessage);
            }
            //更新
            smsMessageDao.updateState(message);
        } catch (Exception ex) {
            logger.error("send-短信发送失败:{}", ex);
        }
    }

    /**
     * 根据渠道ID获取渠道对象
     *
     * @param channelId
     * @return
     */
    private SMSChannel getChannel(Integer channelId) {
        for (SMSChannel channel :
            channels) {
            if (channel.getSmsChannelId().equals(channelId)) {
                return channel;
            }
        }

        return null;
    }

    /**
     * 根据模板ID获取模板对象
     *
     * @param templateId
     * @return
     */
    private SMSTemplate getTemplate(Integer templateId) {
        for (SMSTemplate template :
            templates) {
            if (template.getTemplateId().equals(templateId)) {
                return template;
            }
        }

        return null;
    }

    /**
     * 获取APP对象
     *
     * @param appCode
     * @return
     */
    private MCApp getApp(String appCode) {
        for (MCApp app :
            apps) {
            if (app.getAppCode().equals(appCode)) {
                return app;
            }
        }

        return null;
    }
}
