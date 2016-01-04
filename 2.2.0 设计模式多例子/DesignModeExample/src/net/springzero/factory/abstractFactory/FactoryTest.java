package net.springzero.factory.abstractFactory;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午11:31:34
 * 类说明
 */
public class FactoryTest {

public static void main(String[] args) {
	AbstractFactory factory1 = new ConcreateFactory1();
	factory1.factoryA().method1();
	factory1.factoryA().method2();
	factory1.factoryB().method1();
	factory1.factoryB().method2();
	AbstractFactory factory2 = new ConcreateFactory2();
	factory2.factoryA().method1();
	factory2.factoryA().method2();
	factory2.factoryB().method1();
	factory2.factoryB().method2();

}

}
