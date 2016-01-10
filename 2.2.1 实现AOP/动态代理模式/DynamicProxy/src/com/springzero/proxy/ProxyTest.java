package com.springzero.proxy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月7日 下午9:33:54
 * 类说明
 */
public class ProxyTest {

	public static void main(String[] args) throws Exception {
		//将委托类作为参数传入到 InvocationHandler
		InvocationHandler myInvocationHandler = new MyInvocationHandler(new PuTong());
		
		/*生成动态代理类另一种方法：
		Person person = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), Person.class, myInvocationHandler);
		*/
		
		//生成动态代理类字节码
		Class clazzProxy = Proxy.getProxyClass(Person.class.getClassLoader(), Person.class);
		
		//获取动态代理类的构造方法
		Constructor constructor = clazzProxy.getConstructor(InvocationHandler.class);
		
		//将InvocationHandler扔进构造方法里，建立桥梁，使其能调用动态代理 实现了person接口的委托类
		Person person = (Person)constructor.newInstance(myInvocationHandler);
		
		System.out.println(person.toString());
		System.out.println(person.hashCode());
		System.out.println(person.getClass().getName());
		person.showSelf();
		//person.showSelf();
		
		//打印当前代理类的所有方法
		Method[] methods = clazzProxy.getMethods();
		for(Method method : methods) {
			String name = method.getName();
			StringBuilder sBuilder = new StringBuilder(name);
			sBuilder.append('(');
			Class[] clazzParams = method.getParameterTypes();
			for(Class clazzParam : clazzParams) {
				sBuilder.append(clazzParam.getName()).append(',');
			}
			if(clazzParams.length != 0) {
				sBuilder.deleteCharAt(sBuilder.length() - 1);
			}
			sBuilder.append(')');
			System.out.println(sBuilder.toString());
		}
		
	}

}























