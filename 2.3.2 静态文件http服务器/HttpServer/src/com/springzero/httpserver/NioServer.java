package com.springzero.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

import com.springzero.handler.RequestHandler;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月25日 下午2:54:04
 * 类说明		提供非阻塞服务类
 */
public class NioServer extends BaseServer{
	private ServerSocketChannel serverSocketChannel;	//通道
	private SocketChannel socketChannel;							//连接
	private Selector selector;													//选择器
	private ByteBuffer buf = ByteBuffer.allocate(512);		//缓冲区
	
	public NioServer(){
		super();
	}
	
	@Override
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
		running = true;
		System.out.println("HttpServer is runing ~~");
		while(running) {
			try {
				this.selector.select();	//唯一一个阻塞的方法
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
						 // 引进线程池处理read操作
						threadPool.execute(new RequestHandler(key, root));
					}
				}
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
	
}
