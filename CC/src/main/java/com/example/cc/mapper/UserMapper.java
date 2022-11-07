package com.example.cc.mapper;

import com.example.cc.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/5 19:55
 */
@Mapper
public interface UserMapper {
    @Select("select * from user")
    public List<User> find();

    @Insert("insert into user(uId,uName,uPwd) values(#{uId},#{uName},#{uPwd})")
    public int insertUser(User user);

    @Select("select * from user where uId = #{uId}")
    public User findByUId(User user);

    @Update("update user set uName = #{uName} where uId = #{uId}")
    public int reviseUName(User user);

    @Update("update user set uPwd = #{uPwd} where uId = #{uId}")
    public int reviseUPwd(User user);
}
