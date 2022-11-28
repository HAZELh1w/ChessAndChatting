package com.example.cc.mapper;

import com.example.cc.entity.Message;
import org.apache.ibatis.annotations.Delete;
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
public interface MessageMapper {
    @Insert("insert into offlineMsg(msgId,sendTime,sender,msg,msgReceiver) values(#{msgId},#{sendTime},#{sender},#{msg},#{msgReceiver})")
    public int storeMessage(Message message);

    @Select("select * from offlineMsg o where o.msgReceiver = #{uId} and o.sender = #{fId}")
    public List<Message> fetchMessage(long uId,long fId);

    @Delete("delete from offlineMsg o where o.msgReceiver = #{uId} and o.sender = #{fId}")
    public int deleteOverdueMessage(long uId,long fId);
}
