package com.example.cc.mapper;

import com.example.cc.entity.FriendApply;
import com.example.cc.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/10 13:56
 */
@Mapper
public interface FriendMapper {
    @Insert("insert into friendApl(aplId,applyTime,applicant,aplReceiver,state) values(#{aplId},#{applyTime},#{applicant},#{aplReceiver},#{state})")
    public int addFriendApply(FriendApply friendApply);

    @Select("select * from friendApl f where f.aplReceiver = #{aplReceiver}")
    public List<FriendApply> findByAplReceiver(long aplReceiver);

    @Update("update friendApl f set f.state = #{state} where f.aplId = #{aplId}")
    public int processFriendApply(FriendApply friendApply);

    @Insert("insert into friendship values(#{uIdA},#{uIdB}),(#{uIdB},#{uIdA})")
    public int addFriendship(long uIdA,long uIdB);

    @Delete("delete from friendship where (uIdA = #{uIdA} and uIdB = #{uIdB}) or (uIdA = #{uIdB} and uIdB = #{uIdA})")
    public int deleteFriendship(long uIdA,long uIdB);

    @Select("select distinct * from user u join friendship f on u.uId = f.uIdB where f.uIdA = #{uId} and u.uId = f.uIdB")
    public List<User> getFriendList(long uId);

}
