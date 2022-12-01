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
    @Insert("")
    public int recordHistory(History history);

    @Select("")
    public List<History> findHistoryByUId();
}
