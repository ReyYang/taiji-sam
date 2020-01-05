package com.taiji.boot.buiness.user;

import com.taiji.boot.dal.base.user.entity.UserEntity;
import com.taiji.boot.service.user.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserBusiness {

    @Resource
    private UserService userService;

    public UserEntity getUser(Integer id) {
        return userService.getUserById(id);
    }
}
