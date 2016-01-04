package net.springzero.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午10:36:42
 * 类说明		观察者类
 */
public class SimpleObserver implements Observer {
	
	public SimpleObserver(SimpleObservable simpleObservable) {
		simpleObservable.addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("msg had changed to : " + ((SimpleObservable) o).getMsg());
	}

}










