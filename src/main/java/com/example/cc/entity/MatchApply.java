package com.example.cc.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/28 15:48
 */
@Data
@Builder
public class MatchApply {
    private long aplId;
    private long applicant;
    private Timestamp applyTime;
    private String rId;
}
