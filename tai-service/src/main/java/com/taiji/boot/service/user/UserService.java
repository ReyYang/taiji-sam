package com.taiji.boot.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taiji.boot.dal.base.user.entity.UserEntity;

import java.util.List;

public interface UserService {

    int insertUser(UserEntity userEntity);

    UserEntity getUserById(Integer userId);

    List<UserEntity> getUserByWrapper(QueryWrapper<UserEntity> wrapper);
}