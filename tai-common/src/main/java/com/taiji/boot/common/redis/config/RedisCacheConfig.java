package com.taiji.boot.common.redis.config;

import cn.hutool.core.util.ObjectUtil;
import com.taiji.boot.common.beans.exception.CacheException;
import com.taiji.boot.common.redis.constants.RedisConstants;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Properties;

/**
 * RedisCacheConfig  缓存客户端配置属性，采用的是缓存池。
 *
 * @author ydy
 * @date 2020/3/4 12:06
 */
public class RedisCacheConfig implements Serializable {
    private static final long serialVersionUID = 361136918778594776L;

    private String prefix = "";

    /***
     * 服务器地址
     */
    private String host;

    /***
     * 端口
     */
    private int port;

    /***
     * 超时时间，毫秒
     */
    private int timeout;

    /***
     * 分配的最大对象数
     */
    private int maxTotal;

    /***
     * 最大空闲数
     */
    private int maxIdle;

    /***
     * 当池内没有返回对象时，最大等待时间
     */
    private int maxWait;

    /**
     * 密码（使用阿里云redis必传，使用公司搭建的redis可以不传）
     */
    private String password;

    public RedisCacheConfig() {
    }

    public RedisCacheConfig(String filePath) {
        // 初始化属性
        initProperty(filePath);
    }

    private void initProperty(String filePath) {
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getResourceAsStream(filePath));
        } catch (IOException e) {
            throw new CacheException("RedisCacheConfig Initialization error", e);
        }
        if (prop.isEmpty()) {
            throw new CacheException("properties is null or no attribute");
        }
        this.prefix = prop.getProperty("prefix", "");
        this.host = prop.getProperty("host");
        this.port = Integer.parseInt(prop.getProperty("port"));
        this.timeout = Integer.parseInt(prop.getProperty("timeout"));
        this.maxIdle = Integer.parseInt(prop.getProperty("maxIdle"));
        this.maxTotal = Integer.parseInt(prop.getProperty("maxTotal"));
        this.maxWait = Integer.parseInt(prop.getProperty("maxWait"));
        this.password = prop.getProperty("password");
        checkConfigProperty();
    }

    private void checkConfigProperty() {
        if (ObjectUtil.isEmpty(host)) {
            throw new CacheException("host must be not null or empty");
        }
        if (RedisConstants.NUMBER_ZERO >= port) {
            throw new CacheException("port must be greater than 0");
        }
        if (RedisConstants.NUMBER_ZERO >= timeout) {
            throw new CacheException("timeout must be greater than 0");
        }
        if (RedisConstants.NUMBER_ZERO >= maxIdle) {
            throw new CacheException("maxIdle must be greater than 0");
        }
        if (RedisConstants.NUMBER_ZERO >= maxTotal) {
            throw new CacheException("maxTotal must be greater than 0");
        }
        if (RedisConstants.NUMBER_ZERO >= maxWait) {
            throw new CacheException("maxWait must be greater than 0");
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RedisCacheConfig that = (RedisCacheConfig) o;
        if (port != that.port)
            return false;
        if (timeout != that.timeout)
            return false;
        if (maxTotal != that.maxTotal)
            return false;
        if (maxIdle != that.maxIdle)
            return false;
        if (maxWait != that.maxWait)
            return false;
        if (!Objects.equals(prefix, that.prefix))
            return false;
        if (!Objects.equals(host, that.host))
            return false;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        int result = prefix != null ? prefix.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        result = 31 * result + timeout;
        result = 31 * result + maxTotal;
        result = 31 * result + maxIdle;
        result = 31 * result + maxWait;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
