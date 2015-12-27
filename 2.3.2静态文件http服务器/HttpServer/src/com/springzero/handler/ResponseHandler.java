package com.springzero.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.springzero.httpserver.HttpMessage;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月24日 下午2:49:28
 * 类说明 	生成响应页面
 */
public class ResponseHandler {
	private OutputStream out;
	private StringBuilder result;
	private SocketChannel channel;
	private boolean isNio = false;
	
	public ResponseHandler(OutputStream out) {
		this.out = out;
		result = new StringBuilder();
	}
	
	public ResponseHandler(SocketChannel channel) {
		this.channel = channel;
		result = new StringBuilder();
		isNio = true;
	}

	public void send_200_status(HttpMessage httpMessage, byte[] fileContents) {
		result.append("HTTP/1.1 200 OK\r\n");
		result.append("Cache-Control: no-cache\r\n");
		if(httpMessage.targetFile.endsWith("html")) {
			result.append("Content-Type: text/html; charset=utf-8\r\n");
		} else if(httpMessage.targetFile.endsWith("jpg")) {
			result.append("Content-Type: image/jpeg\r\n");
		}
		result.append("Last-Modified:  " + httpMessage.lastModified +"\r\n");
		result.append("\r\n");
		write(isNio,fileContents);
	}
	
	public void send_304_status() {
		result.append("HTTP/1.1 304 Not Modified\r\n");
		result.append("\r\n");
		write(isNio,null);
	}
	
	public void send_400_status() {
		result.append("HTTP/1.1 400 bad request\r\n"
				+ "Connection: close\r\n");
		result.append("\r\n");
		write(isNio,null);
	}
	
	/**
	 * 发送响应包
	 * @param isNio	是否为NIO【非阻塞IO】
	 * @param fileContents	文件内容 若没有请输入null
	 */
	public void write(boolean isNio, byte[] fileContents) {
		if(isNio) {
			try {
				channel.write(ByteBuffer.wrap(result.toString().getBytes()));
				if(fileContents != null) {
					channel.write(ByteBuffer.wrap(fileContents));
				}
			} catch (IOException e) {
				System.out.println("nio write error");
				e.printStackTrace();
			}
		} else {
			try {
				out.write(result.toString().getBytes());
				out.write(fileContents);
				out.flush();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
