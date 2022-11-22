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
package org.springblade.core.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import lombok.AllArgsConstructor;
import org.springblade.core.redis.cache.BladeRedis;
import org.springblade.core.sms.model.SmsCode;
import org.springblade.core.sms.model.SmsData;
import org.springblade.core.sms.model.SmsResponse;
import org.springblade.core.sms.props.SmsProperties;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.StringPool;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

import static org.springblade.core.sms.constant.SmsConstant.CAPTCHA_KEY;

/**
 * 阿里云短信发送类
 *
 * @author Chill
 */
@AllArgsConstructor
public class AliSmsTemplate implements SmsTemplate {

	private static final int SUCCESS = 200;
	private static final String FAIL = "fail";
	private static final String OK = "ok";
	private static final String DOMAIN = "dysmsapi.aliyuncs.com";
	private static final String VERSION = "2017-05-25";
	private static final String ACTION = "SendSms";

	private final SmsProperties smsProperties;
	private final IAcsClient acsClient;
	private final BladeRedis bladeRedis;

	@Override
	public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain(DOMAIN);
		request.setSysVersion(VERSION);
		request.setSysAction(ACTION);
		request.putQueryParameter("PhoneNumbers", StringUtil.join(phones));
		request.putQueryParameter("TemplateCode", smsProperties.getTemplateId());
		request.putQueryParameter("TemplateParam", JsonUtil.toJson(smsData.getParams()));
		request.putQueryParameter("SignName", smsProperties.getSignName());
		try {
			CommonResponse response = acsClient.getCommonResponse(request);
			Map<String, Object> data = JsonUtil.toMap(response.getData());
			String code = FAIL;
			if (data != null) {
				code = String.valueOf(data.get("Code"));
			}
			return new SmsResponse(response.getHttpStatus() == SUCCESS && code.equalsIgnoreCase(OK), response.getHttpStatus(), response.getData());
		} catch (ClientException e) {
			e.printStackTrace();
			return new SmsResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
	}

	@Override
	public SmsCode sendValidate(SmsData smsData, String phone) {
		SmsCode smsCode = new SmsCode();
		boolean temp = sendSingle(smsData, phone);
		if (temp && StringUtil.isNotBlank(smsData.getKey())) {
			String id = StringUtil.randomUUID();
			String value = smsData.getParams().get(smsData.getKey());
			bladeRedis.setEx(CAPTCHA_KEY + phone + StringPool.COLON + id, value, Duration.ofMinutes(30));
			smsCode.setId(id).setValue(value);
		} else {
			smsCode.setSuccess(Boolean.FALSE);
		}
		return smsCode;
	}

	@Override
	public boolean validateMessage(SmsCode smsCode) {
		String id = smsCode.getId();
		String value = smsCode.getValue();
		String cache = bladeRedis.get(CAPTCHA_KEY + smsCode.getPhone() + StringPool.COLON + id);
		if (StringUtil.isNotBlank(value) && StringUtil.equalsIgnoreCase(cache, value)) {
			bladeRedis.del(CAPTCHA_KEY + id);
			return true;
		}
		return false;
	}

}
