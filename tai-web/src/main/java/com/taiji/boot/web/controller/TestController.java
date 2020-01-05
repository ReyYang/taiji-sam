package com.taiji.boot.web.controller;

import com.taiji.boot.buiness.user.UserBusiness;
import com.taiji.boot.dal.base.user.entity.UserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Demo TestController
 *
 * @author ydy
 * @date 2020/1/4 21:06
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private UserBusiness userBusiness;

    @GetMapping("/detail/{id}")
    public UserEntity getUser(@PathVariable Integer id) {
        return userBusiness.getUser(id);
    }
}
