package com.springzero.httpserver;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.springzero.handler.ResponseHandler;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月23日 下午3:38:29
 * 类说明
 */
public class GetDisk {
	private HttpMessage httpMessage;
	private ResponseHandler response;
	private Path path;
	
	public GetDisk(HttpMessage httpMessage, OutputStream out) {
		response = new ResponseHandler(out);
		this.httpMessage = httpMessage;
		path = Paths.get(this.httpMessage.root, this.httpMessage.targetFile);
	}
	
	public void work() {
		if(!Files.exists(path)) {
			response.send_400_status();
			return;
		}
		File f = new File(path.toUri());
		httpMessage.lastModified = Long.toString(f.lastModified());
		if(httpMessage.If_Modified_Since) {
			if(httpMessage.If_Modified_SinceString.equals(httpMessage.lastModified)) {
				response.send_304_status();
			}
		}
		
		byte[] fileContents = null;
		try {
			fileContents = Files.readAllBytes(path);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		if(fileContents != null) {
			response.send_200_status(httpMessage, fileContents);
		}
	}
	
}
