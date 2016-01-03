package httpclient;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.ConnectionClosedException;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月2日 下午8:00:58
 * 类说明  反向代理
 */
public class ReverseProxy {
	
	private static final String HTTP_IN_CONN = "http.proxy.in-conn";
	private static final String HTTP_OUT_CONN = "http.proxy.out-conn";
	private static final String HTTP_CONN_KEEPALIVE = "http.proxy.conn-keepalive";
	
	/**
	 * @param args[0]	hostname (necessary)	你要代理的服务器ip
	 * @param args[1]	port									你要代理的服务器服务的端口（默认是80端口）
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Please add target hostname and port");
			System.exit(1);
		}
		final String hostname = args[0];
		int port = 80;
		if (args.length > 1) {
			port = Integer.parseInt(args[1]);
		}
		final HttpHost target = new HttpHost(hostname, port);
		//客户需访问8888端口，才能访问真正服务器的服务；我觉得真实场景的话，这里也应该设置为80端口
		final Thread t = new RequestListenerThread(8888, target);	
		t.setDaemon(false);
		t.start();
	}
	
	static class ProxyHandler implements HttpRequestHandler {
		private final HttpHost target;
	    private final HttpProcessor httpProc;
	    private final HttpRequestExecutor httpExecutor;
	    private final ConnectionReuseStrategy connStrategy;
	    
	    public ProxyHandler(
	    		final HttpHost target,
	    		final HttpProcessor httpProc,
	    		final HttpRequestExecutor httpExecutor
	    		) {
	    	super();
	    	this.target = target;
	    	this.httpProc = httpProc;
	    	this.httpExecutor = httpExecutor;
	    	this.connStrategy = DefaultConnectionReuseStrategy.INSTANCE;
	    }
	    
	    public void handle(
	    		final HttpRequest request,
	    		final HttpResponse response,
	    		final HttpContext context
	    		) throws HttpException, IOException {
	    	final HttpClientConnection conn = (HttpClientConnection) context.getAttribute(HTTP_OUT_CONN);
	    	context.setAttribute(HttpCoreContext.HTTP_CONNECTION, conn);
	    	context.setAttribute(HttpCoreContext.HTTP_TARGET_HOST, this.target);
	    	
	    	System.out.println(">> Request URI: " + request.getRequestLine().getUri());
	    	
	    	request.removeHeaders(HTTP.CONTENT_LEN);
	    	request.removeHeaders(HTTP.TRANSFER_ENCODING);
	    	request.removeHeaders(HTTP.CONN_DIRECTIVE);
	    	request.removeHeaders("Keep-Alive");
	    	request.removeHeaders("Proxy-Authenticate");
	    	request.removeHeaders("IE");
	    	request.removeHeaders("Trailers");
	    	request.removeHeaders("Upgrade");
	    	
	    	// 在http处理总能看到这几句核心处理代码
	    	this.httpExecutor.preProcess(request, httpProc, context);
	    	final HttpResponse targetResponse = this.httpExecutor.execute(request, conn, context);
	    	this.httpExecutor.postProcess(response, httpProc, context);
	    	
	    	targetResponse.removeHeaders(HTTP.CONTENT_LEN);
	    	targetResponse.removeHeaders(HTTP.TRANSFER_ENCODING);
	    	targetResponse.removeHeaders(HTTP.CONN_DIRECTIVE);
	    	targetResponse.removeHeaders("Keep-Alive");
	    	targetResponse.removeHeaders("TE");
            targetResponse.removeHeaders("Trailers");
            targetResponse.removeHeaders("Upgrade");
            
            response.setStatusLine(targetResponse.getStatusLine());
            response.setHeaders(targetResponse.getAllHeaders());
            response.setEntity(targetResponse.getEntity());
            
            System.out.println("<< Response: " + response.getStatusLine());

            final boolean keepalive = this.connStrategy.keepAlive(response, context);
            context.setAttribute(HTTP_CONN_KEEPALIVE, new Boolean(keepalive));
	    }
	  
	}
	
	static class RequestListenerThread extends Thread {
		
		private final HttpHost target;
		private final ServerSocket serverSocket;	//这里居然用了ServerSocket，而不是conn
		private final HttpService httpService;
		
		public RequestListenerThread(final int port, final HttpHost target) throws IOException {
			this.target = target;
			this.serverSocket = new ServerSocket(port);
			
			// Immutable 不可变  为in建立一个http处理器
			// Set up HTTP protical processor for incoming connections
			final HttpProcessor inHttpProc = new ImmutableHttpProcessor(
					new HttpRequestInterceptor[] {
							new RequestContent(),
							new RequestTargetHost(),
							new RequestConnControl(),
							new RequestUserAgent("HTTP/1.1"),
							new RequestExpectContinue(true)
					});
			
			// 为 outConn 建立一个http处理器 为什么感觉英文原文要好些。。
			// Set up HTTP protical processor for outgoing connections
			final HttpProcessor outHttpProc = new ImmutableHttpProcessor(
					new HttpResponseInterceptor[] {
							new ResponseDate(),
							new ResponseServer("HTTP/1.1"),
							new ResponseContent(),
							new ResponseConnControl()
					});
			
			final HttpRequestExecutor httpExecutor = new HttpRequestExecutor();
			
			final UriHttpRequestHandlerMapper uriMapper = new UriHttpRequestHandlerMapper();
			uriMapper.register("*", new ProxyHandler(
					this.target,
					outHttpProc,
					httpExecutor
					));
			this.httpService = new HttpService(inHttpProc, uriMapper);
		}
		
		@Override
		public void run() {
			System.out.println("Listening on port " + this.serverSocket.getLocalPort());
			while(!Thread.interrupted()) {
				try {
					final int bufSize = 8 * 1024;
					final Socket inSocket = this.serverSocket.accept();
					final DefaultBHttpServerConnection inConn = new DefaultBHttpServerConnection(bufSize);
					inConn.bind(inSocket);
					System.out.println("Incoming connection from " + inSocket.getInetAddress());
					
					final Socket outSocket = new Socket(this.target.getHostName(), this.target.getPort());
					final DefaultBHttpClientConnection outConn = new DefaultBHttpClientConnection(bufSize);
					outConn.bind(outSocket);
					System.out.println("Outgoing connection to " + outSocket.getInetAddress());
					
					final Thread t = new ProxyThread(this.httpService, inConn, outConn);
					t.setDaemon(true);	//守护线程
					t.start();
				} catch(final InterruptedIOException e) {
					e.printStackTrace();
					break;
				} catch(final IOException e) {
					System.err.println("I/O error initialising connection thread: " + e.getMessage());
					break;
				}
			}
		}
		
	}
	
	static class ProxyThread extends Thread {
		
		private final HttpService httpService;
		private final HttpServerConnection inConn;
		private final HttpClientConnection outConn;
		
		public ProxyThread(
				final HttpService httpService,
				final HttpServerConnection inConn,
				final HttpClientConnection outConn
				) {
			super();
			this.httpService = httpService;
			this.inConn = inConn;
			this.outConn = outConn;
		}
		
		@Override
		public void run() {
			System.out.println("YES New connection thread");
			final HttpContext context = new BasicHttpContext(null);
			
			context.setAttribute(HTTP_IN_CONN, this.inConn);
			context.setAttribute(HTTP_OUT_CONN, this.outConn);
			
			try {
				while(!Thread.interrupted()) {
					if(!this.inConn.isOpen()) {
						this.outConn.close();
						break;
					}
					
					/*
					 * 这句方法中有条这样的代码：
					 * response = responseFactory.newHttpResponse(HttpVersion.HTTP_1_1, 100, context);
					 * 这里生成的response ，应该和 context.setAttribute(HTTP_OUT_CONN, this.outConn);有关
					 * 这样就贯通起来
					 */
					this.httpService.handleRequest(this.inConn, context);
					
					final Boolean keepAlive = (Boolean) context.getAttribute(HTTP_CONN_KEEPALIVE);
					if(!Boolean.TRUE.equals(keepAlive)) {
						this.outConn.close();
						this.inConn.close();
						break;
					}
				}
			} catch(final ConnectionClosedException e) {
				System.err.println("Client closed connection");
			} catch(final IOException e) {
				System.err.println("I/O error: " + e.getMessage());
			} catch(final HttpException e) {
				System.err.println("Unrecoverable HTTP protocol violation: " + e.getMessage());
			} finally {
				try {
					this.inConn.shutdown();
				} catch(final IOException ignore) {
					//ignore
				}
				
				try {
					this.outConn.shutdown();
				} catch(final IOException ignore) {
					//ignore
				}
			}
		}
		
		
	}
	
	
}






























