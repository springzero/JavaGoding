package com.springzero.handler;

import java.io.IOException;
import java.io.OutputStream;

import com.springzero.httpserver.HttpMessage;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月24日 下午2:49:28
 * 类说明
 */
public class ResponseHandler {
	private OutputStream out;
	private StringBuilder result;
	
	public ResponseHandler(OutputStream out) {
		this.out = out;
		result = new StringBuilder();
	}

	public void send_200_status(HttpMessage httpMessage, byte[] fileContents) {
		result.append("HTTP/1.1 200 OK\r\n");
		result.append("Cache-Control: no-cache\r\n");
		result.append("Content-Length:" + fileContents.length+"\r\n");
		if(httpMessage.targetFile.endsWith("html")) {
			result.append("Content-Type: text/html; charset=utf-8\r\n");
		} else if(httpMessage.targetFile.endsWith("jpg")) {
			result.append("Content-Type: image/jpeg\r\n");
		}
		result.append("Last-Modified:  " + httpMessage.lastModified +"\r\n");
		result.append("\r\n");
		try {
			out.write(result.toString().getBytes());
			out.write(fileContents);
			out.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send_304_status() {
		result.append("HTTP/1.1 304 Not Modified\r\n");
		result.append("\r\n");
		try {
			out.write(result.toString().getBytes());
			out.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send_400_status() {
		result.append("HTTP/1.1 400 bad request\r\n"
				+ "Connection: close\r\n");
		result.append("\r\n");
		try {
			out.write(result.toString().getBytes());
			out.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
