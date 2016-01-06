package com.springzero.reflect;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Annotation;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月6日 下午7:24:41
 * 类说明		反射测试
 */
public class ReflectTest {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, IntrospectionException {
		int reflectAge;
		String reflectName;
		//Person person = new Person(18,"springzero");
		Constructor constructor = Class.forName("com.springzero.reflect.Person").getConstructor(int.class,String.class);
		Person person = (Person)constructor.newInstance(18,"springzero");
		
		
		Field field = person.getClass().getDeclaredField("age");
		field.setAccessible(true);		//俗称暴力反射（因为age在类中定义是private）
		reflectAge = field.getInt(person);
		System.out.println("reflectAge : " + reflectAge);
		
		field = Person.class.getDeclaredField("name");
		field.setAccessible(true);
		reflectName = field.get(person).toString();
		System.out.println("reflectName : " + reflectName);
		
		//正规的应该获取其相应的get方法
		PropertyDescriptor pd = new PropertyDescriptor("age", Class.forName("com.springzero.reflect.Person"));
		Method methodGetAge = pd.getReadMethod();
		Object retVal = methodGetAge.invoke(person);
		System.out.println("reflectAge : " + retVal + " by method get");
		
		Method methodSetAge = pd.getWriteMethod();
		methodSetAge.invoke(person, 100);
		retVal = methodGetAge.invoke(person);
		System.out.println("reflectAge : " + retVal + " after method set");
		
		//反射普通方法
		Method methodSayHello = Person.class.getMethod("sayHello");
		methodSayHello.invoke(person);
		
		//反射获取注解
		if(Person.class.isAnnotationPresent(MyAnnotation.class)) {
			MyAnnotation annotation = Person.class.getAnnotation(MyAnnotation.class);
			System.out.println(annotation.color());
		}
		
	}

}
