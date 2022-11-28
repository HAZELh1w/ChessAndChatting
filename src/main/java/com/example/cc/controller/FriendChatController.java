package com.example.cc.controller;
import com.example.cc.entity.Message;
import com.example.cc.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/27 18:15
 */
@RestController
@CrossOrigin
public class FriendChatController {
    @Autowired
    private MessageMapper messageMapper;

    @PostMapping("/fChat/{uId}/{fId}")
    public boolean sendMessage(@PathVariable long uId,@PathVariable long fId,@RequestBody Message message){
        message.setSender(uId);
        message.setSendTime(new Timestamp(System.currentTimeMillis()));
        message.setMsgReceiver(fId);
        int i = messageMapper.storeMessage(message);
        return i > 0;
    }

    @GetMapping("/fChat/{uId}/{fId}")
    public List<Message> getMessage(@PathVariable long uId,@PathVariable long fId){
        List<Message> messageList = messageMapper.fetchMessage(uId,fId);
        messageMapper.deleteOverdueMessage(uId,fId);
        return messageList;
    }
}
