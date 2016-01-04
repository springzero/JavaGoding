package net.springzero.observer;

import java.util.Observable;

/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午10:31:15
 * 类说明		被观察者类
 */
public class SimpleObservable extends Observable {
	private String msg = "hello world";
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		if(!this.msg.equals(msg)) {
			this.msg = msg;
			setChanged();
			// 只有在setChange（）被调用后，notif月Observe（）才会去通知所有的observer（其调用update（）），否则什么都不干
			notifyObservers();
		}
	}
	
}
