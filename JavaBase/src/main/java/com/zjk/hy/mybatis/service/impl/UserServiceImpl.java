package com.zjk.hy.mybatis.service.impl;

import com.zjk.hy.mybatis.dao.UserDao;
import com.zjk.hy.mybatis.dto.User;
import com.zjk.hy.mybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Override
    public List<User> findList() {
        List<User> list = userDao.findList();
        return list;
    }
}
