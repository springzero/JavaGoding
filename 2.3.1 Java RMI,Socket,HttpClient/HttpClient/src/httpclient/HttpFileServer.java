package httpclient;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.ConnectionClosedException;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月3日 下午8:46:37
 * 类说明		文件服务器
 */
public class HttpFileServer {
	
	public static void main(String[] args) throws Exception{
		if(args.length < 1) {
			System.out.println("Please add document root directory");
			System.exit(1);
		}
		//  Document root directory
		String docRoot = args[0];
		System.out.println("docRoot: " + docRoot);
		int port = 8080;
		if(args.length >= 2) {
			port = Integer.parseInt(args[1]);
		}
		
		// 
		SSLContext sslContext  = null;
		if(port == 8443) {
			//Init SSL context
			URL url = HttpFileServer.class.getResource("/my.kestore");
			if(url == null) {
				System.out.println("Keystore not found");
				System.exit(1);
			}
			sslContext = SSLContexts.custom()
					.loadKeyMaterial(url, "secret".toCharArray(), "secret".toCharArray())
					.build();
		}
		
		SocketConfig socketConfig = SocketConfig.custom()
				.setSoTimeout(15000)
				.setTcpNoDelay(true)
				.build();
		
		final HttpServer server = ServerBootstrap.bootstrap()
				.setListenerPort(port)
				.setServerInfo("HTTP/1.1")
				.setSocketConfig(socketConfig)
				.setSslContext(sslContext)
				.setExceptionLogger(new StdErrorExceptionLogger())
				.registerHandler("*", new HttpFileHandler(docRoot))
				.create();
		
		server.start();
		server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				server.shutdown(5, TimeUnit.SECONDS);
			}
		});
	}
	
	static class StdErrorExceptionLogger implements ExceptionLogger {
		
		@Override
		public void log(final Exception e) {
			if(e instanceof SocketTimeoutException) {
				System.err.println("Connection timed out");
			} else if(e instanceof ConnectionClosedException) {
				System.err.println(e.getMessage());
			} else {
				e.printStackTrace();
			}
		}
	}
	
	static class HttpFileHandler implements HttpRequestHandler {
		private final String docRoot;
		
		public HttpFileHandler(final String docRoot) {
			super();
			this.docRoot = docRoot;
		}
		
		public void handle(
				final HttpRequest request,
				final HttpResponse response,
				final HttpContext context
				) throws MethodNotSupportedException, IOException {
			String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
			if(!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
				throw new MethodNotSupportedException(method + " method not supported");
			}
			String target = request.getRequestLine().getUri();
			
			if(request instanceof HttpEntityEnclosingRequest) {
				HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
				byte[] entityContent = EntityUtils.toByteArray(entity);
				System.out.println("Incoming entity content (bytes): " + entityContent.length);
			}
			
			final File file = new File(this.docRoot, URLDecoder.decode(target, "UTF-8"));
			if(!file.exists()) {
				response.setStatusCode(HttpStatus.SC_NOT_FOUND);
				StringEntity entity = new StringEntity(
						"<html><body><h1>File " + file.getPath() + 
						" not found</h1></body></html>",
						ContentType.create("text/html", "UTF-8"));
				response.setEntity(entity);
				System.out.println("File " + file.getPath() + " not found");
			} else if(!file.canRead() || file.isDirectory()) {
				response.setStatusCode(HttpStatus.SC_FORBIDDEN);
				StringEntity entity = new StringEntity(
						"<html><body><h1>Access denied</h1></body></html>",
						ContentType.create("text/html", "UTF-8"));
				response.setEntity(entity);
				System.out.println("Cannot read file " + file.getPath());
			} else {
				HttpCoreContext coreContext = HttpCoreContext.adapt(context);
				HttpConnection conn = coreContext.getConnection(HttpConnection.class);
				response.setStatusCode(HttpStatus.SC_OK);
				FileEntity body = new FileEntity(file, ContentType.create("text/html", (Charset) null));
				response.setEntity(body);
				System.out.println(conn + ": serving file " + file.getPath());
			}
			
		}
		
	}
	
}










































