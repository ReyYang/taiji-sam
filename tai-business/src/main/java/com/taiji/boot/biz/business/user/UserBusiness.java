package com.taiji.boot.biz.business.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.taiji.boot.biz.bo.user.UserBO;
import com.taiji.boot.biz.middleware.redis.RedisClient;
import com.taiji.boot.biz.vo.user.UserVO;
import com.taiji.boot.common.beans.page.PaginationQuery;
import com.taiji.boot.common.beans.page.PaginationResult;
import com.taiji.boot.common.beans.page.PaginationUtil;
import com.taiji.boot.common.redis.factory.CacheInterfaceFactory;
import com.taiji.boot.common.utils.IdUtils;
import com.taiji.boot.common.utils.TaiBeansUtils;
import com.taiji.boot.common.utils.shiro.ShiroUtils;
import com.taiji.boot.dal.base.authority.entity.PermissionEntity;
import com.taiji.boot.dal.base.authority.entity.PermissionGroupEntity;
import com.taiji.boot.dal.base.authority.entity.RoleEntity;
import com.taiji.boot.dal.base.authority.form.PermissionForm;
import com.taiji.boot.dal.base.authority.form.PermissionGroupForm;
import com.taiji.boot.dal.base.authority.form.RoleForm;
import com.taiji.boot.dal.base.user.entity.UserEntity;
import com.taiji.boot.dal.base.user.form.UserForm;
import com.taiji.boot.service.authority.AuthorityService;
import com.taiji.boot.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserBusiness {

    @Resource
    private UserService userService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    private CacheInterfaceFactory factory;

    @Resource
    private RedisClient redisClient;


    public UserVO getUser(Integer id) {
        UserEntity user = userService.getUserById(id);
        return BeanUtil.toBean(user, UserVO.class);
    }

    public UserForm getUserDetail(String username) {
        UserForm userForm = BeanUtil.toBean(userService.getUserByLoginName(username), UserForm.class);
        List<RoleEntity> roles = authorityService.listRoleByUserId(userForm.getUserId());
        List<PermissionEntity> permissions = authorityService.listPermissionByRoleId(roles.parallelStream().map(RoleEntity::getRoleId).distinct().collect(Collectors.toList()));
        List<PermissionGroupEntity> permissionGroups = authorityService.listPermissionGroupByUserIds(userForm.getUserId());
        userForm.setRoles(TaiBeansUtils.copyProperties(roles, RoleForm.class));
        userForm.setPermissions(TaiBeansUtils.copyProperties(permissions, PermissionForm.class));
        userForm.setPermissionGroups(TaiBeansUtils.copyProperties(permissionGroups, PermissionGroupForm.class));
        return BeanUtil.toBean(userService.getUserByLoginName(username), UserForm.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean save(UserBO userBO) {
        UserEntity entity = BeanUtil.toBean(userBO, UserEntity.class);
        entity.setUserId(IdUtils.getId());
        entity.setPassword(ShiroUtils.getEncryptPwd(entity.getPassword(), entity.getLoginName()));
        userService.insertUser(entity);
        return Boolean.TRUE;
    }
}
