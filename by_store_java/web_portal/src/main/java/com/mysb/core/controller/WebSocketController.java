package com.mysb.core.controller; /**
 * Created: Administrator
 * author:xmf
 * Date:2018/7/20
 * Time:9:49
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysb.core.pojo.communication.Communication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.ContextLoader;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,value是客户端连接地址，这里定义了两个参数，和springmvc的配置指定格式的URL，
 * 映射到对应的参数是一样的
 */
@ServerEndpoint(value = "/websocket/{msg_me}")//参数格式 自定义连接标识
public class WebSocketController {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识类似于扣扣号,map是线程不安全的
    private static ConcurrentHashMap<String, Session> webSocketSet = new ConcurrentHashMap<String, Session>();

    private RedisTemplate redisTemplate;


    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据 @PathParam用来获取连接路径上的参数
     * @param param   发送者@接收者@id
     */
    @OnOpen
    public void onOpen(@PathParam(value = "msg_me") String param, Session session) {
        addOnlineCount();           //在线数加1
        if (this.redisTemplate == null) {
            this.redisTemplate = (RedisTemplate) ContextLoader.getCurrentWebApplicationContext().getBean("redisTemplate");
        }
        System.out.println("标识为----" + param + "----的用户连接成功,当前在线人数:::" + getOnlineCount());
        try {
            String newParam = null;
            String[] split = param.split("@");
            if (split[2].equals("0")) {
                //客户端传来
                newParam = split[0] + "@" + split[1];
            } else {
                newParam = split[1] + "@" + split[0];
            }
            System.out.println(newParam);
            webSocketSet.put(split[0] + "@" + split[1], session);     //加入map中
            List<Communication> listAll = (List<Communication>) redisTemplate.boundHashOps("customer_seller").get(newParam + "_All");
            if (listAll == null) {
                listAll = new ArrayList<>();
            }
            sendMessage(JSON.toJSON(listAll).toString(), session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam(value = "msg_me") String param) {
        //根据自定义的标识移除此用户的连接
        webSocketSet.remove(param);
        subOnlineCount();   //在线数减1
        System.out.println("标识为----" + param + "----的用户断开连接成功,当前在线人数:::" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息 格式发送人_接收人_消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam(value = "msg_me") String formId) {
        System.out.println("收到消息:::" + message.toString());
        //将消息装成json对象
        JSONObject msgObj = JSON.parseObject(message);
        //接收者id
        String toId = msgObj.getString("toId");
        //发送者
        String senderId = msgObj.getString("senderId");
        //得到消息内容
        String msgContent = msgObj.getString("msgContent");
        //头像
        String pic = msgObj.getString("pic");
        //判断
        String flag = msgObj.getString("flag");

        String param = null;
        for (String sc : webSocketSet.keySet()) {
            param = toId + "@" + senderId;
            String[] split = formId.split("@");
            formId = split[0] + "@" + split[1];
            if (sc.equals(formId)) {
                //获取商家与客户的会话
                Session session = webSocketSet.get(param);
                //Session session2 = webSocketSet.get(param);
                try {
                    //用map构造消息格式 懒得创建消息实体类
                    Communication communication = new Communication();
                    communication.setSendId(senderId);
                    communication.setMsgContent(msgContent);
                    communication.setToId(toId);
                    communication.setCreateTime(new Date());
                    communication.setPic(pic);
                    communication.setFlag(flag);
                    List<Communication> seller_customer = (List<Communication>) redisTemplate.boundHashOps("customer_seller").get(formId);
                    if (seller_customer == null) {
                        seller_customer = new ArrayList<>();
                    }
                    seller_customer.add(communication);

                    redisTemplate.boundHashOps("customer_seller").put(formId, seller_customer);

                    List<Communication> list = (List<Communication>) redisTemplate.boundHashOps("customer_seller").get(param);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.addAll(seller_customer);

                    Collections.sort(list, new Comparator<Communication>() {
                        @Override
                        public int compare(Communication o1, Communication o2) {
                            Date createTime = o1.getCreateTime();
                            Date time = o2.getCreateTime();
                            if (createTime.getTime() > time.getTime()) {
                                return 1;
                            } else if (createTime.getTime() == time.getTime()) {
                                return 0;
                            } else {
                                return -1;
                            }
                        }
                    });
                    redisTemplate.boundHashOps("customer_seller").put(formId + "_All", list);
                    //由于这里使用的string的消息类型所以在这里转成json，当然还有其它消息类型
                    sendMessage(JSON.toJSON(list).toString(), session);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。作用是发送消息
     *
     * @throws java.io.IOException
     * @param： message要发送的消息 session接收者的会话
     */
    public static void sendMessage(String message, Session session) throws IOException {
        session.getBasicRemote().sendText(message);

    }

    /**
     * 功能描述：用来返回当前在线的总人数
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2018/7/20 9:37
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 功能描述：在线总人数加一
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2018/7/20 9:37
     */
    public static synchronized void addOnlineCount() {
        WebSocketController.onlineCount++;
    }


    /**
     * 功能描述：在线总人数减一
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2018/7/20 9:38
     */
    public static synchronized void subOnlineCount() {
        WebSocketController.onlineCount--;
    }
}
