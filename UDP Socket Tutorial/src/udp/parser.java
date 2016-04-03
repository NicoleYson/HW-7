package udp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

//Group members: Mikhail Prigozhiy, Nicole Yson, Jonathan Getahun, Jonathan Caverly

public class parser {

	public static Domain[] parse(File f) {

		Domain[] domArray = null;

		//If the file does not exist
		if (!f.exists()) {
			domArray = new Domain[1];
			domArray[0] = new Domain("", "");
			domArray[0].setAddress("err");
			domArray[0].setHost("err");
			return domArray;
		}
		
		int lineCount = 0;
		int count = 0;

		try {

			BufferedReader input = new BufferedReader(new FileReader(f));
			String line;

			//If the file is empty
			if (!input.ready()) {
				input.close();
				domArray = new Domain[1];
				domArray[0] = new Domain("", "");
				domArray[0].setAddress("err");
				domArray[0].setHost("err");
				return domArray;
			}

			//Get the number of lines that are not commented
			while ((line = input.readLine()) != null) {
				if (line.length() > 0 && line.charAt(0) != '#') {
					lineCount++;
				}	
			}

			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		//If there are uncommented lines, create a domain array of that size
		if (lineCount != 0)
			domArray = new Domain[lineCount];

		try {

			BufferedReader input = new BufferedReader(new FileReader(f));
			String line;

			//While the end of the file has not been reached
			while ((line = input.readLine()) != null) {
				//If there is something on the line and it is not a comment
				if (line.length() > 0 && line.charAt(0) != '#') {
					StringTokenizer st = new StringTokenizer(line);
					String address = st.nextToken();
					String host = st.nextToken();
					//Create a new Domain object, add it to the array, and initialize its address and host
					domArray[count] = new Domain("", "");
					domArray[count].setAddress(address);
					domArray[count].setHost(host);
					count++;
				}	
			}

			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return domArray;

	}

}