package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.log.PayLog;
import com.mysb.core.service.OrderService;
import com.mysb.core.service.PayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {
    /**
     * 获取当前登录用户名
     * 根据用户名获取redis中的支付日志
     * 根据支付日志对象中的支付单号和总金额
     * 调用微信接口,生成支付订单
     *
     * @param username
     * @return
     */
    @Reference
    private OrderService orderService;
    @Reference
    private PayService payService;

    @RequestMapping("/createNative")
    public Map createNative(String username) {
        PayLog payLog = orderService.getPayLogByUsername(username);
        if (payLog != null) {
            //调用生成支付链接
           // Map map = payService.createNative(payLog.getOutTradeNo(), "1");
            HashMap<String, String> map = new HashMap<>();
            map.put("out_trade_no",payLog.getOutTradeNo());//商户订单号
            map.put("total_fee","1");//总金额
            map.put("code_url","http://localhost:8084/pay/updateStatus?username="+username);//总金额
            return map;
        }
        return new HashMap();
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String username, String out_trade_no) {
        Result result = null;
        int flag = 1;
        while (true) {
            //判断支付订单等于null
            if (out_trade_no == null) {
                result = new Result(false, "二维码超时");
                break;
            }
            //调用查询接口查询支付是否成功
            Map map = payService.queryPayStatus(out_trade_no);
            if ("SUCCESS".equals(map.get("trade_state"))) {
                result = new Result(true, "支付成功!");
                //如果支付成功,支付日志表和订单表的支付状态改为已支付,redis的支付日志表对象删除
                orderService.updatePayStatus(username);
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (flag > 100) {
                result = new Result(false, "二维码超时");
                break;
            }
            flag++;
        }
        return result;
    }

    @RequestMapping("/updateStatus")
    public void updateStatus(String username){
        //如果支付成功,支付日志表和订单表的支付状态改为已支付,redis的支付日志表对象删除
        orderService.updatePayStatus(username);
    }
    @RequestMapping("/queryStatus")
    public PayLog queryStatus( String out_trade_no){
        return orderService.queryStatus(out_trade_no);
    }
}