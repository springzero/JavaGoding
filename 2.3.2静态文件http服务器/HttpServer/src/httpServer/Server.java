package httpServer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import sun.net.www.protocol.gopher.Handler;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月23日 下午12:48:37
 * 类说明
 */
public class Server {
	private int num;
	private int port;
	private boolean running;
	private String root;
	private ServerSocket serverSocket;
	
	public Server(){
		num = 0;
		running = false;
		SAXParserFactory saxPF = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try {
			InputStream in = new FileInputStream("server.xml");
			saxParser = saxPF.newSAXParser();
			saxParser.parse(in, new Handler());
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
				new Thread(new requestHandler(incomingSocket, root)).start();
				num++;
			} catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println("System deal " + num + " request");
		}
		System.out.println("System is down");
	}
	
	private class Handler extends DefaultHandler {
		private boolean isPort;
		private boolean isRoot;
		
		public Handler(){
			isPort = false;
			isRoot = false;
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
	
	public void shutDown() {
		running = false;
	}
	
	private class requestHandler implements Runnable {
		Socket incomingSocket;
		String root;
		
		public requestHandler(Socket incomingSocket, String root) {
			this.incomingSocket = incomingSocket;
			this.root = root;
		}
		
		@Override
		public void run() {
			new HttpSolver(incomingSocket,root).serverDoing();;
		}
	}

}
