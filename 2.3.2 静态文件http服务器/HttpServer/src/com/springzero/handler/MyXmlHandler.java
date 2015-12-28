package com.springzero.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月24日 下午2:18:39
 * 类说明		处理server.xml
 */
public class MyXmlHandler extends DefaultHandler {
	private boolean isPort;
	private boolean isRoot;
	private boolean isCorePoolSize;
	private boolean isMaximumPoolSize;
	private boolean isKeepAliveTime;
	private int port;
	private int corePoolSize;
	private int maximumPoolSize;
	private long keepAliveTime;
	private String root;
	
	public MyXmlHandler(){
		isPort = false;
		isRoot = false;
	}
	
	public int getPort() {
		return port;
	}

	public String getRoot() {
		return root;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		String temp = new String(ch,start,length);
		if(isPort) {
			port = Integer.parseInt(temp);
			isPort = false;
		} else if(isRoot) {
			root = temp;
			isRoot = false;
		} else if(isCorePoolSize) {
			corePoolSize = Integer.parseInt(temp);
			isCorePoolSize = false;
		} else if(isMaximumPoolSize) {
			maximumPoolSize = Integer.parseInt(temp);
			isMaximumPoolSize = false;
		} else if(isKeepAliveTime) {
			keepAliveTime = Long.parseLong(temp);
			isKeepAliveTime = false;
		}
	};
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(qName.equals("port")) {
			isPort = true;
		} else if(qName.equals("root")) {
			isRoot = true;
		} else if(qName.equals("corePoolSize")) {
			isCorePoolSize = true;
		} else if(qName.equals("maximumPoolSize")) {
			isMaximumPoolSize = true;
		} else if(qName.equals("keepAliveTime")) {
			isKeepAliveTime = true;
		} else if(qName.equals("server") || qName.equals("ThreadPool")) {
			//待定，因为没有什么要处理的，小不和谐的样子
		} else {
			System.out.println("errot xml element");
		}
	}
}
