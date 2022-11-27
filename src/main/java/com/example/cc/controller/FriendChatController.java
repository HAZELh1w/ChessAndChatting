package com.example.cc.controller;

import com.alibaba.fastjson2.JSON;
import com.example.cc.entity.Message;
import com.example.cc.mapper.OfflineMsgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/27 18:15
 */
@ServerEndpoint("/chat/{uId}")
@RestController
public class FriendChatController {
    @Autowired
    private OfflineMsgMapper offlineMsgMapper;

    private static Map<Long, Session> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathVariable long uId) {
        clients.put(uId, session);
    }

    @OnClose
    public void onClose(@PathVariable Long uId) {
        clients.remove(uId);
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String json) {
        Message message = JSON.parseObject(json,Message.class);
        Session rcvSession = clients.get(message.getMsgReceiver());
        if(rcvSession == null){
            offlineMsgMapper.storeOfflineMsg(message);
        }else {
            rcvSession.getAsyncRemote().sendText(json);
        }
    }

    @GetMapping("/getOfflineMsg")
    public List<Message> getOfflineMsg(@RequestParam long uId){
        List<Message> offlineMsgList = offlineMsgMapper.fetchOfflineMsg(uId);
        return offlineMsgList;
    }
}
