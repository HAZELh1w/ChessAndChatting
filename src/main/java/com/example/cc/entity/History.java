package com.example.cc.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/12/1 13:52
 */
@Data
@Builder
public class History {
    private long uId;
    private int res;//0平，1胜，-1负
    private Timestamp endTime;
    private long fae;
}
