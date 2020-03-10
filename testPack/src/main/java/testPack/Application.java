package testPack;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.Packer;

public class Application {

	
	public static void main(String[] args) {
		try {
			Packer.pack("/Users/Farzaneh/Desktop/s1.txt");
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
