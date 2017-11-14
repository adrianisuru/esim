import java.io.File;

import org.esim.util.Utils;

public class HelloWorld {

	public static void main(String[] args) {
		try {
			System.out.println(Utils.loadResource("/test.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
