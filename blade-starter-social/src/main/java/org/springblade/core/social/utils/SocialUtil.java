/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springblade.core.social.utils;

import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.request.*;
import org.springblade.core.social.props.SocialProperties;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.Objects;

/**
 * SocialUtil
 *
 * @author Chill
 */
public class SocialUtil {

	/**
	 * 自定义state缓存
	 */
	private static AuthStateCache authStateCache;

	public static AuthStateCache getAuthStateCache() {
		if (authStateCache == null) {
			authStateCache = SpringUtil.getBean(AuthStateCache.class);
		}
		return authStateCache;
	}

	/**
	 * 根据具体的授权来源，获取授权请求工具类
	 *
	 * @param source 授权来源
	 * @return AuthRequest
	 */
	public static AuthRequest getAuthRequest(String source, SocialProperties socialProperties) {
		AuthDefaultSource authSource = Objects.requireNonNull(AuthDefaultSource.valueOf(source.toUpperCase()));
		AuthConfig authConfig = socialProperties.getOauth().get(authSource);
		if (authConfig == null) {
			throw new AuthException("未获取到有效的Auth配置");
		}
		AuthRequest authRequest = null;
		switch (authSource) {
			case GITHUB:
				authRequest = new AuthGithubRequest(authConfig, getAuthStateCache());
				break;
			case GITEE:
				authRequest = new AuthGiteeRequest(authConfig, getAuthStateCache());
				break;
			case OSCHINA:
				authRequest = new AuthOschinaRequest(authConfig, getAuthStateCache());
				break;
			case QQ:
				authRequest = new AuthQqRequest(authConfig, getAuthStateCache());
				break;
			case WECHAT_OPEN:
				authRequest = new AuthWeChatOpenRequest(authConfig, getAuthStateCache());
				break;
			case WECHAT_ENTERPRISE:
				authRequest = new AuthWeChatEnterpriseQrcodeRequest(authConfig, getAuthStateCache());
				break;
			case WECHAT_ENTERPRISE_WEB:
				authRequest = new AuthWeChatEnterpriseWebRequest(authConfig, getAuthStateCache());
				break;
			case WECHAT_MP:
				authRequest = new AuthWeChatMpRequest(authConfig, getAuthStateCache());
				break;
			case DINGTALK:
				authRequest = new AuthDingTalkRequest(authConfig, getAuthStateCache());
				break;
			case ALIPAY:
				// 支付宝在创建回调地址时，不允许使用localhost或者127.0.0.1，所以这儿的回调地址使用的局域网内的ip
				authRequest = new AuthAlipayRequest(authConfig, getAuthStateCache());
				break;
			case BAIDU:
				authRequest = new AuthBaiduRequest(authConfig, getAuthStateCache());
				break;
			case WEIBO:
				authRequest = new AuthWeiboRequest(authConfig, getAuthStateCache());
				break;
			case CODING:
				authRequest = new AuthCodingRequest(authConfig, getAuthStateCache());
				break;
			case CSDN:
				authRequest = new AuthCsdnRequest(authConfig, getAuthStateCache());
				break;
			case TAOBAO:
				authRequest = new AuthTaobaoRequest(authConfig, getAuthStateCache());
				break;
			case GOOGLE:
				authRequest = new AuthGoogleRequest(authConfig, getAuthStateCache());
				break;
			case FACEBOOK:
				authRequest = new AuthFacebookRequest(authConfig, getAuthStateCache());
				break;
			case DOUYIN:
				authRequest = new AuthDouyinRequest(authConfig, getAuthStateCache());
				break;
			case LINKEDIN:
				authRequest = new AuthLinkedinRequest(authConfig, getAuthStateCache());
				break;
			case MICROSOFT:
				authRequest = new AuthMicrosoftRequest(authConfig, getAuthStateCache());
				break;
			case MI:
				authRequest = new AuthMiRequest(authConfig, getAuthStateCache());
				break;
			case TOUTIAO:
				authRequest = new AuthToutiaoRequest(authConfig, getAuthStateCache());
				break;
			case TEAMBITION:
				authRequest = new AuthTeambitionRequest(authConfig, getAuthStateCache());
				break;
			case PINTEREST:
				authRequest = new AuthPinterestRequest(authConfig, getAuthStateCache());
				break;
			case RENREN:
				authRequest = new AuthRenrenRequest(authConfig, getAuthStateCache());
				break;
			case STACK_OVERFLOW:
				authRequest = new AuthStackOverflowRequest(authConfig, getAuthStateCache());
				break;
			case HUAWEI:
				authRequest = new AuthHuaweiRequest(authConfig, getAuthStateCache());
				break;
			case KUJIALE:
				authRequest = new AuthKujialeRequest(authConfig, getAuthStateCache());
				break;
			case GITLAB:
				authRequest = new AuthGitlabRequest(authConfig, getAuthStateCache());
				break;
			case MEITUAN:
				authRequest = new AuthMeituanRequest(authConfig, getAuthStateCache());
				break;
			case ELEME:
				authRequest = new AuthElemeRequest(authConfig, getAuthStateCache());
				break;
			case TWITTER:
				authRequest = new AuthTwitterRequest(authConfig, getAuthStateCache());
				break;
			default:
				break;
		}
		if (null == authRequest) {
			throw new AuthException("未获取到有效的Auth配置");
		}
		return authRequest;
	}

}
