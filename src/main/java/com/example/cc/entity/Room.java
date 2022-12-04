package com.example.cc.entity;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/27 16:17
 */
@Data
@Builder
public class Room {
    private String rName;
    private String rID;
    private HashMap<Long,User> members;
    private String rPwd;
    private long owner;
    private int memberCount;
    private int capacity;
    private HashMap<Integer,Chess> chessBoard;
    private HashMap<Integer,Chess> preChessBoard;
    private long player;
    private HashMap<Long,MatchApply> applies;
    private long repentanceApplicant;
    private long drawApplicant;
    private long surrenderApplicant;
    private int roomState;//0空闲，1对局中
}
