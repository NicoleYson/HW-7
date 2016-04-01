package sock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class parser {

	public static String[] parse(File f) {
		
		String[] strArray = null;
		
		int errFlag = 0;
		
		int lineCount = 0;
		int count = 0;
		
		try {

			BufferedReader input = new BufferedReader(new FileReader(f));
			String line;
			
			if (!input.ready()) {
				input.close();
				strArray = new String[1];
				strArray[0] = "";
				return strArray;
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
			strArray = new String[lineCount];
		
		try {

			BufferedReader input = new BufferedReader(new FileReader(f));
			String line;

			while ((line = input.readLine()) != null) {
				if (line.length() > 0 && line.charAt(0) != '#') {
					StringTokenizer st = new StringTokenizer(line);
					String address = st.nextToken();
					if (st.hasMoreTokens()) {
						String name = st.nextToken();
						if (st.hasMoreTokens()) {
							String comment  = st.nextToken();
							if (comment.charAt(0) != '#') {
								System.err.println("Format error (code 1).");
								errFlag = 1;
								break;
							}
							else {
								strArray[count] = address+" "+name;
							}
						}
						else {
							strArray[count] = address+" "+name;
						}
					}
					else {
						System.err.println("Not implemented error (code 4).");
						errFlag = 1;
						break;
					}
					count++;
				}	
			}
			
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (errFlag == 0) {
			return strArray;
		}
		else {
			strArray = new String[1];
			strArray[0] = "";
			return strArray;
		}

	}

}
