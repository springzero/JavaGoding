import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class FileUtilServer {
	
	public static void main(String[] agrs) {
		try {
			IFileUtil file = new FileUtilImpl();
			LocateRegistry.createRegistry(1099);
			Naming.rebind("rmi://127.0.0.1/FileUtilServer", file);
			System.out.println("Ready......");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}