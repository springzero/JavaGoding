package com.springzero.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月6日 下午7:22:24
 * 类说明		一个用来实验的类
 */
@MyAnnotation(color="blue")
public class Person {
	private int age;
	private String name;
	
	public Person(int age, String name) {
		this.age = age;
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}
	public String getName() {
		return name;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void sayHello() {
		System.out.println("hello world great");
	}
	
}
