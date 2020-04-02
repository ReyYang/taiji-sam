package com.taiji.boot.dal.base.user.form;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.taiji.boot.dal.base.authority.form.PermissionForm;
import com.taiji.boot.dal.base.authority.form.PermissionGroupForm;
import com.taiji.boot.dal.base.authority.form.RoleForm;
import lombok.Data;

import java.util.List;

/**
 * Demo UserForm
 *
 * @author ydy
 * @date 2020/4/1 21:01
 */
@Data
public class UserForm {
    /**
     * id
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

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

    private List<RoleForm> roles;
    private List<PermissionForm> permissions;
    private List<PermissionGroupForm> permissionGroups;
}
