package net.springzero.singleton;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午10:09:56
 * 类说明		枚举式单例
 * 优点： 不仅能避免多线程同步问题，而且还能防止序列化重新创建新的对象
 */
public enum EnumSingleton {
	INSTANCE;
	public void whateverMethod() {}
}
