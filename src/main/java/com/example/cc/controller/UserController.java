package com.example.cc.controller;

import com.example.cc.entity.User;
import com.example.cc.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/5 19:55
 */
@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/searchUserByUId")
    public User searchUserByUId(@RequestParam long uId){
        return userMapper.findByUId(uId);
    }

    @PostMapping("/login")
    public boolean login(@RequestBody User user, HttpServletRequest request){
        User userDB = userMapper.findByUId(user.getUId());
        Boolean loginSuccess = userDB.getUPwd().equals(user.getUPwd());
        if(loginSuccess){
            HttpSession session = request.getSession();
            session.setAttribute("uId",user.getUId());
        }
        return loginSuccess;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        user.setUId(System.currentTimeMillis());
        userMapper.insertUser(user);
        return user;
    }

    @PutMapping("/reviseUName")
    public boolean reviseUName(@RequestBody User user){
        int i = userMapper.reviseUName(user);
        if(i > 0){
            return true;
        }
        return false;
    }

    @PutMapping("/reviseUPwd")
    public boolean reviseUPwd(@RequestBody User user){
        int i = userMapper.reviseUPwd(user);
        if(i > 0){
            return true;
        }
        return false;
    }

}
