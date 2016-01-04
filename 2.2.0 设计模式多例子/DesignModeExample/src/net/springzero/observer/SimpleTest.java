package net.springzero.observer;
/**
 * @author springzero E-mail: 464150147@qq.com
 * @version 创建时间：2016年1月4日 下午10:40:40
 * 类说明
 */
public class SimpleTest {
	public static void main(String[] args) {
		SimpleObservable observed = new SimpleObservable();
		SimpleObserver observer = new SimpleObserver(observed);
		observed.setMsg("hello world");
		observed.setMsg("Hi world");
		observed.setMsg("go go go ~ ~");
	}
}
