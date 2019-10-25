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
 * zyiyuan 下午3:33:55 创建
 */
package com.zxy.zdk.business.containers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.support.TransactionTemplate;

import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.ResultBase;
import com.google.common.collect.Maps;
import com.zxy.zdk.business.containers.interfaces.ZdkBusinessContainer;
import com.zxy.zdk.business.executor.element.ZdkBusinessExecutorElement;
import com.zxy.zdk.business.executor.element.annotation.ZdkInvoke;
import com.zxy.zdk.business.executor.interfaces.ZdkBusinessExecutor;
import com.zxy.zdk.domain.factory.DomainFactory;

import io.jsonwebtoken.lang.Assert;


/**
 *  @category 系统业务执行容器实现抽象类 【 执行容器注册容器 】
 * 
 */
public abstract class ZdkBusinessActiveContainer implements ZdkBusinessContainer {

	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = -2298763017070389905L;
	
	/** 日志 */
	protected static Logger logger = LoggerFactory.getLogger(ZdkBusinessActiveContainer.class);

	
	/** SOA分布式结构，模块系统名 */
	protected String moduleName;
	
	/**  领域驱动工厂  */
	@Autowired
	protected DomainFactory domainFactory;
	
	/** spring的事务模板 */
	@Autowired
	protected TransactionTemplate transactionTemplate;
	
	/**  spring处理上下文  */
	protected ApplicationContext applicationContext;
	
	/**  spring手动注入bean工厂  */
	protected AutowireCapableBeanFactory autowireFactory;
	
	/**  应用系统业务处理容器   */
	protected Map< String , ZdkBusinessExecutorElement > elements = Maps.newHashMap();
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
		
		this.autowireFactory = applicationContext.getAutowireCapableBeanFactory();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		logger.info("********************************【】系统 开始加载业务执行器：******************************" , this.moduleName );
		
		/**
		 * TODO 参数解释
		 * 	
		 * 1. ZdkBusinessExecutor.class  用于表示这一类型的所有bean
		 *  
		 *  2. 是否只找出单例的
		 *  
		 *  3. 是否支持懒惰式加载
		 */
		Map<String , ZdkBusinessExecutor> executors = this.applicationContext.getBeansOfType(ZdkBusinessExecutor.class, false, true);
		
		executors.forEach( ( beanName, executor ) -> this.registerExecutorElement(executor) ); 
		
	}
	
	@Override
	public <ORDER extends OrderBase, RESULT extends ResultBase> void registerExecutorElement(ZdkBusinessExecutor<ORDER, RESULT> executor) {
		
		if( null == this.elements ) {
			
			this.elements = Maps.newHashMap();
		}
		
		ZdkBusinessExecutorElement element = this.createZdkBusinessExecutorElement( executor );
	
		this.elements.put( element.getServiceName(),  element );
		
		logger.info(String.format("********************************************** [%s] : 执行器注册容器完成！********************************************** ", executor.getBeanName()));
	}
	
	private ZdkBusinessExecutorElement createZdkBusinessExecutorElement(ZdkBusinessExecutor  executor) {
		
		Class< ? extends ZdkBusinessExecutor> executorClz = executor.getClass();
		
		if( executorClz.isAnnotationPresent(ZdkInvoke.class) ) {
			
			ZdkInvoke invoke = executorClz.getAnnotation(ZdkInvoke.class);
			
			ZdkBusinessExecutorElement element = new ZdkBusinessExecutorElement();
			
			element.setServiceName(invoke.serviceName());
			
			element.setValidateGroup(invoke.validateGroup());
			
			element.setEntityType(invoke.entityType());
			
			element.setExecutor(executor);
			
			element.setBeanName(executor.getBeanName());
			
			element.setActionMemo(invoke.actionMemo());
			
			element.setModuleName(this.moduleName); 
			
			element.setTransaction(invoke.isTransaction());
			
			this.autowireFactory.autowireBeanProperties(element, 0, false);
			
			Assert.notNull(element , String.format("executeElement :[%s] must be not null !", executor.getBeanName()) ); 
			
			Assert.notNull(element.getServiceName() , String.format("executeElement.serviceName :[%s] must be not null !", executor.getBeanName()) );
			
			Assert.notNull(element.getEntityType() , String.format("executeElement.entityType :[%s] must be not null !", executor.getBeanName()) );
			
			Assert.notNull(element.getActionMemo() , String.format("executeElement.actionMemo :[%s] must be not null !", executor.getBeanName()) );
			
			return element;
		}
		
		throw new RuntimeException(String.format( "【%s】执行器未配置，请联系系统研发人员！", executorClz.getName()) );
	}
}
