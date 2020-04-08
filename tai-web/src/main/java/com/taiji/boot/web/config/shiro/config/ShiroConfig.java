package com.taiji.boot.web.config.shiro.config;

import com.taiji.boot.biz.middleware.redis.RedisClient;
import com.taiji.boot.service.authority.AuthorityService;
import com.taiji.boot.service.user.UserService;
import com.taiji.boot.web.config.shiro.credentials.RetryHashedCredentialsMatcher;
import com.taiji.boot.web.config.shiro.realm.TaijiRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Demo ShiroConfig
 *
 * @author ydy
 * @date 2020/4/2 15:03
 */
@Configuration
public class ShiroConfig {

    @Resource
    private RedisClient redisClient;

    @Resource
    private AuthorityService authorityService;

    @Bean
    public SecurityManager getSecurityManager() {
        DefaultSecurityManager manager = new DefaultSecurityManager();
        manager.setRealm(getRealm());
        return manager;
    }

    public Realm getRealm() {
        TaijiRealm realm = new TaijiRealm();
        realm.setCredentialsMatcher(getCredentialsMatcher());
        return realm;
    }

    @Bean
    public CredentialsMatcher getCredentialsMatcher() {
        RetryHashedCredentialsMatcher matcher = new RetryHashedCredentialsMatcher(redisClient, authorityService);
        matcher.setHashAlgorithmName("SHA-256");
        matcher.setHashIterations(500000);
        matcher.setStoredCredentialsHexEncoded(true);
        return matcher;
    }

}
