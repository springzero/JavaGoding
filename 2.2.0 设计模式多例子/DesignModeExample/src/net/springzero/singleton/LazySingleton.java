package net.springzero.singleton;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午9:45:49
 * 类说明		懒汉式单例
 */
public class LazySingleton {
	private static LazySingleton single = null;
	
	// 私有化默认构造函数
	private LazySingleton() {}
	
	// 同步下 防止多线程出意外
	public synchronized static LazySingleton getInstance() {
		if(single == null) {
			single = new LazySingleton();
		}
		return single;
	}
	
}







