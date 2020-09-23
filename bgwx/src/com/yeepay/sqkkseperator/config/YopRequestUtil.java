package com.yeepay.sqkkseperator.config;

import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.config.HttpClientConfig;
import com.yeepay.g3.sdk.yop.utils.RSAKeyUtils;
import org.apache.commons.lang.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * 易宝开放平台请求工具类
 * @Auther: _ckay
 * @Date: 2018/12/15 
 * @Description:
 */
public class YopRequestUtil {


    /**
     * 构造易宝请求参数
     * @auther:_ckay
     * @date: 2018/12/15
     */
    public static YopRequest getYopRequest(String appKey, String secretKey, String publicKey, String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        YopRequest yoprequest = null;
        if (StringUtils.isEmpty(secretKey)){
            yoprequest = new YopRequest(appKey);
        }else{
            yoprequest = new YopRequest(appKey, secretKey);
            yoprequest.getAppSdkConfig().setAesSecretKey(secretKey);
        }

        PrivateKey privateKeyObj = RSAKeyUtils.string2PrivateKey(privateKey);
        PublicKey publicKeyObk = RSAKeyUtils.string2PublicKey(publicKey);

//      yoprequest.getAppSdkConfig().setServerRoot(serverRoot); //默认请求https://open.yeepay.com/yop-center
        yoprequest.getAppSdkConfig().setAppKey(appKey);
        yoprequest.getAppSdkConfig().setDefaultIsvPrivateKey(privateKeyObj);
        yoprequest.getAppSdkConfig().setDefaultYopPublicKey(publicKeyObk);

        HttpClientConfig httpClientConfig = new HttpClientConfig();
        httpClientConfig.setConnectTimeout(10000);
        httpClientConfig.setMaxConnPerRoute(100);
        httpClientConfig.setMaxConnTotal(200);
        httpClientConfig.setReadTimeout(30000);
        yoprequest.getAppSdkConfig().setHttpClientConfig(httpClientConfig);

        return yoprequest;
    }

}
