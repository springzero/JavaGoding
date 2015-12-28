import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.Naming;

public class FileUtilClient {
	
	public static void main(String[] args) {
		if(args.length != 3) {
			System.out.println("The first parameter: RMI Server IP");
			System.out.println("The second parameter: The full name of the file that you want to down");
			System.out.println("The third parameter: The position that file will be");
			System.exit(0);
		}
		try {
			String name = "rmi://" + args[0] + "/FileUtilServer";
			IFileUtil fileUtil = (IFileUtil)Naming.lookup(name);
			byte[] filedata = fileUtil.downloadFile(args[1]);
			if(filedata == null) {
				System.out.println("Error: <filedata is null!>");
				System.out.println("The second parameter has a problem");
				System.exit(0);
			}
			File file = new File(args[2]);
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath()));
			output.write(filedata, 0, filedata.length);
			output.flush();
			output.close();
			System.out.println(file.getName() + " down successful!");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}














