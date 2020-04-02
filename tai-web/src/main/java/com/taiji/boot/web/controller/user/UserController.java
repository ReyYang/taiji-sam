package com.taiji.boot.web.controller.user;

import com.taiji.boot.biz.bo.user.UserBO;
import com.taiji.boot.biz.business.user.UserBusiness;
import com.taiji.boot.common.beans.asserts.BusinessAssert;
import com.taiji.boot.common.beans.result.Result;
import com.taiji.boot.common.beans.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Demo UserController
 *
 * @author ydy
 * @date 2020/4/2 20:00
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserBusiness userBusiness;

    @PostMapping("/add")
    public Result<Boolean> save(@RequestBody UserBO userBO) {
        BusinessAssert.notNull(userBO, "保存用户对象为null");
        return ResultUtil.buildSuccessResult(userBusiness.save(userBO));
    }

}
