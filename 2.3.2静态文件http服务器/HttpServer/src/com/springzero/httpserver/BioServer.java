package com.springzero.httpserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.springzero.handler.MyXmlHandler;
import com.springzero.handler.RequestHandler;

import sun.net.www.protocol.gopher.Handler;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月23日 下午12:48:37
 * 类说明
 */
public class BioServer {
	private int requestNum;
	private int port;
	private boolean running;
	private String root;
	private ServerSocket serverSocket;
	private ThreadPoolExecutor  threadPool;
	
	public BioServer(){
		serverSystemInit();
	}
	
	public void serverSystemInit() {
		requestNum = 0;
		running = false;
		SAXParserFactory saxPF = SAXParserFactory.newInstance();
		SAXParser saxParser;
		MyXmlHandler myXmlHandler = new MyXmlHandler();
		try {
			InputStream in = new FileInputStream("server.xml");
			saxParser = saxPF.newSAXParser();
			saxParser.parse(in, myXmlHandler);
			this.port = myXmlHandler.getPort();
			this.root = myXmlHandler.getRoot();
			threadPool = new ThreadPoolExecutor(myXmlHandler.getCorePoolSize(), myXmlHandler.getMaximumPoolSize(),
					myXmlHandler.getKeepAliveTime(),TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3));
		} catch(FileNotFoundException e) {
			System.out.println("server.xml not find");
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println("parse server.xml error");
			e.printStackTrace();
		}
	}
	
	public void serverStart() {
		try {
			serverSocket = new ServerSocket(port);
		} catch(IOException e) {
			System.out.println("ServerSocket cann't establish");
			e.printStackTrace();
		}
		running = true;
		System.out.println("HttpServer is runing ~~");
		while(running) {
			try {
				Socket incomingSocket = serverSocket.accept();
				//TODO 在这里引进线程池，而不是直接new个线程
				threadPool.execute(new RequestHandler(incomingSocket,root));  
				requestNum++;
			} catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println("System deal " + requestNum + " request");
		}
		System.out.println("System is down");
	}
	
	public void shutDown() {
		running = false;
	}
	
}
