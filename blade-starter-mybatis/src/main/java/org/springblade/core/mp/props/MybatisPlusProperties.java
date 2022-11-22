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
package org.springblade.core.mp.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * MybatisPlus配置类
 *
 * @author Chill
 */
@Data
@ConfigurationProperties(prefix = "blade.mybatis-plus")
public class MybatisPlusProperties {

	/**
	 * 开启租户模式
	 */
	private Boolean tenantMode = true;

	/**
	 * 开启sql日志
	 */
	private Boolean sqlLog = true;

	/**
	 * sql日志忽略打印关键字
	 */
	private List<String> sqlLogExclude = new ArrayList<>();

	/**
	 * 分页最大数
	 */
	private Long pageLimit = 500L;

	/**
	 * 溢出总页数后是否进行处理
	 */
	protected Boolean overflow = false;

	/**
	 * join优化
	 */
	private Boolean optimizeJoin = false;

}
