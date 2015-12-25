package com.springzero.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Scanner;

import com.springzero.handler.ResponseHandler;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月23日 下午1:54:28
 * 类说明
 */
public class HttpSolver {
	private Socket incomingSocket;
	private SelectionKey key;
	private HttpMessage httpMessage = new HttpMessage();
	private ByteBuffer buf = ByteBuffer.allocate(512);;
	
	public HttpSolver(Socket incomingSocket, String root) {
		this.incomingSocket = incomingSocket;
		httpMessage.root = root;
	}
	
	public HttpSolver(SelectionKey key, String root) {
		this.key = key;
		httpMessage.root = root;
	}
	
	/**
	 * Nio server
	 * @throws IOException 
	 */
	public void serverOfNio() throws IOException {
		this.buf.clear();
		SocketChannel channel = (SocketChannel) key.channel();
		int count = channel.read(buf);
		
		if(count == -1) {
			key.channel().close();
			key.cancel();
			return;
		}
		String inputTemp = new String(this.buf.array()).trim(); 
		System.out.println("您的输入为：" + inputTemp);
		inputTemp = inputTemp + "\r\n" + "" +"\r\n";
		String[] inputArrayTemp = inputTemp.split("\r\n");
		int length = inputArrayTemp.length+1;
		String[] inputArray = new String[length];
		for(int i = 0 ; i < length-1; i++) {
			inputArray[i] = inputArrayTemp[i];
		}
		inputArray[length-1] = "";
		
		
		
		for (String input : inputArray) {
			String splitResult[] = null;
			if(input.startsWith("GET") || input.startsWith("get")) {	//input= "GET /servlet/default.jsp HTTP/1.1"
				splitResult = input.split(new String(" "));
				if(!splitResult[2].equals("HTTP/1.1")) {
					new ResponseHandler(channel).send_400_status();
					incomingSocket.close();
					break;
				}
				if(splitResult[1].equals("/")) {
					httpMessage.targetFile = "index.html";
				} else {
					httpMessage.targetFile = splitResult[1].substring(1,splitResult[1].length()); //这里就是处理链接地址的地方，很显然现在写的太简单了
				}
			} else {	//Accept: text/plain; text/html 处理这一类
				splitResult = input.split(new String(":"));
				switch (splitResult[0]) {
					case "If-Modified-Since":
							httpMessage.If_Modified_Since = true;
							httpMessage.If_Modified_SinceString = splitResult[1].trim();
							break;
					case "":
							new GetDisk(httpMessage,channel).work();
							channel.close();
							break;
					default:
							break;
				}
			}
		}
		
	}
	
	/**
	 * 默认 BIO server
	 */
	public void server() {
		Scanner in = null;
		OutputStream out = null;
		
		try {
			in = new Scanner(incomingSocket.getInputStream());
			out = incomingSocket.getOutputStream();
			while(in.hasNextLine()) {
				String input = in.nextLine();
				String splitResult[] = null;
				if(input.startsWith("GET") || input.startsWith("get")) {	//input= "GET /servlet/default.jsp HTTP/1.1"
					splitResult = input.split(new String(" "));
					if(!splitResult[2].equals("HTTP/1.1")) {
						new ResponseHandler(out).send_400_status();
						incomingSocket.close();
						break;
					}
					if(splitResult[1].equals("/")) {
						httpMessage.targetFile = "index.html";
					} else {
						httpMessage.targetFile = splitResult[1].substring(1,splitResult[1].length()); //这里就是处理链接地址的地方，很显然现在写的太简单了
					}
				} else {	//Accept: text/plain; text/html 处理这一类
					splitResult = input.split(new String(":"));
					switch (splitResult[0]) {
						case "If-Modified-Since":
								httpMessage.If_Modified_Since = true;
								httpMessage.If_Modified_SinceString = splitResult[1].trim();
								break;
						case "":
								new GetDisk(httpMessage,out).work();
								incomingSocket.close();
								break;
						default:
								break;
					}
				}
			}
		} catch(IOException e) {
			System.out.println("socket io exception");
			e.printStackTrace();
		} finally {
			try {
				if(in != null) {
					in.close();
				}
				if(out != null) {
					out.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
