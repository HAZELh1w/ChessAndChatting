package com.example.cc.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/18 8:38
 */
@Data
@Builder
public class Message {
    private long msgId;
    private Timestamp sendTime;
    private long sender;
    private String msg;
    private long msgReceiver;
}
