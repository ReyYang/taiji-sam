package com.taiji.boot.dal.base.authority.mapper;

import com.taiji.boot.dal.base.authority.entity.RoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author taiji
 * @since 2020-04-01
 */
public interface RoleMapper extends BaseMapper<RoleEntity> {
    List<RoleEntity> selectRoleByUserId(@Param("userId") Long userId);
}
