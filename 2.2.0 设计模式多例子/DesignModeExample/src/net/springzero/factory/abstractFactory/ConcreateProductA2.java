package net.springzero.factory.abstractFactory;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午11:27:51
 * 类说明  具体产品A		等级为2
 */
public class ConcreateProductA2 implements ProductA {

	@Override
	public void method1() {
		System.out.println("等级为2的产品A的method1()"); 
	}

	@Override
	public void method2() {
		System.out.println("等级为2的产品A的method2()"); 
	}

}
