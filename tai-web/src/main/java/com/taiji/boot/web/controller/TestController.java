package com.taiji.boot.web.controller;

import com.taiji.boot.biz.bo.user.UserBO;
import com.taiji.boot.biz.business.user.UserBusiness;
import com.taiji.boot.biz.vo.user.UserVO;
import com.taiji.boot.common.beans.page.PaginationQuery;
import com.taiji.boot.common.beans.page.PaginationResult;
import com.taiji.boot.common.beans.result.Result;
import com.taiji.boot.common.beans.result.ResultUtil;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/list/page")
    public Result<PaginationResult<UserVO>> listPage(@RequestBody PaginationQuery<UserBO> query) {
        return ResultUtil.buildSuccessResult(userBusiness.listPage(query));
    }

    @PutMapping("/updateUser")
    public Result<Boolean> updateUser(@RequestBody UserBO user) {
        return ResultUtil.buildSuccessResult(userBusiness.updateUser(user));
    }
}
