/*
 * 栈溢出
 * 不停的为方法申请栈空间
 */
  
public class stack{

	public static void F(int i){
		F(i);
	}

	public static void main(String[] agrs){
		F(1);
	}

}