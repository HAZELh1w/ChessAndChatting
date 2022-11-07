package com.example.cc.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/5 19:54
 */
@Data
@Builder
public class User {
    private long uId;

    private String uName;

    private String uPwd;

    private int wins = 0;

    private int loses = 0;

    private int draws = 0;

    private int total = 0;

    private double winRate = 0;

    private int state = 0;
}
