package sock;

import java.io.File;

public class Test {

	public static void main (String[] args) {
		
		File f = new File(args[0]);
				
		String[] arr = parser.parse(f);
				
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
		
	}
	
}
