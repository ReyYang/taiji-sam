package com.taiji.boot.dal.base.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author taiji
 * @since 2020-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 登录名
     */
    @TableField("login_name")
    private String loginName;

    /**
     * 登陆密码
     */
    @TableField("password")
    private String password;

    /**
     * 手机号
     */
    @TableField("mobile_number")
    private String mobileNumber;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 是否锁定；0：未锁定；1：已锁定
     */
    @TableField("locked")
    private Boolean locked;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


}
