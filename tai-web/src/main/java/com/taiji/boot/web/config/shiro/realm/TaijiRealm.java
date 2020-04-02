package com.taiji.boot.web.config.shiro.realm;

import com.taiji.boot.biz.business.user.UserBusiness;
import com.taiji.boot.common.utils.shiro.ShiroUtils;
import com.taiji.boot.dal.base.authority.form.PermissionForm;
import com.taiji.boot.dal.base.authority.form.RoleForm;
import com.taiji.boot.dal.base.user.form.UserForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Demo ShiroRealm
 *
 * @author ydy
 * @date 2020/3/31 17:26
 */
@Slf4j
public class TaijiRealm extends AuthorizingRealm {


    @Resource
    private UserBusiness userBusiness;

    /**
     * 功能描述: 获取授权信息
     *
     * @param principalCollection 1
     * @return : org.apache.shiro.authz.AuthorizationInfo
     * @author : ydy
     * @date : 2020/3/31 17:49
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
        UserForm userForm = (UserForm) principalCollection.getPrimaryPrincipal();
        for (RoleForm role : userForm.getRoles()) {
            authenticationInfo.addRole(role.getName());
            for (PermissionForm permission : userForm.getPermissions()) {
            }
        }
        return null;
    }

    /**
     * 功能描述: 获取身份验证信息（登录时调用）
     *
     * @param authenticationToken 1
     * @return : org.apache.shiro.authc.AuthenticationInfo
     * @author : ydy
     * @date : 2020/3/31 17:49
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        UserForm user = userBusiness.getUserDetail(username);
//        UserForm user = new UserForm();
//        user.setLoginName("yangyihui");
//        user.setPassword("866df58f02acee5dab06812befe4a2664975b89079d85c1fdeb419e74c21c215");
        if (Objects.isNull(user)) {
            throw new UnknownAccountException("该用户不存在");
        }
        if (user.getLocked()) {
            throw new LockedAccountException("该用户已被锁定，请联系管理员解锁");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(username + ShiroUtils.PUBLICKEY), getName());
    }
}
