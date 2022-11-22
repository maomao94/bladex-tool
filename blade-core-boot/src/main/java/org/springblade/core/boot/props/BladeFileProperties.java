package org.springblade.core.boot.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * BladeFileProperties
 *
 * @author Chill
 */
@Getter
@Setter
@ConfigurationProperties("blade.file")
public class BladeFileProperties {

	/**
	 * 远程上传模式
	 */
	private boolean remoteMode = false;

	/**
	 * 外网地址
	 */
	private String uploadDomain = "http://127.0.0.1:8999";

	/**
	 * 上传下载路径(物理路径)
	 */
	private String remotePath = System.getProperty("user.dir") + "/target/blade";

	/**
	 * 上传路径(相对路径)
	 */
	private String uploadPath = "/upload";

	/**
	 * 下载路径
	 */
	private String downloadPath = "/download";

	/**
	 * 图片压缩
	 */
	private Boolean compress = false;

	/**
	 * 图片压缩比例
	 */
	private Double compressScale = 2.00;

	/**
	 * 图片缩放选择:true放大;false缩小
	 */
	private Boolean compressFlag = false;

	/**
	 * 项目物理路径
	 */
	private String realPath = System.getProperty("user.dir");

	/**
	 * 项目相对路径
	 */
	private String contextPath = "/";


	public String getUploadRealPath() {
		return (remoteMode ? remotePath : realPath) + uploadPath;
	}

	public String getUploadCtxPath() {
		return contextPath + uploadPath;
	}

}
