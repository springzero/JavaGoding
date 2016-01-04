package net.springzero.singleton;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午10:04:15
 * 类说明		静态内部类
 * 优点：加载时不会初始化静态变量，因为没有主动使用，达到了lazy loading的效果
 */
public class InternalSingleton {
	
	private static class SingletonHolder{
		private final static  InternalSingleton INSTANCE = new InternalSingleton();
	}
	
	private InternalSingleton() {}
	
	public static InternalSingleton getInstance() {
		return SingletonHolder.INSTANCE;
	}
}
