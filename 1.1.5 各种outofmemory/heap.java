/*
 * 堆溢出
 * 定义一个动态数组，往里面添加对象，使它占据25M的大小，在命令行下执行
 * java -Xmx20m -Xms5m heap
 * 我们还可以添加参数-XX:+HeapDumpOnOutOfMemoryError,在内存溢出时导出整个堆信息
 * 配合上面参数使用有-XX:HeapDumpPath=d:/a.dump,来指定导出堆的位置
 */

import java.util.Vector;

public class heap{

	public static void main(String[] args){
		Vector v = new Vector();
		for(int i = 0; i < 25; i++)
			v.add(new byte[1*1024*1024]);
	}

}