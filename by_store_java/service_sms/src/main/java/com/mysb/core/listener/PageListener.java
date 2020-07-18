package com.mysb.core.listener;

import com.mysb.core.service.SmsUtil;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class PageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        MapMessage map = (MapMessage) message;
        try {
            SmsUtil.sendSms(map.getString("mobile"),
                    map.getString("template_code"),
                    map.getString("sign_name"),
                    map.getString("param"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
