package com.taiji.boot.web.controller;

import com.taiji.boot.biz.business.user.UserBusiness;
import com.taiji.boot.biz.vo.user.UserVO;
import com.taiji.boot.common.beans.result.Result;
import com.taiji.boot.common.beans.result.ResultUtil;
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
    public Result<UserVO> getUser(@PathVariable Integer id) {
        Result<UserVO> userVOResult = ResultUtil.buildSuccessResult(userBusiness.getUser(id));
        String s = userVOResult.toString();
        System.out.println(s);
        return userVOResult;
    }
}
