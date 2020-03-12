package com.taiji.boot.biz.business.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.taiji.boot.biz.bo.user.UserBO;
import com.taiji.boot.biz.vo.user.UserVO;
import com.taiji.boot.common.beans.page.PaginationQuery;
import com.taiji.boot.common.beans.page.PaginationResult;
import com.taiji.boot.common.beans.page.PaginationUtil;
import com.taiji.boot.common.redis.factory.CacheInterfaceFactory;
import com.taiji.boot.common.utils.TaiBeansUtils;
import com.taiji.boot.dal.base.user.entity.UserEntity;
import com.taiji.boot.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.file.Watchable;
import java.util.List;

@Slf4j
@Component
public class UserBusiness {

    @Resource
    private UserService userService;

    @Resource
    private CacheInterfaceFactory factory;

    public UserVO getUser(Integer id) {
        UserEntity user = userService.getUserById(id);
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, new UserVO());
        System.out.println(vo);
        return vo;
    }

    public static void main(String[] args) {
        UserEntity entity = new UserEntity();
        entity.setAge(15);
        entity.setName("yang");
        entity.setEmail("1270730209@qq.com");
        entity.setType(1);
        entity.setUserId(12345678888855L);
        UserVO userVO = BeanUtil.toBean(entity, UserVO.class);
        System.out.println(userVO);
    }

    public PaginationResult<UserVO> listPage(PaginationQuery<UserBO> query) {
        QueryWrapper<UserEntity> wrapper = buildQueryWrapper(query.getParams());
        if (query.isPageable()) {
            int pageSize = query.getPageSize();
            int start = query.getStart();
            wrapper.last("limit " + start + StrUtil.C_COMMA + pageSize);
        }
        long total = userService.countUserByWrapper(wrapper);
        List<UserEntity> entityList = userService.getUserByWrapper(wrapper);
        return PaginationUtil.buildPageResult(total, TaiBeansUtils.copyProperties(entityList, UserVO.class));
    }

    private QueryWrapper<UserEntity> buildQueryWrapper(UserBO params) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        if (ObjectUtil.isNull(params)) {
            return wrapper;
        }
        if (StrUtil.isNotBlank(params.getName())) {
            wrapper.eq("name", params.getName());
        }
        if (ObjectUtil.isNotNull(params.getType())) {
            wrapper.eq("type", params.getType());
        }
        return wrapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUser(UserBO user) {
        UpdateWrapper<UserEntity> wrapper = buildUpdateWrapper(user);
        UserEntity entity = BeanUtil.toBean(user, UserEntity.class);

        return null;
    }

    private UpdateWrapper<UserEntity> buildUpdateWrapper(UserBO user) {
        UpdateWrapper<UserEntity> wrapper = new UpdateWrapper<>();
        return null;
    }

    public boolean setRedis(String key, String value) {
        factory.put(key, value, 10);
        return true;
    }
}
