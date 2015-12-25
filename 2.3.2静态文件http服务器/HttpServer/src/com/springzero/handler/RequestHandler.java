package com.springzero.handler;

import java.net.Socket;

import com.springzero.httpserver.HttpSolver;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月24日 下午2:14:54
 * 类说明
 */
public class RequestHandler implements Runnable {
	Socket incomingSocket;
	String root;

	public RequestHandler(Socket incomingSocket, String root) {
		this.incomingSocket = incomingSocket;
		this.root = root;
	}

	@Override
	public void run() {
		new HttpSolver(incomingSocket,root).server();
	}
}