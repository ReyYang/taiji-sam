package com.taiji.boot.biz.business.user;

import cn.hutool.core.bean.BeanUtil;
import com.taiji.boot.biz.vo.user.UserVO;
import com.taiji.boot.dal.base.user.entity.UserEntity;
import com.taiji.boot.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class UserBusiness {

    @Resource
    private UserService userService;

    public UserVO getUser(Integer id) {
        UserEntity user = userService.getUserById(id);
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        System.out.println(vo);
        return vo;
    }
}
