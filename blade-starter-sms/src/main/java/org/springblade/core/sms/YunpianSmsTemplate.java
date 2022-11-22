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

import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.constant.Code;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsBatchSend;
import lombok.AllArgsConstructor;
import org.springblade.core.redis.cache.BladeRedis;
import org.springblade.core.sms.model.SmsCode;
import org.springblade.core.sms.model.SmsData;
import org.springblade.core.sms.model.SmsResponse;
import org.springblade.core.sms.props.SmsProperties;
import org.springblade.core.tool.support.Kv;
import org.springblade.core.tool.utils.PlaceholderUtil;
import org.springblade.core.tool.utils.StringPool;
import org.springblade.core.tool.utils.StringUtil;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

import static org.springblade.core.sms.constant.SmsConstant.CAPTCHA_KEY;

/**
 * 云片短信发送类
 *
 * @author Chill
 */
@AllArgsConstructor
public class YunpianSmsTemplate implements SmsTemplate {

	private final SmsProperties smsProperties;
	private final YunpianClient client;
	private final BladeRedis bladeRedis;

	@Override
	public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
		String templateId = smsProperties.getTemplateId();
		// 云片短信模板内容替换, 占位符格式为官方默认的 #code#
		String templateText = PlaceholderUtil.getResolver(StringPool.HASH, StringPool.HASH).resolveByMap(
			templateId, Kv.create().setAll(smsData.getParams())
		);
		Map<String, String> param = client.newParam(2);
		param.put(YunpianClient.MOBILE, StringUtil.join(phones));
		param.put(YunpianClient.TEXT, templateText);
		Result<SmsBatchSend> result = client.sms().multi_send(param);
		return new SmsResponse(result.getCode() == Code.OK, result.getCode(), result.toString());
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
