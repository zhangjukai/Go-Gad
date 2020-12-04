package com.zjk.hy.controller;

import com.zjk.hy.dto.UserDo;
import com.zjk.hy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping(value = "/user/list")
    public List<UserDo> findList() {
        return userService.findList();
    }
}
