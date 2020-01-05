package com.taiji.boot.service.user;

import com.taiji.boot.dal.base.user.entity.UserEntity;

public interface UserService {

    int insertUser(UserEntity userEntity);

    UserEntity getUserById(Integer userId);
}
