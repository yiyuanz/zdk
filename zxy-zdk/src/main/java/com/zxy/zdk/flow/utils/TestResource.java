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
 * zyiyuan 8:54:45 AM 创建
 */
package com.zxy.zdk.flow.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import com.zxy.zdk.flow.contants.FlowContants;

public class TestResource {
	
	
	
	
	public static void main( String[] args ) throws  Exception {
		
		
		try {
			
			Resource res = new ByteArrayResource("zhangyiyuan".getBytes());
			
			
			XmlDocumentReader.validatorResouce(FlowContants.FLOW_XSD_URL, res);
			
		}catch( Exception ex ) {
			
			ex.printStackTrace();
			
		}finally {
			
			
		}
		
		
		
		
		
	}
}
