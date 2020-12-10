package com.zjk.hy.dao;

import com.zjk.hy.dto.UserDo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDao {
    @Select("select * from s_user")
    List<UserDo> findList();
}
