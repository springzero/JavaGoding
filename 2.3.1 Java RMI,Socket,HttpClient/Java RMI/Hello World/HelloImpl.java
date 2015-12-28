import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements IHello {
	
	// this is necessary (why?) Because the default constructor doesn't thorw RemoteException.
	//and the constructor of UnicastRemoteObject thorw RemoteException
	protected HelloImpl() throws RemoteException {
		super();
	}
	
	private static final long serialVersionUID = 1L;
	public String sayHello(String name) throws RemoteException {
		return "Hello World," + name + "~ ~";
	}
	
	public static void main(String[] args) {
		try {
			IHello hello = new HelloImpl();
			// registry remote object
			LocateRegistry.createRegistry(1099);
			java.rmi.Naming.rebind("rmi://localhost:1099/hello", hello);
			System.out.println("Ready... ...");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}


















