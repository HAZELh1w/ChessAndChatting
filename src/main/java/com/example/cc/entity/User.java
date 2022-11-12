package com.example.cc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private int wins;

    private int loses;

    private int total;

    private int draws;

    private double winRate;

    private int state;
}
