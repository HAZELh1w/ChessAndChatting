package com.example.cc.entity;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
}
