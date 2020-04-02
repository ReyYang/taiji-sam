package com.taiji.boot.dal.base.authority.mapper;

import com.taiji.boot.dal.base.authority.entity.PermissionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author taiji
 * @since 2020-04-01
 */
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

    List<PermissionEntity> selectPermissionByRoleId(@Param("roleIds") List<Long> roleIds);
}
