package com.springzero.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月6日 下午8:15:46
 * 类说明
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
	String color();
}
