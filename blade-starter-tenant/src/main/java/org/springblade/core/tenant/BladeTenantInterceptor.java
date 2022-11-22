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
package org.springblade.core.tenant;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.StringPool;

import java.util.Arrays;
import java.util.List;

/**
 * 租户拦截器
 *
 * @author Chill
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BladeTenantInterceptor extends TenantLineInnerInterceptor {

	/**
	 * 租户处理器
	 */
	private TenantLineHandler tenantLineHandler;
	/**
	 * 租户配置文件
	 */
	private BladeTenantProperties tenantProperties;
	/**
	 * 超管需要启用租户过滤的表
	 */
	private List<String> adminTenantTables = Arrays.asList("blade_top_menu", "blade_dict_biz");

	@Override
	public void setTenantLineHandler(TenantLineHandler tenantLineHandler) {
		super.setTenantLineHandler(tenantLineHandler);
		this.tenantLineHandler = tenantLineHandler;
	}

	@Override
	protected void processInsert(Insert insert, int index, String sql, Object obj) {
		// 未启用租户增强，则使用原版逻辑
		if (!tenantProperties.getEnhance()) {
			super.processInsert(insert, index, sql, obj);
			return;
		}
		if (tenantLineHandler.ignoreTable(insert.getTable().getName())) {
			// 过滤退出执行
			return;
		}
		List<Column> columns = insert.getColumns();
		if (CollectionUtils.isEmpty(columns)) {
			// 针对不给列名的insert 不处理
			return;
		}
		String tenantIdColumn = tenantLineHandler.getTenantIdColumn();
		if (columns.stream().map(Column::getColumnName).anyMatch(i -> i.equals(tenantIdColumn))) {
			// 针对已给出租户列的insert 不处理
			return;
		}
		columns.add(new Column(tenantLineHandler.getTenantIdColumn()));
		Select select = insert.getSelect();
		if (select != null) {
			this.processInsertSelect(select.getSelectBody());
		} else if (insert.getItemsList() != null) {
			// fixed github pull/295
			ItemsList itemsList = insert.getItemsList();
			if (itemsList instanceof MultiExpressionList) {
				((MultiExpressionList) itemsList).getExpressionLists().forEach(el -> el.getExpressions().add(tenantLineHandler.getTenantId()));
			} else {
				((ExpressionList) itemsList).getExpressions().add(tenantLineHandler.getTenantId());
			}
		} else {
			throw ExceptionUtils.mpe("Failed to process multiple-table update, please exclude the tableName or statementId");
		}
	}

	/**
	 * 处理 PlainSelect
	 */
	@Override
	protected void processPlainSelect(PlainSelect plainSelect) {
		FromItem fromItem = plainSelect.getFromItem();
		Expression where = plainSelect.getWhere();
		processWhereSubSelect(where);
		if (fromItem instanceof Table) {
			Table fromTable = (Table) fromItem;
			if (!tenantLineHandler.ignoreTable(fromTable.getName())) {
				// 若是超管则不进行过滤
				if (!doTenantFilter(fromTable.getName())) {
					//#1186 github
					plainSelect.setWhere(builderExpression(where, fromTable));
				}
			}
		} else {
			processFromItem(fromItem);
		}
		//#3087 github
		List<SelectItem> selectItems = plainSelect.getSelectItems();
		if (selectItems != null && selectItems.size() > 0) {
			selectItems.forEach(this::processSelectItem);
		}
		List<Join> joins = plainSelect.getJoins();
		if (joins != null && joins.size() > 0) {
			joins.forEach(j -> {
				processJoin(j);
				processFromItem(j.getRightItem());
			});
		}
	}

	/**
	 * update 语句处理
	 */
	@Override
	protected void processUpdate(Update update, int index, String sql, Object obj) {
		final Table table = update.getTable();
		if (tenantLineHandler.ignoreTable(table.getName())) {
			// 过滤退出执行
			return;
		}
		if (doTenantFilter(table.getName())) {
			// 过滤退出执行
			return;
		}
		update.setWhere(this.andExpression(table, update.getWhere()));
	}

	/**
	 * delete 语句处理
	 */
	@Override
	protected void processDelete(Delete delete, int index, String sql, Object obj) {
		final Table table = delete.getTable();
		if (tenantLineHandler.ignoreTable(table.getName())) {
			// 过滤退出执行
			return;
		}
		if (doTenantFilter(table.getName())) {
			// 过滤退出执行
			return;
		}
		delete.setWhere(this.andExpression(table, delete.getWhere()));
	}

	/**
	 * delete update 语句 where 处理
	 */
	@Override
	protected BinaryExpression andExpression(Table table, Expression where) {
		EqualsTo equalsTo = builderEqualsTo(table);
		if (null != where) {
			if (where instanceof OrExpression) {
				return new AndExpression(equalsTo, new Parenthesis(where));
			} else {
				return new AndExpression(equalsTo, where);
			}
		}
		return equalsTo;
	}

	/**
	 * 增强插件使超级管理员可以看到所有租户数据
	 */
	@Override
	protected Expression builderExpression(Expression currentExpression, Table table) {
		EqualsTo equalsTo = builderEqualsTo(table);
		if (currentExpression == null) {
			return equalsTo;
		}
		if (currentExpression instanceof OrExpression) {
			return new AndExpression(new Parenthesis(currentExpression), equalsTo);
		} else {
			return new AndExpression(currentExpression, equalsTo);
		}
	}

	/**
	 * 子查询解析支持
	 */
	protected void processSelectItem(SelectItem selectItem) {
		if (selectItem instanceof SelectExpressionItem) {
			SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
			if (selectExpressionItem.getExpression() instanceof SubSelect) {
				processSelectBody(((SubSelect) selectExpressionItem.getExpression()).getSelectBody());
			} else if (selectExpressionItem.getExpression() instanceof Function) {
				ExpressionList parameters = ((Function) selectExpressionItem.getExpression()).getParameters();
				if (parameters != null) {
					parameters.getExpressions().forEach(expression -> {
						if (expression instanceof SubSelect) {
							processSelectBody(((SubSelect) expression).getSelectBody());
						}
					});
				}
			}
		}
	}

	/**
	 * 获取条件表达式
	 *
	 * @param table 表
	 */
	public EqualsTo builderEqualsTo(Table table) {
		//获得条件表达式
		EqualsTo equalsTo = new EqualsTo();
		Expression leftExpression = this.getAliasColumn(table);
		Expression rightExpression = tenantLineHandler.getTenantId();
		// 若是超管则不进行过滤
		if (doTenantFilter(table.getName())) {
			leftExpression = rightExpression = new StringValue(StringPool.ONE);
		}
		equalsTo.setLeftExpression(leftExpression);
		equalsTo.setRightExpression(rightExpression);
		return equalsTo;
	}

	/**
	 * 判断当前操作是否需要进行过滤
	 *
	 * @param tableName 表名
	 */
	public boolean doTenantFilter(String tableName) {
		return AuthUtil.isAdministrator() && !adminTenantTables.contains(tableName);
	}

}
