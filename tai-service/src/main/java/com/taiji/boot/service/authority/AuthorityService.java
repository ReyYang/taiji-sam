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

    List<PermissionEntity> listPermissionByGroupId(List<Long> groupIds);

    List<PermissionEntity> listPermissionByRoleId(List<Long> roleIds);

    List<RoleEntity> listRoleByUserId(Long userId);

    List<PermissionGroupEntity> listPermissionGroupByUserIds(Long userId);

}
