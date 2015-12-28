package com.springzero.handler;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SelectionKey;

import com.springzero.httpserver.HttpSolver;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月24日 下午2:14:54
 * 类说明		处理请求
 */
public class RequestHandler implements Runnable {
	private Socket incomingSocket;
	private SelectionKey key;
	private String root;
	private boolean isNio = false;

	public RequestHandler(Socket incomingSocket, String root) {
		this.incomingSocket = incomingSocket;
		this.root = root;
	}

	public RequestHandler(SelectionKey key,String root) {
		this.key = key;
		this.root = root;
		this.isNio = true;
	}
	
	@Override
	public void run() {
		if(isNio) {
			try {
				new HttpSolver(key, root).serverOfNio();
			} catch (IOException e) {
				System.out.println("Nio server error");
				e.printStackTrace();
			}
		} else {
			new HttpSolver(incomingSocket,root).server();
		}
		
	}
}