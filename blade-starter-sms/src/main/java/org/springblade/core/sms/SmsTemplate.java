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

import org.springblade.core.sms.model.SmsCode;
import org.springblade.core.sms.model.SmsData;
import org.springblade.core.sms.model.SmsInfo;
import org.springblade.core.sms.model.SmsResponse;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * 短信通用封装
 *
 * @author Chill
 */
public interface SmsTemplate {

	/**
	 * 发送短信
	 *
	 * @param smsInfo 短信信息
	 * @return 发送返回
	 */
	default boolean send(SmsInfo smsInfo) {
		return sendMulti(smsInfo.getSmsData(), smsInfo.getPhones());
	}

	/**
	 * 发送短信
	 *
	 * @param smsData 短信内容
	 * @param phone   手机号
	 * @return 发送返回
	 */
	default boolean sendSingle(SmsData smsData, String phone) {
		if (StringUtils.isEmpty(phone)) {
			return Boolean.FALSE;
		}
		return sendMulti(smsData, Collections.singletonList(phone));
	}

	/**
	 * 发送短信
	 *
	 * @param smsData 短信内容
	 * @param phones  手机号列表
	 * @return 发送返回
	 */
	default boolean sendMulti(SmsData smsData, Collection<String> phones) {
		SmsResponse response = sendMessage(smsData, phones);
		return response.isSuccess();
	}

	/**
	 * 发送短信
	 *
	 * @param smsData 短信内容
	 * @param phones  手机号列表
	 * @return 发送返回
	 */
	SmsResponse sendMessage(SmsData smsData, Collection<String> phones);

	/**
	 * 发送验证码
	 *
	 * @param smsData 短信内容
	 * @param phone   手机号
	 * @return 发送返回
	 */
	SmsCode sendValidate(SmsData smsData, String phone);

	/**
	 * 校验验证码
	 *
	 * @param smsCode 验证码内容
	 * @return 是否校验成功
	 */
	boolean validateMessage(SmsCode smsCode);

}
