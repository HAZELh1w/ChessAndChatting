package com.example.cc.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/28 14:52
 */
@Data
@Builder
public class Chess {
    private int cId;
    private int xPos;
    private int yPos;
    private boolean alive;
}
