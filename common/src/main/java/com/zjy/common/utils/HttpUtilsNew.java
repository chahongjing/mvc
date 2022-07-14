package com.zjy.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author junyi.zeng
 * @description http请求工具类
 * @date 2021-01-20 17:55
 */
public class HttpUtilsNew {
    static final Logger logger = LoggerFactory.getLogger(HttpUtilsNew.class);

    static CloseableHttpClient httpClient;

    private HttpUtilsNew() {
    }

    // 静态代码，生成httpClient
    static {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        registryBuilder.register("http", new PlainConnectionSocketFactory());
        //指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接
            TrustStrategy anyTrustStrategy = (X509Certificate[] x509Certificates, String s) -> true;
            HostnameVerifier hostnameVerifier = (String s, SSLSession sslSession) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            registryBuilder.register("https", new SSLConnectionSocketFactory(sslContext, hostnameVerifier));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registryBuilder.build());
//        connManager.setDefaultConnectionConfig(connConfig);
//        connManager.setDefaultSocketConfig(socketConfig);

        //构建客户端
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000)
                // .setProxy(new HttpHost("127.0.0.1", 8888))
                .build();
        httpClient = HttpClientBuilder.create().setConnectionManager(connManager).setDefaultRequestConfig(config)
                .setConnectionManagerShared(true)
                .evictExpiredConnections()
                //MaxIdleTime 必须小于服务端的关闭时间否则有可能出现NoHttpResponse
                //用来关闭闲置连接，它会启动一个守护线程进行清理工作。用户可以通过builder#evictIdleConnections开启该组件，并通过builder#setmaxIdleTime设置最大空闲时间。;
                .evictIdleConnections(10, TimeUnit.SECONDS).build();
    }

    // region get请求

    /**
     * get请求
     *
     * @param url    url
     * @param params url参数
     * @return 返回字符串
     */
    public static String doGet(String url, List<NameValuePair> params) {
        return doGet(url, params, null);
    }

    /**
     * get请求
     *
     * @param url     url
     * @param params  url参数
     * @param headers header参数
     * @return 返回字符串
     */
    public static String doGet(String url, List<NameValuePair> params, List<Header> headers) {
        byte[] bytes = doHttp(new HttpGet(), url, params, null, headers);
        if (bytes != null && bytes.length > 0) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return "";
    }
    // endregion

    // region post请求

    /**
     * post请求
     *
     * @param url        url
     * @param urlParams  url参数
     * @param bodyParams body参数
     * @return 返回字符串
     */
    public static String doPost(String url, List<NameValuePair> urlParams, Map<String, Object> bodyParams) {
        return doPost(url, urlParams, bodyParams, null, null);
    }

    /**
     * post请求
     *
     * @param url        url
     * @param urlParams  url参数
     * @param bodyParams body参数
     * @param mediaType  内容类型，如json
     * @return 返回字符串
     */
    public static String doPost(String url, List<NameValuePair> urlParams, Map<String, Object> bodyParams, MediaType mediaType) {
        return doPost(url, urlParams, bodyParams, null, mediaType);
    }

    /**
     * post请求
     *
     * @param url        url
     * @param urlParams  url参数
     * @param bodyParams body参数
     * @param headers    header参数
     * @return 返回字符串
     */
    public static String doPost(String url, List<NameValuePair> urlParams, Map<String, Object> bodyParams, List<Header> headers) {
        return doPost(url, urlParams, bodyParams, headers, null);
    }

    /**
     * post请求
     *
     * @param url        url
     * @param urlParams  url参数
     * @param bodyParams body参数
     * @param headers    请求header
     * @param mediaType  内容类型，如json
     * @return 返回字符串
     */
    public static String doPost(String url, List<NameValuePair> urlParams, Map<String, Object> bodyParams, List<Header> headers, MediaType mediaType) {
        HttpEntity entity = null;
        //设置请求参数
        if (MapUtils.isNotEmpty(bodyParams)) {
            // json格式传参
            if (mediaType == MediaType.APPLICATION_JSON) {
                JSONObject content = new JSONObject();
                for (Map.Entry<String, Object> entry : bodyParams.entrySet()) {
                    if (entry.getValue() == null) continue;
                    content.put(entry.getKey(), entry.getValue());
                }
                entity = new StringEntity(JSON.toJSONString(content), StandardCharsets.UTF_8);
                ((StringEntity) entity).setContentType(MediaType.APPLICATION_JSON_VALUE);
                ((StringEntity) entity).setContentEncoding(StandardCharsets.UTF_8.name());
            } else {
                // form表单形式传参
                List<BasicNameValuePair> bodyP = new ArrayList<>();
                for (Map.Entry<String, Object> entry : bodyParams.entrySet()) {
                    if (entry.getValue() == null) continue;
                    bodyP.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                entity = new UrlEncodedFormEntity(bodyP, StandardCharsets.UTF_8);
            }
        }
        byte[] bytes = doHttp(new HttpPost(), url, urlParams, entity, headers);
        if (bytes != null && bytes.length > 0) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return "";
    }
    // endregion

    // region 最底层的请求，返回字节数组，支持处理文件相关

    /**
     * post请求，返回字符串
     *
     * @param httpMethod 请求方式，如get, post
     * @param url        url
     * @param urlParams  url参数
     * @param entity     body参数
     * @param headers    header参数
     * @return 返回字节码
     */
    public static byte[] doHttp(HttpRequestBase httpMethod, String url, List<NameValuePair> urlParams, HttpEntity entity, List<Header> headers) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        // 设置header
        // 传递cookie
//    Cookie[] cookies = request.getCookies();
//    String cookiesString = Arrays.stream(cookies).map(item -> {
//        String name = item.getName();
//        String value = item.getValue();
//        return name + "=" + value;
//    }).collect(Collectors.joining(";"));
//    NameValuePair header = new BasicNameValuePair("Cookie", cookiesString);
        if (CollectionUtils.isNotEmpty(headers)) {
            for (Header header : headers) {
                httpMethod.setHeader(header);
            }
        }
        byte[] bytes = null;
        CloseableHttpResponse response = null;
        // 设置url及get参数
        try {
            if (CollectionUtils.isNotEmpty(urlParams)) {
                url += StringUtils.contains(url, '?') ? "&" : "?";
                url += EntityUtils.toString(new UrlEncodedFormEntity(urlParams, StandardCharsets.UTF_8));
            }
            httpMethod.setURI(URI.create(url));

            // 如果有content，设置content
            if (entity != null && httpMethod instanceof HttpEntityEnclosingRequestBase) {
                ((HttpEntityEnclosingRequestBase) httpMethod).setEntity(entity);
            }
            response = httpClient.execute(httpMethod);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.warn(String.format("调用http请求失败！【%s】url：%s。header：%s。返回状态码：%d", httpMethod.getMethod(), url, JSON.toJSONString(headers), response.getStatusLine().getStatusCode()));
                return null;
            }
            HttpEntity respEntity = response.getEntity();
            bytes = EntityUtils.toByteArray(respEntity);
            EntityUtils.consume(respEntity);
            logger.info(String.format("调用http请求成功！【%s】url：%s。header：%s。返回结果：%s", httpMethod.getMethod(), url, JSON.toJSONString(headers), new String(bytes, "utf-8")));
        } catch (Exception ex) {
            logger.error(String.format("调用http请求失败！【%s】url：%s。header：%s", httpMethod.getMethod(), url, JSON.toJSONString(headers)), ex);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception ex) {
                    logger.error("回收资源失败！", ex);
                }
            }
        }
        return bytes;
    }
    // endregion

    public String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
