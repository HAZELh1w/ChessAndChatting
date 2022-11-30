package com.example.cc.controller;

import com.example.cc.entity.*;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.MaskFormatter;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/27 18:16
 */
@RestController
@CrossOrigin
public class RoomController {
    private static Map<String, Room> roomMap = new HashMap<>();

    private static Map<Long, List<Message>> msgMap = new HashMap<>();

    @PostMapping("/createRoom")
    public Room createRoom(@RequestBody Room room) {
        String rId = "r" + System.currentTimeMillis();
        room.setRID(rId);
        if(room.getRName() == null){
            room.setRName(rId);
        }
        if (roomMap.containsKey(rId) && !room.getMembers().isEmpty()) {
            return null;
        }
        room.setCapacity(30);
        room.setMembers(new HashMap<Long,User>());
        room.setMemberCount(0);
        room.setChessBoard(new HashMap<Integer,Chess>());
        room.setPreChessBoard(new HashMap<Integer,Chess>());
        roomMap.put(rId, room);
        return room;
    }

    @GetMapping("/{rId}/checkRPwd")
    public boolean checkRPwd(@PathVariable String rId, @RequestParam String rPwd) {
        Room room = roomMap.get(rId);
        if (room != null) {
            if (room.getRPwd().equals(rPwd)) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("/{rId}/enterRoom")
    public boolean enterRoom(@PathVariable String rId, @RequestBody User user) {
        Room room = roomMap.get(rId);
        if (room != null) {
            if (room.getCapacity() > room.getMemberCount()) {
                HashMap<Long, User> members = room.getMembers();
                members.put(user.getUId(), user);
                int count = room.getMemberCount();
                count++;
                room.setMemberCount(count);
                msgMap.put(user.getUId(),new ArrayList<Message>());
                return true;
            }
        }
        return false;
    }

    @DeleteMapping("/{rId}/leaveRoom")
    public boolean leaveRoom(@PathVariable String rId, @RequestParam long uId) {
        Room room = roomMap.get(rId);
        if (room != null) {
            long owner = room.getOwner();
            HashMap<Long, User> members = room.getMembers();
            int count = room.getMemberCount();
            if (owner != uId) {
                members.remove(uId);
                count--;
                room.setMemberCount(count);
                msgMap.remove(uId);
                return true;
            } else {
                members.remove(owner);
                if (!members.isEmpty()) {
                    for (Long newOwner : members.keySet()) {
                        room.setOwner(newOwner);
                        break;
                    }
                } else {
                    roomMap.remove(rId);
                }
                count--;
                room.setMemberCount(count);
                msgMap.remove(uId);
                return true;
            }
        }
        return false;
    }

    @GetMapping("/{rId}/getRoomInfo")
    public Room getRoomInfo(@PathVariable String rId) {
        Room room = roomMap.get(rId);
        return room;
    }

    @PutMapping("/{rId}/transferOwner")
    public boolean transferOwner(@PathVariable String rId, @RequestParam long uId) {
        Room room = roomMap.get(rId);
        if (room != null) {
            if (room.getMembers().containsKey(uId)) {
                room.setOwner(uId);
                return true;
            }
        }
        return false;
    }

    @PutMapping("/{rId}/reviseRPwd")
    public boolean reviseRPwd(@PathVariable String rId, @RequestParam String newRPwd) {
        Room room = roomMap.get(rId);
        if (room != null) {
            room.setRPwd(newRPwd);
            return true;
        }
        return false;
    }

    @PostMapping("/{rId}/postMatchApply")
    public boolean postMatchApply(@PathVariable String rId, @RequestBody MatchApply apply) {
        apply.setApplyTime(new Timestamp(System.currentTimeMillis()));
        apply.setAplId(System.currentTimeMillis());
        apply.setState(0);
        apply.setRId(rId);
        Room room = roomMap.get(rId);
        if(room != null){
            HashMap<Long, MatchApply> applies = room.getApplies();
            applies.put(apply.getAplId(), apply);
            return true;
        }
        return false;
    }

    @PutMapping("/{rId}/acceptMatchApply")
    public boolean acceptMatchApply(@PathVariable String rId, @RequestBody MatchApply apply) {
        Room room = roomMap.get(rId);
        if(room != null){
            HashMap<Long, MatchApply> applies = room.getApplies();
            MatchApply tarApply = applies.get(apply.getAplId());
            if(tarApply.getState() == 0){
                tarApply.setState(1);
                return true;
            }
        }
        return false;
    }

    @GetMapping("/getRooms")
    public Map<String,Room> getRooms(){
        return roomMap;
    }

    @GetMapping("/getRoomsByRNameAndRId")
    public Map<String,Room> getRoomByRNameAndRId(@RequestParam String inputStr){
        Map<String,Room> rooms = new HashMap<>();
        for (String rId : roomMap.keySet()) {
            if(rId.equals(inputStr) || roomMap.get(rId).getRName().equals(inputStr)){
                rooms.put(rId,roomMap.get(rId));
            }
        }
        return rooms;
    }

    @PostMapping("/{rId}/sendMsg")
    public boolean sendMsg(@PathVariable String rId, @RequestBody Message message){
        Room room = roomMap.get(rId);
        if(room != null){
            message.setSendTime(new Timestamp(System.currentTimeMillis()));
            long broadcastRoom = Long.parseLong(rId.substring(1));
            message.setMsgReceiver(broadcastRoom);
            HashMap<Long, User> memberMap = room.getMembers();
            for (Long memberKey : memberMap.keySet()) {
                List<Message> messageList = msgMap.get(memberKey);
                messageList.add(message);
            }
            return true;
        }
        return false;
    }
    
    @GetMapping("/{rId}/getMsg")
    public List<Message> getMsg(@PathVariable String rId, @RequestParam long uId){
        List<Message> res = new ArrayList<>();
        Room room = roomMap.get(rId);
        long broadcastRoom = Long.parseLong(rId.substring(1));
        if(room != null){
            for (Message message : msgMap.get(uId)) {
                if(message.getMsgReceiver() == broadcastRoom){
                    res.add(message);
                }
            }
        }
        return res;
    }

    @PostMapping("/{rId}/initBoard")
    public boolean initBoard(@PathVariable String rId) {
        Room room = roomMap.get(rId);
        if(room != null){
            HashMap<Integer, Chess> chessBoard = room.getChessBoard();
            for(int i = 0; i < 32; i++){
                chessBoard.get(i).setAlive(true);
            }
        }
        return false;
    }

    @PutMapping("/{rId}/move")
    public boolean move(@PathVariable String rId, @RequestBody Move move){
        Room room = roomMap.get(rId);
        if (room != null){
            int kill = move.getKill();
            HashMap<Integer, Chess> chessBoard = room.getChessBoard();
            HashMap<Integer, Chess> preChessBoard = room.getPreChessBoard();
            for(int i = 0; i < 32; i++){
                preChessBoard.get(i).setAlive(chessBoard.get(i).isAlive());
                preChessBoard.get(i).setXPos(chessBoard.get(i).getXPos());
                preChessBoard.get(i).setYPos(chessBoard.get(i).getYPos());
            }
            if(kill >= 0){
                chessBoard.get(kill).setAlive(false);
            }
            chessBoard.get(move.getCId()).setXPos(move.getNewXPos());
            chessBoard.get(move.getCId()).setYPos(move.getNewYPos());
            return true;
        }
        return false;
    }

    @PostMapping("/{rId}/postRepentance")
    public boolean postRepentance(@PathVariable long rId,@RequestBody User user){
        Room room = roomMap.get(rId);
        if (room != null){
            if(user.getUId() == room.getPlayer() || user.getState() == room.getOwner()){
                room.setRepentanceApplicant(user.getUId());
                return true;
            }
        }
        return false;
    }

    @PutMapping("/{rId}/acceptRepentance")
    public boolean acceptRepentance(@PathVariable long rId){
        Room room = roomMap.get(rId);
        if (room != null){
            HashMap<Integer, Chess> chessBoard = room.getChessBoard();
            HashMap<Integer, Chess> preChessBoard = room.getPreChessBoard();
            for (int i = 0; i < 32; i++){
                Chess chess = chessBoard.get(i);
                Chess preChess = preChessBoard.get(i);
                chess.setAlive(preChess.isAlive());
                chess.setXPos(preChess.getXPos());
                chess.setYPos(preChess.getYPos());
                room.setRepentanceApplicant(0);
                return true;
            }
        }
        return false;
    }

    @GetMapping("/{rId}/surrender")
    public boolean surrender(){
        return false;
    }
}
