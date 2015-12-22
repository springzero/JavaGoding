/*
 * 堆溢出
 * 定义一个动态数组，往里面添加对象，使它占据25M的大小，在命令行下执行
 * java -Xmx20m -Xms5m heap
 */

import java.util.Vector;

public class heap
{
	public static void main(String[] args){
		Vector v = new Vector();
		for(int i = 0; i < 25; i++)
			v.add(new byte[1*1024*1024]);
	}
}