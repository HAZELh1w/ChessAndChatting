package com.example.cc.mapper;

import com.example.cc.entity.History;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/12/1 13:56
 */
@Mapper
public interface HistoryMapper {
    @Insert("insert into history(uId,res,endTime,fae) values(#{uId},#{res},#{endTime},#{fae}")
    public int recordHistory(History history);

    @Select("select * from history h where h.uId = #{uId}")
    public List<History> findHistoryByUId(long uId);
}
