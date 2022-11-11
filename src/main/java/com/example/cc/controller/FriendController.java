package com.example.cc.controller;

import com.example.cc.entity.FriendApply;
import com.example.cc.entity.User;
import com.example.cc.mapper.FriendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/10 13:45
 */
@RestController
@CrossOrigin
public class FriendController {
    @Autowired
    private FriendMapper friendMapper;

    @PostMapping("/postFriendApply")
    public boolean postFriendApply(@RequestBody FriendApply friendApply){
        friendApply.setAplId(System.currentTimeMillis());
        friendApply.setApplyTime(new Timestamp(System.currentTimeMillis()));
        int i = friendMapper.addFriendApply(friendApply);
        if(i > 0){
            return true;
        }
        return false;
    }

    @GetMapping("/getFriendApply")
    public List<FriendApply> getFriendApply(@RequestParam long uId){
        List<FriendApply> aplList = friendMapper.findByAplReceiver(uId);
        return aplList;
    }

    @PostMapping("/acceptFriendApply")
    public boolean acceptFriendApply(@RequestBody FriendApply friendApply){
        friendApply.setState(1);
        int i = friendMapper.processFriendApply(friendApply);
        int j = friendMapper.addFriendship(friendApply.getApplicant(), friendApply.getAplReceiver());
        if(i > 0 && j > 0){
            return true;
        }
        return false;
    }

    @PostMapping("/rejectFriendApply")
    public boolean rejectFriendApply(@RequestBody FriendApply friendApply){
        friendApply.setState(2);
        int i = friendMapper.processFriendApply(friendApply);
        if(i > 0){
            return true;
        }
        return false;
    }

    @DeleteMapping("/deleteFriendship")
    public boolean deleteFriendship(@RequestParam  long uIdA, @RequestParam long uIdB){
        int i = friendMapper.deleteFriendship(uIdA, uIdB);
        if(i > 0){
            return true;
        }
        return false;
    }

    @GetMapping("/getFriendList")
    public List<User> getFriendList(@RequestParam long uId){
        List<User> friendList = friendMapper.getFriendList(uId);
        return friendList;
    }
}
