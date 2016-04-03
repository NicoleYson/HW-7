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

			if (!input.ready()) {
				input.close();
				domArray = new Domain[1];
				domArray[0] = new Domain("", "");
				domArray[0].setAddress("err");
				domArray[0].setHost("err");
				return domArray;
			}

			while ((line = input.readLine()) != null) {
				if (line.length() > 0 && line.charAt(0) != '#') {
					lineCount++;
				}	
			}

			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (lineCount != 0)
			domArray = new Domain[lineCount];

		try {

			BufferedReader input = new BufferedReader(new FileReader(f));
			String line;

			while ((line = input.readLine()) != null) {
				if (line.length() > 0 && line.charAt(0) != '#') {
					StringTokenizer st = new StringTokenizer(line);
					String address = st.nextToken();
					String host = st.nextToken();
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