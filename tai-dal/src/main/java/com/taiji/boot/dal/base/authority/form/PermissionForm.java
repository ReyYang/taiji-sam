package com.taiji.boot.dal.base.authority.form;

import lombok.Data;

/**
 * Demo PermissionForm
 *
 * @author ydy
 * @date 2020/4/1 21:03
 */
@Data
public class PermissionForm {
    /**
     * id
     */
    private Long id;

    /**
     * 权限id
     */
    private Long permissionId;

    /**
     * 所属权限组id
     */
    private Long groupId;

    /**
     * 权限url
     */
    private String url;

    /**
     * 权限名称
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
