/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
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
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */
package org.springblade.core.mp.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springblade.core.tool.utils.BeanUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 分页工具类
 *
 * @author L.cm
 */
public class PageUtil {

	/**
	 * 2个 IPage 转 Page
	 *
	 * @param page   IPage
	 * @param target 需要copy转换的类型
	 * @param <T>    泛型
	 * @return PageResult
	 */
	public static <T> Page<T> toPage(IPage<?> page, Class<T> target) {
		List<T> records = BeanUtil.copy(page.getRecords(), target);
		return toPage(page, records);
	}

	/**
	 * 2个 IPage 转 Page
	 *
	 * @param page    IPage
	 * @param records 转换过的list模型
	 * @param <T>     泛型
	 * @return PageResult
	 */
	public static <T> Page<T> toPage(IPage<?> page, List<T> records) {
		Page<T> pageResult = new Page<>();
		pageResult.setCurrent(page.getCurrent());
		pageResult.setSize(page.getSize());
		pageResult.setPages(page.getPages());
		pageResult.setTotal(page.getTotal());
		pageResult.setRecords(records);
		return pageResult;
	}

	/**
	 * Page 转换
	 *
	 * @param page     IPage
	 * @param function 转换过的函数
	 * @param <T>      泛型
	 * @return PageResult
	 */
	public static <T, R> Page<R> toPage(IPage<T> page, Function<T, R> function) {
		List<R> records = new ArrayList<>();
		for (T record : page.getRecords()) {
			records.add(function.apply(record));
		}
		return toPage(page, records);
	}

}
