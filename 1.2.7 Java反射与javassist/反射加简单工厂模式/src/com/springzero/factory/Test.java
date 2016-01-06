package com.springzero.factory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月6日 下午9:47:56
 * 类说明
 */
public class Test {

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		InputStream ips = new FileInputStream("config.properties");
		Properties props = new Properties();
		props.load(ips);
		ips.close();
		
		String className = props.getProperty("class");
		String name = props.getProperty("name");
		int price = Integer.parseInt(props.getProperty("price"));
		
		Car car = (Car)Class.forName(className).newInstance();
		car.setName(name);
		car.setPrice(price);
		System.out.println("car name : " + car.getName());
		System.out.println("car price : " + car.getPrice());
	}

}
