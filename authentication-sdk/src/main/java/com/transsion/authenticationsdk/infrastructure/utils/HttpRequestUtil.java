package com.transsion.authenticationsdk.infrastructure.utils;

import com.alibaba.fastjson2.JSON;
import com.transsion.authenticationsdk.infrastructure.advice.CommResponse;
import com.transsion.authenticationsdk.infrastructure.constants.NetCodeEnum;
import com.transsion.authenticationsdk.infrastructure.exception.SdkCustomException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.ConnectException;
import java.util.Map;

/**
 * @Description: Http请求工具类
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
public class HttpRequestUtil {

    static CloseableHttpClient httpclient = HttpClients.createDefault();

    public static CommResponse postHttp(String url, String params, Map<String, String> headers) {
        try {
            HttpPost post = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000 * 2)
                    .setSocketTimeout(5000).build();
            post.setConfig(requestConfig);
            post.setHeader("Content-Type", "application/json; charset=UTF-8");
            // 添加请求头
            for (String s : headers.keySet()) {
                post.setHeader(s, headers.get(s));
            }
            // 设置实体
            post.setEntity(new StringEntity(params, ContentType.APPLICATION_JSON));
            // 执行方法
            CloseableHttpResponse res = httpclient.execute(post);
            String resultStr = EntityUtils.toString(res.getEntity());
            res.close();
            return JSON.parseObject(resultStr, CommResponse.class);
        } catch (ConnectException connectException) {
            //链接源失效时错误
            throw new SdkCustomException(NetCodeEnum.KMS_AUTH_CHANNEL.getCode(), "kms服务无效地址");
        } catch (Exception e) {
            throw new SdkCustomException(e.getMessage());
        }
    }
}
