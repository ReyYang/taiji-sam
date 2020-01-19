package com.taiji.boot.common.utils.http;

import com.alibaba.fastjson.JSONObject;
import com.taiji.boot.common.beans.exception.BaseException;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Demo HttpUtils
 *
 * @author ydy
 * @date 2020/1/6 16:26
 */
@Slf4j
public class HttpUtils {

    public final static String GET = "GET";

    public final static String POST = "POST";

    public final static String PUT = "PUT";

    public final static String DELETE = "DELETE";

    public final static String PATCH = "PATCH";

    private final static String UTF8 = "UTF-8";

    private final static String GBK = "GBK";

    private final static String DEFAULT_CHARSET = UTF8;

    private final static String DEFAULT_METHOD = GET;

    private final static String DEFAULT_MEDIA_TYPE = "application/json";

    private final static boolean DEFAULT_LOG = false;

    private final static OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(20, 5, TimeUnit.MINUTES))
            .readTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS).build();

    /**
     * 通用执行方法
     */
    private static String execute(OkHttp okHttp) {
        if (StringUtils.isEmpty(okHttp.requestCharset)) {
            okHttp.requestCharset = DEFAULT_CHARSET;
        }
        if (StringUtils.isEmpty(okHttp.responseCharset)) {
            okHttp.responseCharset = DEFAULT_CHARSET;
        }
        if (StringUtils.isEmpty(okHttp.method)) {
            okHttp.method = DEFAULT_METHOD;
        }
        if (StringUtils.isEmpty(okHttp.mediaType)) {
            okHttp.mediaType = DEFAULT_MEDIA_TYPE;
        }
        // 记录请求日志
        if (okHttp.requestLog) {
            log.info("com.taiji.boot.common.utils.http.HttpUtils send log = [{}]", okHttp.toString());
        }

        String url = okHttp.url;
        Request.Builder requestBuilder = new Request.Builder();

        if (MapUtils.isNotEmpty(okHttp.queryMap)) {
            String queryParams = okHttp.queryMap.entrySet().stream().map(
                    entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(
                    Collectors.joining("&"));
            url = String.format("%s%s%s", url, url.contains("?") ? "&" : "?", queryParams);
        }
        requestBuilder.url(url);

        // 设置请求头
        if (MapUtils.isNotEmpty(okHttp.headerMap)) {
            okHttp.headerMap.forEach(requestBuilder::addHeader);
        }

        // 设置请求类型
        String method = okHttp.method.toUpperCase();
        String mediaType = String.format("%s;charset=%s", okHttp.mediaType, okHttp.requestCharset);

        if (method.equals(GET)) {
            requestBuilder.get();
        } else if (ArrayUtils.contains(new String[]{POST, PUT, DELETE, PATCH}, method)) {
            RequestBody requestBody = RequestBody.create(okHttp.data, MediaType.parse(mediaType));
            requestBuilder.method(method, requestBody);
        } else {
            throw new BaseException("未设置请求method");
        }

        // 返回值
        String result = "";
        try {
            Response response = CLIENT.newCall(requestBuilder.build()).execute();
            byte[] bytes = response.body().bytes();
            result = new String(bytes, okHttp.responseCharset);
            // 记录返回日志
            if (okHttp.responseLog) {
                log.info("com.taiji.boot.common.utils.http.HttpUtils send log = [{}]", okHttp.toString());
            }
        } catch (Exception e) {
            throw new BaseException(e);
        }
        return result;
    }

    /**
     * 功能描述: GET请求
     * @param url URL地址
     * @return : com.alibaba.fastjson.JSONObject
     * @author : ydy
     * @date : 2019/12/23 21:39
     */
    public static JSONObject get(String url) {
        return JSONObject.parseObject(execute(OkHttp.builder().url(url).build()));
    }

    /**
     * 功能描述: GET请求
     * @param url URL地址
     * @param responseCharset 响应数据编码类型
     * @return : com.alibaba.fastjson.JSONObject
     * @author : ydy
     * @date : 2019/12/23 21:38
     */
    public static JSONObject get(String url, String responseCharset) {
        return JSONObject.parseObject(execute(OkHttp.builder().url(url).responseCharset(responseCharset).build()));
    }

    /**
     * 功能描述: 带查询参数的GET查询
     * @param url URL地址
     * @param queryMap 查询参数
     * @return : java.lang.String
     * @author : ydy
     * @date : 2019/12/23 21:38
     */
    public static JSONObject get(String url, Map<String, String> queryMap) {
        return JSONObject.parseObject(execute(OkHttp.builder().url(url).queryMap(queryMap).build()));
    }

    /**
     * 功能描述: 带查询参数的GET查询
     * 带响应字符编码
     * @param url 请求路径
     * @param queryMap 查询参数
     * @param responseCharset 响应数据编码类型
     * @return : java.lang.String
     * @author : ydy
     * @date : 2019/12/23 21:37
     */
    public static JSONObject get(String url, Map<String, String> queryMap, String responseCharset) {
        return JSONObject.parseObject(execute(OkHttp.builder().url(url).queryMap(queryMap).responseCharset(responseCharset).build()));
    }

    /**
     * 功能描述: POST查询
     * @param url url路径地址
     * @param data 请求携带参数
     * @return : com.alibaba.fastjson.JSONObject
     * @author : ydy
     * @date : 2019/12/23 21:48
     */
    public static JSONObject post(String url, String data) {
        return JSONObject.parseObject(execute(OkHttp.builder().url(url).method(POST).data(data).build()));
    }

    /**
     * 功能描述: POST查询
     * @param url url路径地址
     * @param data 请求携带参数
     * @param mediaType 消息体类型
     * @param responseCharset 响应数据编码类型
     * @return : java.lang.String
     * @author : ydy
     * @date : 2019/12/23 21:31
     */
    public static JSONObject post(String url, String data, String mediaType, String responseCharset) {
        return JSONObject.parseObject(execute(OkHttp.builder().url(url).method(POST).data(data).mediaType(mediaType).responseCharset(
                responseCharset).build()));
    }

    /**
     * 功能描述: POST查询 application/json
     * @param url 请求路径
     * @param obj javaBean 对象
     * @return : java.lang.String
     * @author : ydy
     * @date : 2019/12/23 21:37
     */
    public static JSONObject postJson(String url, Object obj) {
        return JSONObject.parseObject(execute(OkHttp.builder().url(url).method(POST).data(JSONObject.toJSONString(obj))
                .mediaType(DEFAULT_MEDIA_TYPE).build()));
    }

    /**
     * 功能描述:POST查询 application/x-www-form-urlencoded
     * @param url 请求路径
     * @param formMap 参数列表
     * @return : java.lang.String
     * @author : ydy
     * @date : 2019/12/23 21:35
     */
    public static JSONObject postForm(String url, Map<String, String> formMap) {
        String data = "";
        if (MapUtils.isNotEmpty(formMap)) {
            data = formMap.entrySet().stream().map(
                    entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(
                    Collectors.joining("&"));
        }
        return JSONObject.parseObject(execute(OkHttp.builder().url(url).method(POST).data(data).mediaType(
                "application/x-www-form-urlencoded").build()));
    }

    /**
     * http请求工具内部类
     *
     * @author gogym
     * @version 2018年7月30日
     * @see OkHttp
     * @since
     */
    @Builder
    @ToString(exclude = {"requestCharset", "responseCharset", "requestLog", "responseLog"})
    static class OkHttp {
        private String url;

        @Builder.Default
        private String method = DEFAULT_METHOD;

        private String data;

        @Builder.Default
        private String mediaType = DEFAULT_MEDIA_TYPE;

        private Map<String, String> queryMap;

        private Map<String, String> headerMap;

        @Builder.Default
        private String requestCharset = DEFAULT_CHARSET;

        @Builder.Default
        private boolean requestLog = DEFAULT_LOG;

        @Builder.Default
        private String responseCharset = DEFAULT_CHARSET;

        @Builder.Default
        private boolean responseLog = DEFAULT_LOG;
    }
}


