package httpServer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月23日 下午2:56:49
 * 类说明
 */
public class BadRequest {
	StringBuilder result;
	
	public BadRequest() {
		this.result = new StringBuilder();
	}
	
	public void work(OutputStream out) {
		result.append("HTTP/1.1 400 bad request\r\n"
								+ "Connection: close\r\n");
		result.append("\r\n");
		try {
			out.write(result.toString().getBytes());
			out.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
