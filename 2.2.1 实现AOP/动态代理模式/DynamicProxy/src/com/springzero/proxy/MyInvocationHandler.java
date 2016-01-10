package com.springzero.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月7日 下午10:09:16
 * 类说明		我的调用处理器
 */
public class MyInvocationHandler implements InvocationHandler {
	private Object target;
	
	public MyInvocationHandler(Object target) {
		this.target = target;
	}
	@Override
	public Object invoke(Object obj, Method method, Object[] aobj) throws Throwable {
		
		long beginTime = System.currentTimeMillis();
		Object val =  method.invoke(target, aobj);
		System.out.println(method.getName() + " is running ! !");
		long endTime = System.currentTimeMillis();
		System.out.println("The Time is " + (endTime-beginTime));
		return val;
	}

}




















