package com.springzero.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月24日 下午2:18:39
 * 类说明
 */
public class MyXmlHandler extends DefaultHandler {
	private boolean isPort;
	private boolean isRoot;
	private int port;
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
		} else if(qName.equals("server")) {
			//待定，因为没有什么要处理的，小不和谐的样子
		} else {
			System.out.println("errot xml element");
		}
	}
}
