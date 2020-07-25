package com.md.union.front.api.facade;

import com.alibaba.fastjson.JSONObject;
import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.arc.util.http.HttpRequest;
import com.arc.util.lang.EncryptUtil;
import com.md.union.front.api.config.MinProperties;
import com.md.union.front.api.vo.MinUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@EnableConfigurationProperties(MinProperties.class)
@Slf4j
public class MinCommon {

    private final MinProperties properties;

    public MinCommon(MinProperties properties) {
        this.properties = properties;
    }


    /**
     * 微信小程序登录
     *
     * @param code 小程序客户端获取到的动态code
     * @return
     */
    public MinUser minLogin(String code) {
        String url = properties.getRouteHoust() + "/sns/jscode2session?appid=" + properties.getMinAppId() + "&secret=" + properties.getMinSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        log.info("minLogin req url :{}", url);
        HttpRequest httpRequest = HttpRequest.get(url);
        String result = httpRequest.body();
        log.info("minLogin result:{}", result);
        JSONObject resp = JSONObject.parseObject(result);
        if (resp.containsKey("errcode") && resp.getIntValue("errcode") != 0) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "登录失败" + resp.getString("errmsg"));
        }
        MinUser minUser = new MinUser();
        minUser.setSessionKey(resp.getString("session_key"));
        minUser.setUnionId(resp.getString("unionid"));
        minUser.setMinId(resp.getString("openid"));
        minUser.setType(2);
        minUser.setSessionId(EncryptUtil.md5("wxlogin" + new Date().getTime() + minUser.getMinId() + minUser.getSessionKey()));


        //微信小程序
        /*WXUserDto.MinUser minUserReq = new WXUserDto.MinUser();
        minUserReq.setMinId(resp.getString("openid"));
        WXUserDto.MinUser minUserResp = wxUserService.infoMinUser(minUserReq);
        if (minUserResp != null) {
            minUser.setMinId(minUserResp.getMinId());
        }
        //查询手机用户信息
        WXUserDto.MobileUser mobileUserReq = new WXUserDto.MobileUser();
        mobileUserReq.setUnionId(resp.getString("unionid"));
        WXUserDto.MobileUser mobileUserResp = wxUserService.infoMobileUser(mobileUserReq);
        if (mobileUserResp != null) {
            if (Strings.isNullOrEmpty(minUser.getMobile())) {
                minUser.setMobile(mobileUserResp.getMobile());
            }
        }
        if (minUserResp == null) {
            WXUserDto.MinUser minUserAdd = new WXUserDto.MinUser();
            minUserAdd.setUnionId(resp.getString("unionid"));
            minUserAdd.setMinId(resp.getString("openid"));
            minUserAdd.setAppId(wpUserResp == null ? resp.getString("openid") : wpUserResp.getAppId());
            minUser.setAppId(minUserAdd.getAppId());
            minUser.setOpenId(wpUserResp == null ? "" : wpUserResp.getOpenId());
            wxUserService.addMinUser(minUserAdd);
        } else {
            //关联微信公众号
            if (wpUserResp != null) {
                if (!minUserResp.getAppId().equals(wpUserResp.getAppId())) {
                    WXUserDto.MinUser minUserUp = new WXUserDto.MinUser();
                    minUserUp.setId(minUserResp.getId());
                    minUserUp.setAppId(wpUserResp.getAppId());
                    wxUserService.updateMinUser(minUserUp);
                }
                if (Strings.isNullOrEmpty(wpUserResp.getMinId())) {
                    WXUserDto.WXUser wpUserUp = new WXUserDto.WXUser();
                    wpUserUp.setId(wpUserResp.getId());
                    wpUserUp.setMinId(resp.getString("openid"));
                    wxUserService.update(wpUserUp);
                }
                minUser.setAppId(wpUserResp.getAppId());
                minUser.setOpenId(wpUserResp.getOpenId());
                minUser.setMobile(wpUserResp.getMobile());
            }
            //关联手机
            if (mobileUserResp != null) {
                if (wpUserResp != null && !mobileUserResp.getAppId().equals(wpUserResp.getAppId())) {
                    WXUserDto.MobileUser mobileUserUp = new WXUserDto.MobileUser();
                    mobileUserUp.setId(mobileUserResp.getId());
                    mobileUserUp.setAppId(wpUserResp.getAppId());
                    wxUserService.updateMobileUser(mobileUserUp);
                }
                if (Strings.isNullOrEmpty(minUser.getMobile())) {
                    minUser.setMobile(mobileUserResp.getMobile());
                }
            }

        }

        redisClient.set(minUser.getSessionId(), JSON.toJSONString(minUser), 60 * 60 * 2);*/
        log.info("minLogin return===>{}", minUser);
        return minUser;
    }

}
