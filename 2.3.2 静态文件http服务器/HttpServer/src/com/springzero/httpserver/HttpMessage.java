package com.springzero.httpserver;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2015年12月23日 下午2:07:18
 * 类说明		用来储存从http请求中解析出的重要信息，方便以后调用
 */
public class HttpMessage {
	public String root;
	public String targetFile;
	public String lastModified;
	public boolean If_Modified_Since = false;
	public String If_Modified_SinceString = null;
	
}
