package net.springzero.factory.abstractFactory;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午11:07:31
 * 类说明		抽象工厂类		
 * 工厂里产品族包括ProductA和ProductB两种产品，定义了生产多族产品
 */
public interface AbstractFactory {
	public ProductA factoryA();
	public ProductB factoryB();
}
