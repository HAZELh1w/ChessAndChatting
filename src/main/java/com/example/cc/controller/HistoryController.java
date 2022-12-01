package com.example.cc.controller;

import com.example.cc.entity.History;
import com.example.cc.mapper.HistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/12/1 14:20
 */
@RestController
@CrossOrigin
public class HistoryController {
    @Autowired
    private HistoryMapper historyMapper;

    @GetMapping("/{uId}/getHistory")
    public List<History> getHistory(@PathVariable long uId){
        return historyMapper.findHistoryByUId(uId);
    }
}
