package com.taiji.boot.biz.bo.user;

import com.taiji.boot.biz.bo.BaseBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserBO extends BaseBO {

    private static final long serialVersionUID = 8437885617853680372L;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 手机号
     */
    private String mobileNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 是否锁定；0：未锁定；1：已锁定
     */
    private Boolean locked;
}
