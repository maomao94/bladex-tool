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
package org.springblade.core.report.provider;

import com.bstek.ureport.UReportPropertyPlaceholderConfigurer;
import org.springblade.core.report.props.ReportProperties;

import java.util.Properties;

/**
 * UReport自定义配置
 *
 * @author Chill
 */
public class ReportPlaceholderProvider extends UReportPropertyPlaceholderConfigurer {

	public ReportPlaceholderProvider(ReportProperties properties) {
		Properties props = new Properties();
		props.setProperty("ureport.disableHttpSessionReportCache", properties.getDisableHttpSessionReportCache().toString());
		props.setProperty("ureport.disableFileProvider", properties.getDisableFileProvider().toString());
		props.setProperty("ureport.fileStoreDir", properties.getFileStoreDir());
		props.setProperty("ureport.debug", properties.getDebug().toString());
		this.setProperties(props);
	}

}
