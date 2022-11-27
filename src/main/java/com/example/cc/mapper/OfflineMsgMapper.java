package com.example.cc.mapper;

import com.example.cc.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/27 18:38
 */
@Mapper
public interface OfflineMsgMapper {
    @Insert("insert into offlineMsg(msgId,sendTime,sender,msg,msgReceiver) values(#{msgId},#{sendTime},#{sender},#{msg},#{msgReceiver})")
    public int storeOfflineMsg(Message message);

    @Select("select * from offlineMsg o where o.msgReceiver = #{uId}")
    public List<Message> fetchOfflineMsg(long uId);
}
