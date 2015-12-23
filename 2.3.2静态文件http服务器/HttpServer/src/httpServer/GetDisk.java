package httpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月23日 下午3:38:29
 * 类说明
 */
public class GetDisk {
	private HttpMessage httpMessage;
	private StringBuilder result;
	private Path path;
	
	public GetDisk(HttpMessage httpMessage) {
		result = new StringBuilder();
		this.httpMessage = httpMessage;
		path = Paths.get(this.httpMessage.root, this.httpMessage.targetFile);
	}
	
	public void work(OutputStream out) {
		if(!Files.exists(path)) {
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
		
		byte[] fileContents = null;
		try {
			fileContents = Files.readAllBytes(path);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		if(fileContents != null) {
			result.append("HTTP/1.1 200 OK\r\n");
			if(httpMessage.targetFile.endsWith("html")) {
				result.append("Content-Type: text/html; charset=utf-8\r\n");
			} else if(httpMessage.targetFile.endsWith("jpg")) {
				result.append("Content-Type: image/jpeg\r\n");
			}
			result.append("Keep-Alive: -1\r\n");
			result.append("\r\n");
			try {
				out.write(result.toString().getBytes());
				out.write(fileContents);
				out.flush();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
