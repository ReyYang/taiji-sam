package com.taiji.boot.common.utils.shiro;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * Demo ShiroUtils
 *
 * @author ydy
 * @date 2020/4/2 20:10
 */
public class ShiroUtils {

    public static final String LOGIN_ERR_CACHE_KEY = "login-error::";

    public static final String PUBLICKEY = "264ecdd38f300bfbe1277bc9a3568434";

    public static final String ALGORITHM_NAME = "SHA-256";

    public static final Integer HASH_ITERATIONS = 500000;

    public static final String DEFAULT_PASSWORD = "123456";


    /**
     * 功能描述: 获取加密密码
     * @param password 密码
     * @param salt 盐值（用户名）
     * @return : java.lang.String
     * @author : ydy
     * @date : 2020/4/2 20:19
     */
    public static String getEncryptPwd(String password, String salt) {
        SimpleHash hash = new SimpleHash(ALGORITHM_NAME, password, salt + PUBLICKEY, HASH_ITERATIONS);
        return hash.toHex();
    }

    /**
     * 功能描述: 获取默认密码 - 123456
     * @param salt 盐值（用户名）
     * @return : java.lang.String
     * @author : ydy
     * @date : 2020/4/2 20:18
     */
    public static String getDefaultPwd(String salt) {
        SimpleHash hash = new SimpleHash(ALGORITHM_NAME, DEFAULT_PASSWORD, salt + PUBLICKEY, HASH_ITERATIONS);
        return hash.toHex();
    }

}
