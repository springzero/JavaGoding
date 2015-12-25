package com.springzero.httpserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.springzero.handler.MyXmlHandler;
import com.springzero.handler.RequestHandler;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月25日 下午2:54:04
 * 类说明
 */
public class NioServer {
	private int requestNum;
	private int port;
	private boolean running;
	private String root;
	private ServerSocket serverSocket;
	private ThreadPoolExecutor  threadPool;
	
	private ServerSocketChannel serverSocketChannel;	//通道
	private SocketChannel socketChannel;							//连接
	private Selector selector;													//选择器
	private ByteBuffer buf = ByteBuffer.allocate(512);		//缓冲区
	
	public NioServer(){
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
			this.selector = SelectorProvider.provider().openSelector();
			this.serverSocketChannel = ServerSocketChannel.open();
			this.serverSocketChannel.configureBlocking(false);	//设置为非阻塞
			this.serverSocketChannel.socket().bind(new InetSocketAddress("localhost", this.port));
			this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch(Exception e) {
			System.out.println("nio init failure");
			e.printStackTrace();
		}
//		try {
//			serverSocket = new ServerSocket(port);
//		} catch(IOException e) {
//			System.out.println("ServerSocket cann't establish");
//			e.printStackTrace();
//		}
		running = true;
		System.out.println("HttpServer is runing ~~");
		while(running) {
			try {
				this.selector.select();
				Iterator selectorKeys = this.selector.selectedKeys().iterator();
				while(selectorKeys.hasNext()) {
					System.out.println("running ~ ~ running");
					SelectionKey key = (SelectionKey) selectorKeys.next();
					selectorKeys.remove();
					if(!key.isValid()) {
						continue;
					}
					if(key.isAcceptable()) {
						this.accept(key);
					} else if (key.isReadable()) {
						 key.interestOps(key.interestOps()&(~SelectionKey.OP_READ));
						threadPool.execute(new RequestHandler(key, root));
						//this.read(key);
					}
				}
				//Socket incomingSocket = serverSocket.accept();
				//TODO 在这里引进线程池，而不是直接new个线程
				//threadPool.execute(new RequestHandler(incomingSocket,root));  
				requestNum++;
			} catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println("System deal " + requestNum + " request");
		}
		System.out.println("System is down");
	}
	
	/**
	 * 服务器连接上客户端
	 * @param key 信号
	 * @throws IOException
	 */
	public void accept(SelectionKey key) throws IOException {
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		if(serverSocketChannel.equals(server)) {
			socketChannel = server.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
		}
	}
	
	public void read(SelectionKey key) throws IOException {
		this.buf.clear();
		SocketChannel channel = (SocketChannel) key.channel();
		int count = channel.read(buf);
		
		if(count == -1) {
			key.channel().close();
			key.cancel();
			return;
		}
		
		String input = new String(this.buf.array()).trim(); 
		if (channel.equals(this.socketChannel)) {
            System.out.println("您的输入为：" + input);  
        }
	}
	
	public void shutDown() {
		running = false;
	}
}
