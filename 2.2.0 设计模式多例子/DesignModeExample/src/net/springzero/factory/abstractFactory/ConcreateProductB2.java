package net.springzero.factory.abstractFactory;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午11:28:00
 * 类说明		具体产品B	等级为2
 */
public class ConcreateProductB2 implements ProductB {

	@Override
	public void method1() {
		System.out.println("等级为2的产品B的method1()"); 
	}

	@Override
	public void method2() {
		System.out.println("等级为2的产品B的method2()"); 
	}

}
