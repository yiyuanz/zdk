/*
 * www.cnvex.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 *                    _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                   O\  =  /O
 *                ____/`---'\____
 *              .'  \\|     |//  `.
 *             /  \\|||  :  |||//  \
 *            /  _||||| -:- |||||-  \
 *            |   | \\\  -  /// |   |
 *            | \_|  ''\---/''  |   |
 *            \  .-\__  `-`  ___/-. /
 *          ___`. .'  /--.--\  `. . __
 *       ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 *  ======`-.____`-.___\_____/___.-`____.-'======
 *                     `=---='
 *  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *           佛祖保佑       永无BUG
 */

/*
 * 修订记录：
 * zyiyuan 下午5:40:54 创建
 */
package com.zxy.zdk.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;


/**
 * @category 自定义zdk的组件所需配置 
 * 
 * 
 */
@ConfigurationProperties( prefix = "com.zdk.autoconfig" )
public class ZdkProperties implements InitializingBean {

	public static final String PREFIX = "com.zdk.autoconfig";
	
	/** 是否打开自动配置 */
	private boolean enable = true;
	
	/** SOA分布式系统结构下， 系统模块名 */
	private String  moduleName;
	
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof ZdkProperties)) {
			return false;
		} else {
			ZdkProperties other = (ZdkProperties) o;
			if (!other.canEqual(this)) {
				return false;
			} else if (this.isEnable() != other.isEnable()) {
				return false;
			}  else {
				return true;
			}
		}
	}

	protected boolean canEqual(Object other) {
		return other instanceof ZdkProperties;
	}

	public String toString() {
		return "ExecutorProperties(enable=" + this.isEnable() + ", moduleName=" + this.getModuleName();
	}
	
	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		if (this.enable) {
			Assert.hasText(this.moduleName, "执行容器必须指定模块名称");
		}
	}
}
