package com.example.cc.controller;
import com.example.cc.entity.Message;
import com.example.cc.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/27 18:15
 */
@RestController("/fChat/{uId}")
@CrossOrigin
public class FriendChatController {
    @Autowired
    private MessageMapper messageMapper;

    @PostMapping("/{fId}")
    public boolean sendMessage(@PathVariable long uId,@PathVariable long fId,@RequestBody Message message){
        int i = messageMapper.storeMessage(message);
        return i > 0;
    }

    @GetMapping("/{fId}")
    public List<Message> getMessage(@PathVariable long uId,@PathVariable long fId){
        List<Message> messageList = messageMapper.fetchMessage(uId,fId);
        messageMapper.deleteOverdueMessage(uId,fId);
        return messageList;
    }
}
