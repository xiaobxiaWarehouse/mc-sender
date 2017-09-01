package com.codi.mc.sender.biz.manager;

import com.codi.mc.sender.api.domain.SMSChannel;
import com.codi.mc.sender.api.enumeration.SMSChannelEnum;
import com.codi.mc.sender.biz.manager.sms.impl.ALISMSProvider;
import com.codi.mc.sender.biz.manager.sms.impl.ChuangLanSMSProvider;
import com.codi.mc.sender.biz.manager.sms.SMSProvider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Shangdu Lin on 2017/3/2 14:13.
 */
public class SMSProviderFactory {

    private static final ConcurrentMap<Integer, SMSProvider> map = new ConcurrentHashMap<>();

    private SMSProviderFactory() {
    }

    /**
     * 获取某一渠道(非线程安全)
     *
     * @param channel
     * @return
     */
    public static SMSProvider getProvider(SMSChannel channel) {
        Integer channelId = channel.getSmsChannelId();
        if (map.containsKey(channelId)) {
            return map.get(channelId);
        } else {
            SMSProvider provider = instanceOf(channel);
            SMSProvider previous = map.putIfAbsent(channelId, provider);
            provider = previous == null ? provider : previous;

            return provider;
        }
    }

    /**
     * 生成一类型的短信渠道，如创蓝
     *
     * @param channel
     * @return
     */
    private static SMSProvider instanceOf(SMSChannel channel) {
        if (SMSChannelEnum.CHUANGLAN.value().equals(channel.getChannelCode())) {
            return ChuangLanSMSProvider.instanceOf(channel.getChannelConfig());
        }

        if (SMSChannelEnum.ALIBABA.value().equals(channel.getChannelCode())) {
            return ALISMSProvider.instanceOf(channel.getChannelConfig());
        }

        return null;
    }
}















