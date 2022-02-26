package com.example.demo.ws;

import com.example.demo.bean.Message;
import com.example.demo.utils.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat",configurator = GetHttpSessionConfigurator.class)
public class ChatEndPoint {
    private static Map<String,ChatEndPoint> map=new ConcurrentHashMap<>();
    private Session session;
    private HttpSession httpSession;
    @OnOpen     //连接建立时
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        this.session=session;
        this.httpSession=(HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String username=(String)httpSession.getAttribute("username");
        map.put(username,this);
        String message= MessageUtils.getMessage(true,null,getNames());
        broadcast(message);
    }
    @OnMessage  //接收客户端数据被调用
    public void onMessage(String message,Session session) throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        Message message1=mapper.readValue(message, Message.class);
        String toName=message1.getToName();
        String data=message1.getMessage();
        String result=MessageUtils.getMessage(false,(String)httpSession.getAttribute("username"),data);
        map.get(toName).session.getBasicRemote().sendText(result);
    }
    @OnClose    //连接关闭时
    public void onClose(String message,Session session) throws IOException {
        String username=(String)httpSession.getAttribute("username");
        if(username!=null){
            map.remove(username);
            String message2=MessageUtils.getMessage(true,null,getNames());
            broadcast(message2);
            httpSession.removeAttribute("username");
        }
    }
    public Set<String> getNames(){
        return map.keySet();
    }
    public void broadcast(String message) throws IOException {
        Set<String> names=map.keySet();
        for(String name:names){
            ChatEndPoint chatEndPoint=map.get(name);
            chatEndPoint.session.getBasicRemote().sendText(message);
        }
    }
}
