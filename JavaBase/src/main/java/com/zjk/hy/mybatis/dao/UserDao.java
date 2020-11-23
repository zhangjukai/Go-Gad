package com.zjk.hy.mybatis.dao;

import com.zjk.hy.mybatis.dto.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDao {
    @Select("select * from s_user")
    public List<User> findList();
}
