package net.springzero.factory.abstractFactory;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午11:20:48
 * 类说明		具体工厂类
 * 用来生产生产等级为2的产品A，B
 */
public class ConcreateFactory2 implements AbstractFactory {
	
	//生产等级为2的产品A
	@Override
	public ProductA factoryA() {
		return new ConcreateProductA2();
	}

	//生产等级为2的产品B
	@Override
	public ProductB factoryB() {
		return new ConcreateProductB2();  
	}

}
























