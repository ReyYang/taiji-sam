package com.taiji.boot.dal.base.authority.mapper;

import com.taiji.boot.dal.base.authority.entity.PermissionGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限组表 Mapper 接口
 * </p>
 *
 * @author taiji
 * @since 2020-04-01
 */
public interface PermissionGroupMapper extends BaseMapper<PermissionGroupEntity> {
    List<PermissionGroupEntity> selectPermissionGroupByRoleId(@Param("roleId") Long roleId);
}
