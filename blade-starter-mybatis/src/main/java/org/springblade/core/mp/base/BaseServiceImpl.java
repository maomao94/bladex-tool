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
package org.springblade.core.mp.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 业务封装基础类
 *
 * @param <M> mapper
 * @param <T> model
 * @author Chill
 */
@Validated
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

	@Override
	public boolean save(T entity) {
		this.resolveEntity(entity);
		return super.save(entity);
	}

	@Override
	public boolean saveBatch(Collection<T> entityList, int batchSize) {
		entityList.forEach(this::resolveEntity);
		return super.saveBatch(entityList, batchSize);
	}

	@Override
	public boolean updateById(T entity) {
		this.resolveEntity(entity);
		return super.updateById(entity);
	}

	@Override
	public boolean updateBatchById(Collection<T> entityList, int batchSize) {
		entityList.forEach(this::resolveEntity);
		return super.updateBatchById(entityList, batchSize);
	}

	@Override
	public boolean saveOrUpdate(T entity) {
		if (entity.getId() == null) {
			return this.save(entity);
		} else {
			return this.updateById(entity);
		}
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
		entityList.forEach(this::resolveEntity);
		return super.saveOrUpdateBatch(entityList, batchSize);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteLogic(@NotEmpty List<Long> ids) {
		BladeUser user = AuthUtil.getUser();
		List<T> list = new ArrayList<>();
		ids.forEach(id -> {
			T entity = BeanUtil.newInstance(currentModelClass());
			if (user != null) {
				entity.setUpdateUser(user.getUserId());
			}
			entity.setUpdateTime(DateUtil.now());
			entity.setId(id);
			list.add(entity);
		});
		return super.updateBatchById(list) && super.removeByIds(ids);
	}

	@Override
	public boolean changeStatus(@NotEmpty List<Long> ids, Integer status) {
		BladeUser user = AuthUtil.getUser();
		List<T> list = new ArrayList<>();
		ids.forEach(id -> {
			T entity = BeanUtil.newInstance(currentModelClass());
			if (user != null) {
				entity.setUpdateUser(user.getUserId());
			}
			entity.setUpdateTime(DateUtil.now());
			entity.setId(id);
			entity.setStatus(status);
			list.add(entity);
		});
		return super.updateBatchById(list);
	}

	@SneakyThrows
	private void resolveEntity(T entity) {
		BladeUser user = AuthUtil.getUser();
		Date now = DateUtil.now();
		if (entity.getId() == null) {
			// 处理新增逻辑
			if (user != null) {
				entity.setCreateUser(user.getUserId());
				entity.setCreateDept(Func.firstLong(user.getDeptId()));
				entity.setUpdateUser(user.getUserId());
			}
			if (entity.getStatus() == null) {
				entity.setStatus(BladeConstant.DB_STATUS_NORMAL);
			}
			entity.setCreateTime(now);
		} else if (user != null) {
			// 处理修改逻辑
			entity.setUpdateUser(user.getUserId());
		}
		// 处理通用逻辑
		entity.setUpdateTime(now);
		entity.setIsDeleted(BladeConstant.DB_NOT_DELETED);
		// 处理多租户逻辑，若字段值为空，则不进行操作
		Field field = ReflectUtil.getField(entity.getClass(), BladeConstant.DB_TENANT_KEY);
		if (ObjectUtil.isNotEmpty(field)) {
			Method getTenantId = ClassUtil.getMethod(entity.getClass(), BladeConstant.DB_TENANT_KEY_GET_METHOD);
			String tenantId = String.valueOf(getTenantId.invoke(entity));
			if (ObjectUtil.isEmpty(tenantId)) {
				Method setTenantId = ClassUtil.getMethod(entity.getClass(), BladeConstant.DB_TENANT_KEY_SET_METHOD, String.class);
				setTenantId.invoke(entity, (Object) null);
			}
		}
	}

}
