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
 * zyiyuan 下午5:48:55 创建
 */
package com.zxy.zdk.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zxy.zdk.business.containers.ZdkBusinessCommonContainer;
import com.zxy.zdk.business.containers.interfaces.ZdkBusinessContainer;
import com.zxy.zdk.chain.InvokeCommandChain;
import com.zxy.zdk.domain.factory.DomainFactory;
import com.zxy.zdk.flow.repertory.FlowBaseActionCreator;
import com.zxy.zdk.flow.repertory.FlowDocumentReader;
import com.zxy.zdk.flow.service.FlowEngineDomainService;
import com.zxy.zdk.flow.service.impl.FlowEngineDomainServiceImpl;


@SuppressWarnings("all")
@Configuration
@EnableConfigurationProperties({ZdkProperties.class})
@ConditionalOnProperty(value = {"cnvex.mdk.autoconfig.enable"}, matchIfMissing = true)
public class ZdkAutoConfig implements ApplicationContextAware {

	
	@Autowired
	private ZdkProperties zdkProperties;
	
	
	private ApplicationContext context;
	
	
	private AutowireCapableBeanFactory autowireFactory;
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.context = applicationContext;
		
		this.autowireFactory = applicationContext.getAutowireCapableBeanFactory();
	}
	
	/**
	 * @category 实体工厂 
	 */
	@Bean
	public DomainFactory domainFactory(){
		
		return  new DomainFactory();
	}
	
	/**
	 * @category 活动记录集 
	 */
	@Bean
	@ConditionalOnProperty({"cnvex.mdk.autoconfig.moduleName"})
	public ZdkBusinessContainer zdkBusinessContainer() {
		
		String moduleName = zdkProperties.getModuleName();
		
		return new ZdkBusinessCommonContainer(moduleName);
	}
	
	/**
	 * @category 责任链 
	 */
	@Bean
	public InvokeCommandChain invokeCommandChain() {
		
		return new InvokeCommandChain();
	}
	
	/**
	 * @category 流程引擎 流程元素构建器
	 */
	@Bean
	public FlowBaseActionCreator flowBaseActionCreator() {
		
		return new FlowBaseActionCreator();
	}
	
	/**
	 * @category 流程引擎  流程图文件解析器
	 */
	@Bean
	public FlowDocumentReader flowDocumentReader() {
		
		return new FlowDocumentReader();
	}
	
	/**
	 * @category 流程引擎  领域服务
	 */
	@Bean
	@ConditionalOnProperty({"cnvex.mdk.autoconfig.moduleName"})
	public FlowEngineDomainService FlowEngineDomainService() {
		
		FlowEngineDomainService service = new FlowEngineDomainServiceImpl();
		
		String flowFilesUrls = zdkProperties.getFlowFiles();
		
		service.intoUrlPartten( flowFilesUrls );
		
		return service;
	}
}
