package com.zjk.hy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjk.hy.dto.UserDo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends BaseMapper<UserDo> {
    @Select("select * from s_user")
    List<UserDo> findList();
}
