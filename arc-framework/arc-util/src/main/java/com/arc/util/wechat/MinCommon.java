package com.arc.util.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arc.util.file.oss.OssProperties;
import com.arc.util.lang.FaultException;
import com.arc.util.http.HttpRequest;
import kf.arc.util.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序相关
 */
@Configuration
@EnableConfigurationProperties(WechatProperties.class)
@ConditionalOnProperty(prefix = "wechat",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false)
@Slf4j
public class MinCommon {
    private final WechatProperties properties;

    private static RedisClient redisClient = new RedisClient("RedisCache");

    public MinCommon(WechatProperties wechatProperties){
        this.properties = wechatProperties;
    }

    /**
     * 微信公众号获取token
     *
     * @return 返回token
     */
    public  String getMinAccessToken() {
        String tokenCache = "WXP_TEMPLATE_" + properties.getMinAppid();
        if (redisClient.exists(tokenCache)) {
            return redisClient.get(tokenCache);
        }
        String accessTokenUrl = properties.getRootHost()+"/weixin" + "/cgi-bin/token?grant_type=client_credential&appid=" +
                properties.getMinAppid() + "&secret=" + properties.getMinSecret();
        log.info("getMinAccessToken req url {}", accessTokenUrl);
        String result = HttpRequest.get(accessTokenUrl).body();
        log.info("common getMinAccessToken result:{}", result);
        JSONObject resp = JSONObject.parseObject(result);
        if (resp.containsKey("errcode") && resp.getIntValue("errcode") != 0) {
            throw new FaultException("获取minAccessToken失败" + resp.getString("errmsg"));
        }
        String accessToken = resp.getString("access_token");
        redisClient.set(tokenCache, accessToken, (resp.getIntValue("expires_in") - 10));
        return accessToken;
    }

    /**
     * 微信公众号获取token
     *
     * @return 返回token
     */
    public  String getPubAccessToken() {
        String tokenCache = "WXP_TEMPLATE_" + properties.getWxAppid();
        if (redisClient.exists(tokenCache)) {
            return redisClient.get(tokenCache);
        }
        String accessTokenUrl = properties.getRootHost()+"/weixin" + "/cgi-bin/token?grant_type=client_credential&appid=" +
                properties.getWxAppid() + "&secret=" + properties.getWxSecret();
        log.info("accessReq url {}", accessTokenUrl);
        String result = HttpRequest.get(accessTokenUrl).body();
        log.info("common getAccessToken result:{}", result);
        JSONObject resp = JSONObject.parseObject(result);
        if (resp.containsKey("errcode") && resp.getIntValue("errcode") != 0) {
            throw new FaultException("获取accessToken失败" + resp.getString("errmsg"));
        }
        String accessToken = resp.getString("access_token");
        redisClient.set(tokenCache, accessToken, (resp.getIntValue("expires_in") - 10));
        return accessToken;
    }

    /**
     * 发送保单消息
     *
     * @param request
     */
    public  void sendMinTip(JSONObject request) {
        String accessToken = getMinAccessToken();
        String url = properties.getRootHost()+"/weixin" + "/cgi-bin/message/wxopen/template/send?access_token=" + accessToken;
        log.info("sendMinTip url:{} data:{}", url, request.toJSONString());
        String result = HttpRequest.post(url).header("Content-Type", HttpRequest.CONTENT_TYPE_JSON)
                .send(request.toJSONString()).body();
        log.info("sendMinTip result {}", result);
    }

    private  void reSendMinTip(JSONObject request) {
        String accessToken = getMinAccessToken();
        String url = properties.getRootHost()+"/weixin" + "/cgi-bin/message/wxopen/template/send?access_token=" + accessToken;
        log.info("sendMinTip url:{} data:{}", url, request.toJSONString());
        String result = HttpRequest.post(url).header("Content-Type", HttpRequest.CONTENT_TYPE_JSON)
                .send(request.toJSONString()).body();
        log.info("sendMinTip result {}", result);
    }

    /**
     * 发送公众号模板消息
     *
     * @param request
     */
    public  void sendPubTip(JSONObject request) {
        String accessToken = getPubAccessToken();
        String url = properties.getRootHost()+"/weixin" + "/cgi-bin/message/template/send?access_token=" + accessToken;
        log.info("sendPubTip url:{} data:{}", url, request.toJSONString());
        String result = HttpRequest.post(url).header("Content-Type", HttpRequest.CONTENT_TYPE_JSON)
                .send(request.toJSONString()).body();
        log.info("sendPubTip result {}", result);
        JSONObject sendResult = JSON.parseObject(result);
        if (sendResult.getIntValue("errcode") == 40001) {
            redisClient.del("WXP_TEMPLATE_" + properties.getWxAppid());
            reSendPubTip(request);
        }
    }

    private  void reSendPubTip(JSONObject request) {
        String accessToken = getPubAccessToken();
        String url = properties.getRootHost()+"/weixin" + "/cgi-bin/message/template/send?access_token=" + accessToken;
        log.info("sendPubTip url:{} data:{}", url, request.toJSONString());
        String result = HttpRequest.post(url).header("Content-Type", HttpRequest.CONTENT_TYPE_JSON)
                .send(request.toJSONString()).body();
        log.info("sendPubTip result {}", result);
    }

    /**
     * 微信公众号 刷新access_token
     *
     * @param
     */
    public  void getAccessTokenByRefreshToken(String refreshToken) {
        String url = properties.getRootHost()+"/weixin" + "/sns/oauth2/refresh_token?appid=" +
                properties.getWxAppid() + "&grant_type=refresh_token&refresh_token=" + refreshToken;
        log.info("wxp getAccessTokenByRefreshToken url :{}", url);
        HttpRequest httpRequest = HttpRequest.get(url);
        String result = httpRequest.body();
        log.info("wxp getAccessTokenByRefreshToken result:{}", result);
        JSONObject resp = JSONObject.parseObject(result);
        if (resp.containsKey("errcode") && resp.getIntValue("errcode") != 0) {
            throw new FaultException("微信公众号刷新access_token失败" + resp.getString("errmsg"));
        }

        log.info("wxp  access_token:{}", resp.getString("access_token"));

        //缓存access_token  refresh_token
        redisClient.set("wxp_access_token", resp.getString("access_token"), 7200);


    }

    /**
     * 获得ticket
     *
     * @param
     */
    public  String getJsapiTicketByAccessToken() {
        String accessToken = getPubAccessToken();
        String url = properties.getRootHost()+"/weixin"+ "/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
        log.info("wxp jsapi_ticket url :{}", url);
        HttpRequest httpRequest = HttpRequest.get(url);
        String result = httpRequest.body();
        log.info("wxp jsapi_ticket result:{}", result);
        JSONObject resp = JSONObject.parseObject(result);
        if (resp.containsKey("errcode") && resp.getIntValue("errcode") != 0) {
            throw new FaultException("微信公众号获得jsapi_ticket失败" + resp.getString("errmsg"));
        }

        log.info("wxp  jsapi_ticket:{}", resp.getString("ticket"));
        return resp.getString("ticket");
    }

}