package com.zjk.hy.service.impl;

import com.zjk.hy.dao.UserDao;
import com.zjk.hy.dto.UserDo;
import com.zjk.hy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Override
    public List<UserDo> findList() {
        return userDao.findList();
    }
}
