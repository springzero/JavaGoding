package net.springzero.factory.abstractFactory;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午11:23:41
 * 类说明		具体产品A 等级为1
 */
public class ConcreateProductA1 implements ProductA {

	@Override
	public void method1() {
		System.out.println("等级为1的产品A的method1()"); 
	}

	@Override
	public void method2() {
		System.out.println("等级为1的产品A的method2()"); 
	}

}
