package com.example.cc.controller;

import com.example.cc.entity.Chess;
import com.example.cc.entity.MatchApply;
import com.example.cc.entity.Room;
import com.example.cc.entity.User;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/createRoom")
    public boolean createRoom(@RequestBody Room room) {
        String rId = "r" + System.currentTimeMillis();
        room.setRID(rId);
        if(room.getRName() == null){
            room.setRName(rId);
        }
        if (roomMap.containsKey(rId) && !room.getMembers().isEmpty()) {
            return false;
        }
        room.setMemberCount(0);
        roomMap.put(rId, room);
        return true;
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

//    @PostMapping("/{rId}/initBoard")
//    public boolean initBoard(@PathVariable String rId) {
//        Room room = roomMap.get(rId);
//        if(room != null){
//            HashMap<Integer, Chess> chessBoard = room.getChessBoard();
//            for(int i = 0; )
//        }
//        return false;
//    }


}
