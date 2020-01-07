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
        BeanUtil.copyProperties(user, new UserVO());
        System.out.println(vo);
        return vo;
    }

    public static void main(String[] args) {
        UserEntity entity = new UserEntity();
        entity.setAge(15);
        entity.setName("yang");
        entity.setEmail("1270730209@qq.com");
        entity.setType(1);
        UserVO userVO = BeanUtil.toBean(entity, UserVO.class);
        System.out.println(userVO);
    }

}
