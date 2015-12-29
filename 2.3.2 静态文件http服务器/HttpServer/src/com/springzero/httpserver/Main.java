package com.springzero.httpserver;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月23日 下午3:14:21
 * 类说明
 */
public class Main {
	public static void main(String agrs[]) {
		//NioServer server = new NioServer();
		BaseServer server = new BaseServer();
		server.serverStart();
	}
}
