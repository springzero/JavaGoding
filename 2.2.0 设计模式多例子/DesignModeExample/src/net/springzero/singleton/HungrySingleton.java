package net.springzero.singleton;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午9:35:18
 * 类说明		饿汉式单例
 */
public class HungrySingleton {
	//饿汉的标志 加载类的时候已经实例化
	private static final HungrySingleton single = new HungrySingleton();
	
	//私有化构造函数，不允许其他类显式new实例
	private HungrySingleton() {}
	
	// 静态工厂方法
	public static HungrySingleton getInstance() {
		return single;
	}
}
