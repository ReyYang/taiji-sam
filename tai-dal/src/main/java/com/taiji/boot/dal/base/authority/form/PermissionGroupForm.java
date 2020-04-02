package com.taiji.boot.dal.base.authority.form;

/**
 * Demo PermissionGroupForm
 *
 * @author ydy
 * @date 2020/4/1 21:03
 */
public class PermissionGroupForm {
    /**
     * id
     */
    private Long id;

    /**
     * 权限组id
     */
    private Long permissionGroupId;

    /**
     * 权限组名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否有效：1有效，0无效
     */
    private Boolean status;

    /**
     * 排序
     */
    private Integer sort;
}
