package com.example.cc.controller;

import com.example.cc.entity.*;
import com.example.cc.mapper.HistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static Map<Long, List<Message>> msgMap = new HashMap<>();

    @Autowired
    private HistoryMapper historyMapper;

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
        room.setApplies(new HashMap<Long,MatchApply>());
        room.setRoomState(0);
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
        if (room != null){
            HashMap<Long, User> members = room.getMembers();
            HashMap<Long, MatchApply> applies = room.getApplies();
            for (MatchApply matchApply : applies.values()){
                if(!members.containsKey(matchApply.getApplicant())){
                    applies.remove(matchApply.getAplId());
                }
            }
            long player = room.getPlayer();
            if (!members.containsKey(player)){
                room.setPlayer(0);
            }
        }
        return room;
    }

    @PutMapping("/{rId}/transferOwner")
    public boolean transferOwner(@PathVariable String rId, @RequestBody User user) {
        Room room = roomMap.get(rId);
        if (room != null) {
            long uId = user.getUId();
            if (room.getMembers().containsKey(uId) && room.getMembers().containsKey(uId)) {
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
            long applicant = tarApply.getApplicant();
            applies.remove(apply.getAplId());
            if(room.getMembers().containsKey(applicant) && room.getRoomState() == 0){
                room.setPlayer(applicant);
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
            room.setRoomState(1);
            HashMap<Integer, Chess> chessBoard = room.getChessBoard();
            for (int i = 0; i < 32; i++){
                Chess chess = Chess.builder().cId(i).alive(true).build();
                chessBoard.put(i,chess);
            }
            for (int i = 0; i < 9; i++){
                Chess chess = chessBoard.get(i);
                chess.setXPos(i);
                chess.setYPos(0);
                Chess chess1 = chessBoard.get(i + 23);
                chess1.setXPos(i);
                chess1.setYPos(9);
            }
            chessBoard.get(10).setXPos(1);
            chessBoard.get(10).setYPos(2);
            chessBoard.get(11).setXPos(7);
            chessBoard.get(11).setYPos(2);
            chessBoard.get(21).setXPos(1);
            chessBoard.get(21).setYPos(7);
            chessBoard.get(22).setXPos(7);
            chessBoard.get(22).setYPos(7);
            for (int i = 11; i < 16; i++){
                Chess chess = chessBoard.get(i);
                chess.setXPos(2*(i-11));
                chess.setYPos(3);
                Chess chess1 = chessBoard.get(i+5);
                chess1.setXPos(2*(i-11));
                chess1.setYPos(6);
            }
            return true;
        }
        return false;
    }

    @PutMapping("/{rId}/move")
    public boolean move(@PathVariable String rId, @RequestBody Move move){
        Room room = roomMap.get(rId);
        if (room != null){
            boolean kill = move.isKill();
            HashMap<Integer, Chess> chessBoard = room.getChessBoard();
            HashMap<Integer, Chess> preChessBoard = room.getPreChessBoard();
            for(int i = 0; i < 32; i++){
                preChessBoard.get(i).setAlive(chessBoard.get(i).isAlive());
                preChessBoard.get(i).setXPos(chessBoard.get(i).getXPos());
                preChessBoard.get(i).setYPos(chessBoard.get(i).getYPos());
            }
            if(kill){
                for (int i = 0; i < 32; i++){
                    Chess preChess = preChessBoard.get(i);
                    if (preChess.getXPos() == move.getNewXPos() && preChess.getYPos() == move.getNewYPos() && preChess.isAlive()){
                        Chess chess = chessBoard.get(i);
                        chess.setAlive(false);
                    }
                }
            }
            chessBoard.get(move.getCId()).setXPos(move.getNewXPos());
            chessBoard.get(move.getCId()).setYPos(move.getNewYPos());
            return true;
        }
        return false;
    }

    @PostMapping("/{rId}/postRepentance")
    public boolean postRepentance(@PathVariable String rId,@RequestBody User user){
        Room room = roomMap.get(rId);
        if (room != null){
            if(user.getUId() == room.getPlayer() || user.getUId() == room.getOwner()){
                room.setRepentanceApplicant(user.getUId());
                return true;
            }
        }
        return false;
    }

    @PutMapping("/{rId}/acceptRepentance")
    public boolean acceptRepentance(@PathVariable String rId){
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
            }
            room.setRepentanceApplicant(0);
            return true;
        }
        return false;
    }

    @PutMapping("/{rId}/rejectRepentance")
    public boolean rejectRepentance(@PathVariable String rId){
        Room room = roomMap.get(rId);
        if (room != null){
            room.setRepentanceApplicant(0);
            return true;
        }
        return false;
    }

    @PostMapping("/{rId}/postDraw")
    public boolean postDraw(@PathVariable String rId,@RequestBody User user){
        Room room = roomMap.get(rId);
        if (room != null){
            if(user.getUId() == room.getPlayer() || user.getUId() == room.getOwner()){
                room.setDrawApplicant(user.getUId());
                return true;
            }
        }
        return false;
    }

    @PutMapping("/{rId}/acceptDraw")
    public boolean acceptDraw(@PathVariable String rId){
        Room room = roomMap.get(rId);
        if (room != null){
            room.setDrawApplicant(0);
            return true;
        }
        return false;
    }

    @PutMapping("/{rId}/rejectDraw")
    public boolean rejectDraw(@PathVariable String rId){
        Room room = roomMap.get(rId);
        if (room != null){
            room.setDrawApplicant(0);
            return true;
        }
        return false;
    }



    @PostMapping("/{rId}/endGame")
    public boolean endGame(@PathVariable String rId,@RequestBody User winner){
        Room room = roomMap.get(rId);
        if (room != null){
            long owner = room.getOwner();
            long player = room.getPlayer();
            Timestamp endTime = new Timestamp(System.currentTimeMillis());
            History history1 = History.builder().uId(owner).res(0).endTime(endTime).fae(player).build();
            History history2 = History.builder().uId(player).res(0).endTime(endTime).fae(owner).build();
            if(winner.getUId() == owner){
                history1.setRes(1);
                history2.setRes(-1);
            }
            if(winner.getUId() == player){
                history1.setRes(-1);
                history2.setRes(1);
            }
            historyMapper.recordHistory(history1);
            historyMapper.recordHistory(history2);
            room.setRoomState(0);
            return true;
        }
        return false;
    }
}
