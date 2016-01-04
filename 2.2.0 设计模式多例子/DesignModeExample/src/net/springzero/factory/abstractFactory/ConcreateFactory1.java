package net.springzero.factory.abstractFactory;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午11:20:48
 * 类说明		具体工厂类
 * 用来生产生产等级为1的产品A，B
 */
public class ConcreateFactory1 implements AbstractFactory {
	
	//生产等级为1的产品A
	@Override
	public ProductA factoryA() {
		return new ConcreateProductA1();
	}

	//生产等级为1的产品B
	@Override
	public ProductB factoryB() {
		return new ConcreateProductB1();  
	}

}
























