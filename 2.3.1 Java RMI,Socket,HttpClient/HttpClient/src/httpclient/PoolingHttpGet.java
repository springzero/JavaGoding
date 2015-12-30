package httpclient;

import java.io.IOException;
import java.util.concurrent.Future;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.pool.BasicConnFactory;
import org.apache.http.impl.pool.BasicConnPool;
import org.apache.http.impl.pool.BasicPoolEntry;
import org.apache.http.message.BasicHttpRequest;
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
 * @version 创建时间：2015年12月30日 下午12:41:45
 * 类说明  看上去像是池技术 我来瞧一瞧 敲一敲。
 */
public class PoolingHttpGet {
	
	public static void main(String[] args) throws Exception {
		final HttpProcessor httpProc = HttpProcessorBuilder.create()
				.add(new RequestContent())
				.add(new RequestTargetHost())
				.add(new RequestConnControl())
				.add(new RequestUserAgent("HTTP/1.1"))
				.add(new RequestExpectContinue(true)).build();
		
		final HttpRequestExecutor httpExecutor = new HttpRequestExecutor();
		final BasicConnPool pool = new BasicConnPool(new BasicConnFactory());
		pool.setDefaultMaxPerRoute(2);
		pool.setMaxTotal(2);
		
		HttpHost[] targets = {
				new HttpHost("www.baidu.com", 80),
				new HttpHost("www.ichunqiu.com", 80),
				new HttpHost("www.bing.com", 80)
		};
		
		class WorkerThread extends Thread {
			private final HttpHost target;
			
			public WorkerThread(final HttpHost target) {
				super();
				this.target = target;
			}
			
			@Override
			public void run() {
				ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;
				try {
					Future<BasicPoolEntry> future = pool.lease(this.target, null);
					boolean reusable = false;
					BasicPoolEntry entry = future.get();
					try {
						HttpClientConnection conn = entry.getConnection();
						HttpCoreContext coreContext = HttpCoreContext.create();
						coreContext.setTargetHost(this.target);
						
						BasicHttpRequest request = new BasicHttpRequest("GET", "/");
						System.out.println(">> Request URI: " + request.getRequestLine().getUri());
						
						httpExecutor.preProcess(request, httpProc, coreContext);
						HttpResponse response = httpExecutor.execute(request, conn, coreContext);
						httpExecutor.postProcess(response, httpProc, coreContext);
						
						System.out.println("<< Response: " + response.getStatusLine());
						System.out.println(EntityUtils.toString(response.getEntity()));
						
						reusable = connStrategy.keepAlive(response, coreContext);
					} catch(IOException e) {
						throw e;
					} catch(HttpException e) {
						throw e;
					} finally {
						if(reusable) {
							System.out.println("Connection kept alive...");
							pool.release(entry, reusable);
						}
					}
				} catch(Exception e) {
					System.out.println("Request to " + this.target + " failed: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
		};
		WorkerThread[] wokers = new WorkerThread[targets.length];
		for(int i = 0, length = wokers.length; i < length; i++) {
			wokers[i] = new WorkerThread(targets[i]);
		}
		for(int i = 0, length = wokers.length; i < length; i++) {
			wokers[i].start();
		}
		for(int i = 0, length = wokers.length; i < length; i++) {
			wokers[i].join();
		}
	}
	
}



















