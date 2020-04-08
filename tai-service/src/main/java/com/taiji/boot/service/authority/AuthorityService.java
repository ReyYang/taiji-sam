package com.taiji.boot.service.authority;

import com.taiji.boot.dal.base.authority.entity.PermissionEntity;
import com.taiji.boot.dal.base.authority.entity.PermissionGroupEntity;
import com.taiji.boot.dal.base.authority.entity.RoleEntity;

import java.util.List;

/**
 * Demo AthorityService
 *
 * @author ydy
 * @date 2020/4/1 15:00
 */
public interface AuthorityService {

    void batchSaveRole(List<RoleEntity> entities);

    void batchSavePermission(List<PermissionEntity> entities);

    void batchSavePermissionGroup(List<PermissionGroupEntity> entities);

    List<PermissionEntity> listPermissionByGroupId(List<Long> groupIds);

    List<PermissionEntity> listPermissionByRoleId(List<Long> roleIds);

    List<RoleEntity> listRoleByUserId(Long userId);

    List<PermissionGroupEntity> listPermissionGroupByUserIds(Long userId);

    void lockUser(String username);

}
