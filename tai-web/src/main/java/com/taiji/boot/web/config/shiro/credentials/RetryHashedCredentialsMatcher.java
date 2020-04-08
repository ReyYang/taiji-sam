package com.taiji.boot.web.config.shiro.credentials;

import cn.hutool.core.util.StrUtil;
import com.taiji.boot.biz.middleware.redis.RedisClient;
import com.taiji.boot.common.beans.asserts.BusinessAssert;
import com.taiji.boot.common.utils.shiro.ShiroUtils;
import com.taiji.boot.service.authority.AuthorityService;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demo RetryHashCredentialsMatcher
 *
 * @author ydy
 * @date 2020/4/8 14:28
 */
public class RetryHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private RedisClient redisClient;

    private AuthorityService authorityService;

    public RetryHashedCredentialsMatcher(RedisClient client, AuthorityService authorityService) {
        this.redisClient = client;
        this.authorityService = authorityService;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        AtomicInteger errorNum = new AtomicInteger(0);
        String errorCacheNumber = redisClient.get(ShiroUtils.LOGIN_ERR_CACHE_KEY + username);
        if (StrUtil.isNotBlank(errorCacheNumber)) {
            errorNum = new AtomicInteger(Integer.parseInt(errorCacheNumber));
        }
        if (errorNum.get() >= 5) {
            authorityService.lockUser(username);
            throw new ExcessiveAttemptsException("密码错误次数过多，该账号已被锁定。请联系管理员解锁");
        }
        boolean match = super.doCredentialsMatch(token, info);
        if (match) {
            redisClient.delete(ShiroUtils.LOGIN_ERR_CACHE_KEY + username);
        } else {
            redisClient.put(ShiroUtils.LOGIN_ERR_CACHE_KEY + username, errorNum.incrementAndGet(), 1800);
            throw new AccountException(StrUtil.format("密码输入错误，错误超过5次后账户将被锁定。当前次数：%s", errorNum.get()));
        }
        return true;
    }
}
