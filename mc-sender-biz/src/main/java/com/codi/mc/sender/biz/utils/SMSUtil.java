package com.codi.mc.sender.biz.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codi.base.util.DateUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Shangdu Lin on 2017/3/7 10:59.
 */
public class SMSUtil {

    /**
     * 根据模板内容拼接成短信内容
     *
     * @param template
     * @param json
     * @return
     */
    public static String buildContent(String signName, String template, String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        for (Map.Entry<String, Object> entry :
            jsonObject.entrySet()) {
            String tag = "${" + entry.getKey() + "}";
            template = template.replace(tag, entry.getValue().toString());
        }

        String content= MessageFormat.format("【{0}】{1}", signName,template);

        return content;
    }

    /**
     * 判断时间是否过期
     * @param time
     * @param now
     * @return
     */
    public static boolean isTimeExpired(String time, Date now){
        Date previousDate= DateUtils.stringToDateWithTime(time);
        if(previousDate==null){
            return true;
        }
        if(now.after(previousDate)){
            return true;
        }else {
            return false;
        }
    }
}
