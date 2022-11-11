package com.example.cc.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/10 13:58
 */
@Data
public class FriendApply {
    private long aplId;

    private long applicant;

    private int state = 0;

    private Timestamp applyTime;

    private long aplReceiver;

}
