package com.example.cc.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/29 19:18
 */
@Data
@Builder
public class Move {
    private int cId;
    private int xPos;
    private int yPos;
    private int newXPos;
    private int newYPos;
    private boolean kill;
}
