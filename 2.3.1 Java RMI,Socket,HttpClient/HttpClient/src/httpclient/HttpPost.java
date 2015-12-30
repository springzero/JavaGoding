package httpclient;

import java.io.ByteArrayInputStream;
import java.net.Socket;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月30日 上午10:53:05
 * 类说明  post方式请求 （当然也可以设置成get方式）
 */
public class HttpPost {

	public static void main(String[] agrs) throws Exception {
		//处理器
		HttpProcessor httpProc = HttpProcessorBuilder.create()
				.add(new RequestContent())
				.add(new RequestTargetHost())
				.add(new RequestConnControl())
				.add(new RequestUserAgent("HTTP/1.1"))
				.add(new RequestExpectContinue(true)).build();
		
		//执行器
		HttpRequestExecutor httpExecutor = new HttpRequestExecutor();
		
		HttpCoreContext coreContext = HttpCoreContext.create();
		HttpHost host = new HttpHost("localhost", 9090);
		coreContext.setTargetHost(host);
		
		DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
		ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;
		
		try {
			HttpEntity[] requestBodies = {
					new StringEntity("This is the first test request", ContentType.create("text/plain", Consts.UTF_8)),
					new ByteArrayEntity("This is the second test request".getBytes(Consts.UTF_8), ContentType.APPLICATION_OCTET_STREAM),
					new InputStreamEntity(new ByteArrayInputStream("This is the third test request (will be chunked)".getBytes(Consts.UTF_8)), ContentType.APPLICATION_OCTET_STREAM)
			};
			
			for(int i = 0, length = requestBodies.length; i < length; i++) {
				if(!conn.isOpen()) {
					Socket socket = new Socket(host.getHostName(), host.getPort());
					conn.bind(socket);
				}
				BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", "/index.html");
				request.setEntity(requestBodies[i]);
				System.out.println(">> Request URI: " + request.getRequestLine().getUri());
				
				httpExecutor.preProcess(request, httpProc, coreContext);
				HttpResponse response  = httpExecutor.execute(request, conn, coreContext);
				httpExecutor.postProcess(response, httpProc, coreContext);
				
				System.out.println("<< Response: " + response.getStatusLine());
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("===========================");
				if(!connStrategy.keepAlive(response, coreContext)) {
					conn.close();
				} else {
					System.out.println("Connect kept alive...");
				}
			}
		} finally {
			conn.close();
		}
	}

}





























