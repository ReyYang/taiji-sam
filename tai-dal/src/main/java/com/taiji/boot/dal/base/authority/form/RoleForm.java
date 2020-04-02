package com.taiji.boot.dal.base.authority.form;

import lombok.Data;

/**
 * Demo RoleForm
 *
 * @author ydy
 * @date 2020/4/1 21:03
 */
@Data
public class RoleForm {

    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;
}
