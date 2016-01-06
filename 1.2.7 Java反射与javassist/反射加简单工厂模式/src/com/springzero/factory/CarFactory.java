package com.springzero.factory;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月6日 下午10:10:45
 * 类说明		生产汽车的工厂
 */
public class CarFactory {
	
	public static Car createCar(String name) {
		try {
			Class clazz = Class.forName(name);
			return (Car)clazz.newInstance();
		} catch(ClassNotFoundException e) {
			System.out.println("The class  doesn't exsist");
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.out.println("This class can't be instantiated");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
