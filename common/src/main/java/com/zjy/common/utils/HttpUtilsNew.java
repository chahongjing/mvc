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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
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
import sun.net.www.protocol.ftp.FtpURLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
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
 * @description http???????????????
 * @date 2021-01-20 17:55
 */
public class HttpUtilsNew {
    static final Logger logger = LoggerFactory.getLogger(HttpUtilsNew.class);

    static CloseableHttpClient httpClient;

    private HttpUtilsNew() {
    }

    // ?????????????????????httpClient
    static {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        registryBuilder.register("http", new PlainConnectionSocketFactory());
        //??????????????????????????????????????????????????????
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //??????????????????
            TrustStrategy anyTrustStrategy = (X509Certificate[] x509Certificates, String s) -> true;
            HostnameVerifier hostnameVerifier = (String s, SSLSession sslSession) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            registryBuilder.register("https", new SSLConnectionSocketFactory(sslContext, hostnameVerifier));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //?????????????????????
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registryBuilder.build());
//        connManager.setDefaultConnectionConfig(connConfig);
//        connManager.setDefaultSocketConfig(socketConfig);

        //???????????????
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000)
                // .setProxy(new HttpHost("127.0.0.1", 8888))
                .build();
        httpClient = HttpClientBuilder.create().setConnectionManager(connManager).setDefaultRequestConfig(config)
                .setConnectionManagerShared(true)
                .evictExpiredConnections()
                //MaxIdleTime ?????????????????????????????????????????????????????????NoHttpResponse
                //????????????????????????????????????????????????????????????????????????????????????????????????builder#evictIdleConnections???????????????????????????builder#setmaxIdleTime???????????????????????????;
                .evictIdleConnections(10, TimeUnit.SECONDS).build();
    }

    // region get??????

    /**
     * get??????
     *
     * @param url    url
     * @param params url??????
     * @return ???????????????
     */
    public static String doGet(String url, List<NameValuePair> params) {
        return doGet(url, params, null);
    }

    /**
     * get??????
     *
     * @param url     url
     * @param params  url??????
     * @param headers header??????
     * @return ???????????????
     */
    public static String doGet(String url, List<NameValuePair> params, List<Header> headers) {
        byte[] bytes = doHttp(new HttpGet(), url, params, null, headers);
        if (bytes != null && bytes.length > 0) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return "";
    }
    // endregion

    // region post??????

    /**
     * post??????
     *
     * @param url        url
     * @param urlParams  url??????
     * @param bodyParams body??????
     * @return ???????????????
     */
    public static String doPost(String url, List<NameValuePair> urlParams, Map<String, Object> bodyParams) {
        return doPost(url, urlParams, bodyParams, null, null);
    }

    /**
     * post??????
     *
     * @param url        url
     * @param urlParams  url??????
     * @param bodyParams body??????
     * @param mediaType  ??????????????????json
     * @return ???????????????
     */
    public static String doPost(String url, List<NameValuePair> urlParams, Map<String, Object> bodyParams, MediaType mediaType) {
        return doPost(url, urlParams, bodyParams, null, mediaType);
    }

    /**
     * post??????
     *
     * @param url        url
     * @param urlParams  url??????
     * @param bodyParams body??????
     * @param headers    header??????
     * @return ???????????????
     */
    public static String doPost(String url, List<NameValuePair> urlParams, Map<String, Object> bodyParams, List<Header> headers) {
        return doPost(url, urlParams, bodyParams, headers, null);
    }

    /**
     * post??????
     *
     * @param url        url
     * @param urlParams  url??????
     * @param bodyParams body??????
     * @param headers    ??????header
     * @param mediaType  ??????????????????json
     * @return ???????????????
     */
    public static String doPost(String url, List<NameValuePair> urlParams, Map<String, Object> bodyParams, List<Header> headers, MediaType mediaType) {
        HttpEntity entity = null;
        //??????????????????
        if (MapUtils.isNotEmpty(bodyParams)) {
            // json????????????
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
                // form??????????????????
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

    // region ??????????????????????????????????????????????????????????????????

    /**
     * post????????????????????????
     *
     * @param httpMethod ??????????????????get, post
     * @param url        url
     * @param urlParams  url??????
     * @param entity     body??????
     * @param headers    header??????
     * @return ???????????????
     */
    public static byte[] doHttp(HttpRequestBase httpMethod, String url, List<NameValuePair> urlParams, HttpEntity entity, List<Header> headers) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        // ??????header
        // ??????cookie
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
        // ??????url???get??????
        try {
            if (CollectionUtils.isNotEmpty(urlParams)) {
                url += StringUtils.contains(url, '?') ? "&" : "?";
                url += EntityUtils.toString(new UrlEncodedFormEntity(urlParams, StandardCharsets.UTF_8));
            }
            httpMethod.setURI(URI.create(url));

            // ?????????content?????????content
            if (entity != null && httpMethod instanceof HttpEntityEnclosingRequestBase) {
                ((HttpEntityEnclosingRequestBase) httpMethod).setEntity(entity);
            }
            response = httpClient.execute(httpMethod);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.warn(String.format("??????http??????????????????%s???url???%s???header???%s?????????????????????%d", httpMethod.getMethod(), url, JSON.toJSONString(headers), response.getStatusLine().getStatusCode()));
                return null;
            }
            HttpEntity respEntity = response.getEntity();
            bytes = EntityUtils.toByteArray(respEntity);
            EntityUtils.consume(respEntity);
            logger.info(String.format("??????http??????????????????%s???url???%s???header???%s??????????????????%s", httpMethod.getMethod(), url, JSON.toJSONString(headers), new String(bytes, "utf-8")));
        } catch (Exception ex) {
            logger.error(String.format("??????http??????????????????%s???url???%s???header???%s", httpMethod.getMethod(), url, JSON.toJSONString(headers)), ex);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception ex) {
                    logger.error("?????????????????????", ex);
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
                //??????????????????????????????IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //?????????????????????????????????????????????IP??????????????????IP,??????IP??????','??????
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    public static boolean testUrlWithTimeOut(String urlString, int timeOutMillSeconds){
        long lo = System.currentTimeMillis();
        try {
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection)url.openConnection();
            co.setConnectTimeout(timeOutMillSeconds);
            co.connect();
            return co.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception ignore) {

        }
        return false;
    }

    public static void main(String[] args) {
        String url = "http://localhost:8088/static/js/manifest.697c382e1893556e4ee21.js";
        System.out.println(testUrlWithTimeOut(url, 100));
    }
}
