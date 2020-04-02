package com.taiji.boot.service.authority.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taiji.boot.common.utils.IdUtils;
import com.taiji.boot.dal.base.authority.entity.PermissionEntity;
import com.taiji.boot.dal.base.authority.entity.PermissionGroupEntity;
import com.taiji.boot.dal.base.authority.entity.RoleEntity;
import com.taiji.boot.dal.base.authority.mapper.PermissionGroupMapper;
import com.taiji.boot.dal.base.authority.mapper.PermissionMapper;
import com.taiji.boot.dal.base.authority.mapper.RoleMapper;
import com.taiji.boot.service.authority.AuthorityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Demo AuthorityServiceImpl
 *
 * @author ydy
 * @date 2020/4/1 18:08
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private PermissionGroupMapper permissionGroupMapper;

    @Override
    public List<PermissionEntity> listPermissionByGroupId(List<Long> groupIds) {
        QueryWrapper<PermissionEntity> wrapper = new QueryWrapper<>();
        wrapper.in("group_id", groupIds);
        return permissionMapper.selectList(wrapper);
    }

    @Override
    public List<PermissionEntity> listPermissionByRoleId(List<Long> roleIds) {
        return permissionMapper.selectPermissionByRoleId(roleIds);
    }

    @Override
    public List<RoleEntity> listRoleByUserId(Long userId) {
        return roleMapper.selectRoleByUserId(userId);
    }

    @Override
    public List<PermissionGroupEntity> listPermissionGroupByUserIds(Long userId) {
        return permissionGroupMapper.selectPermissionGroupByRoleId(userId);
    }
}
