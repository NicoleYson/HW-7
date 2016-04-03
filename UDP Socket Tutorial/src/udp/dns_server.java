package udp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

//Group members: MIKHAIL PRIGOZHIY, NICOLE YSON, JON GETAHUN, JONATHAN CAVERLY


public class dns_server{ static //it doesn't want me to name it dns-server
	
	int responseSize;
	
	public static void main(String[] args)throws Exception{
	
		
		//Command names + Command line processing
			/*
			 * FORMAT
			 * (1) accept a port number with -p parameter
			 * (2) accept hostfile with -f parameter
			 */
		
		int port = 0; //port number
		
		//HostFile need to insert bw that reads from plain txt and stores input
		
				
		if(args.length == 0) // error checking, can't be blank
		{
			port = 12345;
			/*System.out.println("error: invalid input");
			System.exit(1);
			return;*/
		}
		
		/*
		if(args[0].equals("-p") && (args.length == 2)) 
		{
			port = Integer.parseInt(args[1]);
			System.out.println("Got PORT NUMBER = " + port);
		}
		
		if(args[0].equals("-p") && (args.length == 4)) 
		{
			port = Integer.parseInt(args[1]);
			System.out.println("Got PORT NUMBER = " + port);
			if(args[2].equals("-f") && args.length > 3)
			{
				// hostFile = ;							
			}
		}
		
		if(args[0].equals("-f") && (args.length == 4)) 
		{
			System.out.println("Got HOST FILE = "  ); //add variable for txt file here
			if(args[2].equals("-p") && args.length > 3)
			{
				port = Integer.parseInt(args[1]);
				// hostFile = ;							
			}
		}
		*/
		
	//Simple server that receives a UDP packet on a port
		DatagramSocket serverSocket = new DatagramSocket(port);
		System.out.println(serverSocket.getPort());
		String Website;
		
			byte[] recieveData = new byte[1024]; 
			byte[] sendData = new byte[1024];
			while(true){
				DatagramPacket recievePacket = new DatagramPacket(recieveData, recieveData.length);
				System.out.println("Waiting for packet.");
				serverSocket.receive(recievePacket);
				sendData = examine(recievePacket.getData(), recievePacket.getLength());
				System.out.println(recievePacket.getData().length);
				System.out.println("Packet received.");
				String sentence = new String(Arrays.copyOfRange(recieveData, 13, 24), StandardCharsets.UTF_8);
				//System.out.println("Recieved: " + sentence); 
				//Website = Arrays.copyOfRange(recieveData, 12, 16);
				//System.out.println("The website is: " + Website.toString());
				InetAddress IPAddress = recievePacket.getAddress();
				int port1 = recievePacket.getPort();
				
				DatagramPacket sendPacket = new DatagramPacket(sendData, responseSize);
				sendPacket.setPort(port1);
				sendPacket.setAddress(IPAddress);
				serverSocket.send(sendPacket);
				 
			}
	//Parse the DNS message
		//Parse the DNS header
			//Error check for network vs host byte order
		// Parse out Query string
	
			
	//Create a response
			
	//Process the Hosts file
			
	}
	

	static byte[] examine(byte[] pbuf, int plen) {

		// dump what we have for debugging
		// a line of hex first, then a line of characters (where possible)

		System.out.println("Received: " + plen + " bytes: ");
		for (int i=0; i < plen; i++)
			System.out.print(String.format("%02x ", pbuf[i]));
		System.out.println("");
		for (int i=0; i < plen; i++)
			if ((pbuf[i] <= ' ') || (pbuf[i] > '~'))
				System.out.print(String.format("%02x ", pbuf[i]));
			else
				System.out.print(String.format("%c  ", pbuf[i]));
		System.out.println("");

		// look at the bytes as big endian shorts
		// the wrap() method uses an existing byte array for the buffer

		short[] shorts = new short[plen/2];
		ByteBuffer.wrap(pbuf).order(ByteOrder.BIG_ENDIAN).asShortBuffer().get(shorts);

		// dump our buffer as shorts
		for (int i=0; i < plen/2; i++)
			System.out.println("short[" + i + "] = " + shorts[i]);

		// another way we can create shorts is by manually putting 2 bytes together
		// internet format is big endian - the first byte has the more significant value
		// this one produces an unsigned result

		int short_value = ((pbuf[0] & 0xff) << 8) + (pbuf[1] & 0xff);
		System.out.println("first 16 bits = " + short_value);

		System.out.println();
		// demo of extracting bit fields (e.g., for dns)
		// grab the second group of two bytes and treat it as a 16 bit set of bits
		// bits are indexed left to right
		int v = (pbuf[2] & 0xff) << 8 | (pbuf[3] & 0xff);
		for (int i=0; i < 16; i++) {
			System.out.println("bit[" + i + "] = " + (v>>(15-i) & 1));
			// System.out.println("bit[" + i + "] = " + (v & 1<<(15-i)));
		}

		// for example qr, query/response = bit 0 
		int qr = ((v >> 15-0) & 1);
		
		System.out.println("qr = " + qr);
		
		//get opcode bits
		int[] opcode = new int[4];
		
		opcode[0] = ((v >> 15-1) & 1);
		opcode[1] = ((v >> 15-2) & 1);
		opcode[2] = ((v >> 15-3) & 1);
		opcode[3] = ((v >> 15-4) & 1);
		
		
		//Convert the opcodeValue to decimal just for observational purposes
		int opcodeVal = 0;
		
		for (int i = 3; i >= 0; i--){
			if(opcode[i] == 1){
				opcodeVal = (int) (opcodeVal + Math.pow(2, 3-i));
			}
		}
		
		
		System.out.println("Opcode value is: " + opcodeVal);

		int AA = ((v >> 15-5) & 1);
		int TC = ((v >> 15-6) & 1);
		int RD = ((v >> 15-7) & 1);
		int RA = ((v >> 15-8) & 1);
		
		int[] Z = new int[3];
		
		Z[0] = ((v >> 15-9) & 1);
		Z[1] = ((v >> 15-10) & 1);
		Z[2] = ((v >> 15-11) & 1);
		
		
		int ZVal = 0;
		
		for (int i = 2; i >= 0; i--){
			if(Z[i] == 1){
				ZVal = (int) (ZVal + Math.pow(2, 2-i));
			}
		}
		
	int[] rcode = new int[4];
		
		rcode[0] = ((v >> 15-12) & 1);
		rcode[1] = ((v >> 15-13) & 1);
		rcode[2] = ((v >> 15-14) & 1);
		rcode[3] = ((v >> 15-15) & 1);
		
		
		int rcodeVal = 0;
		
		for (int i = 3; i >= 0; i--){
			if(rcode[i] == 1){
				rcodeVal = (int) (rcodeVal + Math.pow(2, 3-i));
			}
		}
		
		System.out.println("All Value: \nqr: " + qr + "\nOpcode: " + opcodeVal + "\nAA: " + AA 
				+ "\nTC: " + TC + "\nRD: " + RD + "\nRA: " + RA + "\nZ: " + ZVal + "\nrcode: " + rcodeVal);
		
		int short_value1 = ((pbuf[4] & 0xff) << 8) + (pbuf[5] & 0xff);
		System.out.println("QDCount 16 bits = " + short_value1);
		
		int short_value2 = ((pbuf[6] & 0xff) << 8) + (pbuf[7] & 0xff);
		System.out.println("ANCount 16 bits = " + short_value2);
		
		for(int j=0; j <16; j++){
			if(j != 15){
				short_value2 &= ~(1<<(15-j));
			} else {
				short_value2 |= (1<<(15-j));
			}
		}
		
		pbuf[6] = (byte) ((short_value2 >> 8) & 0xff);
		pbuf[7] = (byte) (short_value2 & 0xff);
		
		int short_value3 = ((pbuf[8] & 0xff) << 8) + (pbuf[9] & 0xff);
		System.out.println("NSCount 16 bits = " + short_value3);
		
		int short_value4 = ((pbuf[10] & 0xff) << 8) + (pbuf[11] & 0xff);
		System.out.println("ARCount 16 bits = " + short_value4);
		
		int short_value5 = ((pbuf[10] & 0xff) << 8) + (pbuf[11] & 0xff);
		System.out.println("Request Name 16 bits = " + short_value5);
		
		int size = Integer.parseInt(String.format("%d" , pbuf[12]));
		int start = 13;
		int end;
		
		
		System.out.print(String.format("%d ", pbuf[13]));
		int check = 0;
		String domainName = "";
		
		do{
			
			if(check == 1){
				domainName = domainName.concat(".");
				System.out.print(".");
			} else {
				check = 1;
			}
		
		for (int i = 0; i < size; i++){
			domainName = domainName.concat(String.format("%c", pbuf[start]));
			System.out.print(String.format("%c", pbuf[start]));
			start++;
		}
		
		
		size = Integer.parseInt(String.format("%d", pbuf[start]));
		end = start++;
		
		} while(!((pbuf[start] <= ' ') || (pbuf[start] > '~'))); //do a comparison to 0? maybe later.
		System.out.println();
		
		//Question section
		
		
		int short_value6 = ((pbuf[start] & 0xff) << 8) + (pbuf[start+1] & 0xff);
		System.out.println("QType Name 16 bits = " + short_value6);
		
		for(int j=0; j <16; j++){
			if(j != 15){
				short_value6 &= ~(1<<(15-j));
			} else {
				short_value6 |= (1<<(15-j));
			}
		}
		
		pbuf[start] = (byte) ((short_value6 >> 8) & 0xff);
		pbuf[start+1] = (byte) (short_value6 & 0xff);
		
		int short_value7 = ((pbuf[start+2] & 0xff) << 8) + (pbuf[start+3] & 0xff);
		System.out.println("QClass Name 16 bits = " + short_value7);
		
		for(int j=0; j <16; j++){
			if(j != 15){
				short_value7 &= ~(1<<(15-j));
			} else {
				short_value7 |= (1<<(15-j));
			}
		}
		
		pbuf[start+2] = (byte) ((short_value6 >> 8) & 0xff);
		pbuf[start+3] = (byte) (short_value6 & 0xff);
		System.out.println("");
		
		responseSize = start + 4;
		
		// for example rd, recursion desired = bit 7
		int rd = ((v >> 15-7) & 1);
		//System.out.println("rd = " + rd);

		// example of setting a bit. Let's set qr to 1
		v |= (1<<(15-0));
		v |= (1<<(15-5));
		

		v &= ~(1<<(15-7));
		
		pbuf[2] = (byte) ((v >> 8) & 0xff);
		pbuf[3] = (byte) (v & 0xff);
	
		
		for (int i=0; i < 16; i++) {
			System.out.println("bit[" + i + "] = " + (v>>(15-i) & 1));
			// System.out.println("bit[" + i + "] = " + (v & 1<<(15-i)));
		}
		
		
		rcode[0] = ((v >> 15-12) & 1);
		rcode[1] = ((v >> 15-13) & 1);
		rcode[2] = ((v >> 15-14) & 1);
		rcode[3] = ((v >> 15-15) & 1);
		
		
		rcodeVal = 0;
		
		for (int i = 3; i >= 0; i--){
			if(rcode[i] == 1){
				rcodeVal = (int) (rcodeVal + Math.pow(2, 3-i));
			}
		}
		
		System.out.println("new rcodeVal: " + rcodeVal);

		System.out.println(start);
		
		System.out.println("The domain name you are looking for is: " + domainName);
		
		File f = new File("hosts.txt");
		//String[] domains = parser.parse(f);
		Domain[] domains  = parser.parse(f);
		boolean domainExists = false;
	
		for(int i = 0; i < domains.length; i++){
			String ip = domains[i].getAddress();
			String name = domains[i].getHost();

			if(domainName.equalsIgnoreCase(name)){
				domainExists = true;
				System.out.println("The ip address: " + ip);
				
				//set rcode to 0
				v &= ~(1<<(15-12)); 
				v &= ~(1<<(15-13));
				v &= (1<<(15-14));
				v &= (1<<(15-15));
				
				pbuf[2] = (byte) ((v >> 8) & 0xff);
				pbuf[3] = (byte) (v & 0xff);

				int l = 0;
							
				//offset
				for(int j = 12; j < end+1; j++){
					pbuf[start+4+l] = pbuf[j];
					l++;
				}
				
				int curr = start + 4 + l;

				
				int Type = ((pbuf[curr] & 0xff) << 8) + (pbuf[curr+1] & 0xff);
				
				for(int j=0; j <16; j++){
					if(j != 15){
						Type &= ~(1<<(15-j));
					} else {
						Type |= (1<<(15-j));
					}
				}
				
				pbuf[curr] = (byte) ((Type >> 8) & 0xff);
				pbuf[curr+1] = (byte) (Type & 0xff);
				
				curr+=2;
				
				int classy = ((pbuf[curr] & 0xff) << 8) + (pbuf[curr+1] & 0xff);
				
				for(int j=0; j <16; j++){
					if(j != 15){
						classy &= ~(1<<(15-j));
					} else {
						classy |= (1<<(15-j));
					}
				}
				
				pbuf[curr] = (byte) ((classy >> 8) & 0xff);
				pbuf[curr+1] = (byte) (classy & 0xff);
				
				curr+=4;
				
				int ttl = ((pbuf[curr] & 0xff) << 8) + (pbuf[curr+1] & 0xff);
				
				for(int j=0; j <16; j++){
						ttl &= ~(1<<(15-j));
				}
				
				pbuf[curr] = (byte) ((ttl >> 8) & 0xff);
				pbuf[curr+1] = (byte) (ttl & 0xff);
				
				curr+=2;
				
				int rdlength = ((pbuf[curr] & 0xff) << 8) + (pbuf[curr+1] & 0xff);
				
				for(int j=0; j <16; j++){
					if(j != 13){
						rdlength &= ~(1<<(15-j));
					} else {
						rdlength |= (1<<(15-j));
					}
				}
				
				pbuf[curr] = (byte) ((rdlength >> 8) & 0xff);
				pbuf[curr+1] = (byte) (rdlength & 0xff);
		
			
				
				int one = Integer.parseInt(ip.substring(0, ip.indexOf(".")));
				ip = ip.substring(ip.indexOf(".") + 1);
				int two = Integer.parseInt(ip.substring(0, ip.indexOf(".")));
				ip = ip.substring(ip.indexOf(".") + 1);
				int three = Integer.parseInt(ip.substring(0, ip.indexOf(".")));
				ip = ip.substring(ip.indexOf(".") + 1);
				int four = Integer.parseInt(ip);
				
				System.out.println(one + "." + two + "." + three+ "." + four);
				
				curr+=2;
				
				int responseName = ((pbuf[curr] & 0xff) << 8) + (pbuf[curr+1] & 0xff);				
				
				
				int responseName2 = ((pbuf[curr+2] & 0xff) << 8) + (pbuf[curr+3] & 0xff);				
				
				
				
				//pbuf[curr] = (byte) ((responseName >> 8) & 0xff);
				//pbuf[curr+1] = (byte) (responseName & 0xff);
				
				pbuf[curr] = (byte) one;
				pbuf[curr+1] = (byte) two;
				pbuf[curr+2] = (byte) three;
				pbuf[curr+3] = (byte) four;
				responseSize = curr+4;
				
			
				
				//pbuf[curr+2] = (byte) ((responseName >> 8) & 0xff);
				//pbuf[curr+3] = (byte) (responseName & 0xff);
				
				for (int j=0; j < 16; j++) {
					System.out.println("bit[" + j + "] = " + (responseName>>(15-j) & 1));
					// System.out.println("bit[" + i + "] = " + (v & 1<<(15-i)));
				}
				
				for (int j=0; j < 16; j++) {
					System.out.println("bit[" + j + "] = " + (responseName2>>(15-j) & 1));
					// System.out.println("bit[" + i + "] = " + (v & 1<<(15-i)));
				}
				
		
				
				
				for (int j=0; j <= 47; j++)
						System.out.print(String.format("%02x ", pbuf[j]));
				System.out.println("");
				
				for (int j=0; j <= 47; j++)
					if ((pbuf[j] <= ' ') || (pbuf[j] > '~')){
						System.out.print(String.format("%02x ", pbuf[j]));}
					else{
						System.out.print(String.format("%c  ", pbuf[j]));}
				System.out.println("");
				
				break;
				
			} 			
			
		}

		
		if(!domainExists) {
			//Set rcode to 3 for error (address not found)
			v &= ~(1<<(15-12)); 
			v &= ~(1<<(15-13));
			v |= (1<<(15-14));
			v |= (1<<(15-15));
			
			for (int x=0; x < 16; x++) {
				System.out.println("bit[" + x + "] = " + (v>>(15-x) & 1));
				// System.out.println("bit[" + i + "] = " + (v & 1<<(15-i)));
			}
			
			pbuf[2] = (byte) ((v >> 8) & 0xff);
			pbuf[3] = (byte) (v & 0xff);
	
		//set ANCount to 0
			
			int ANCount = ((pbuf[6] & 0xff) << 8) + (pbuf[7] & 0xff);
			System.out.println("ANCount 16 bits = " + short_value2);
			
			for(int j=0; j <16; j++){
					short_value2 &= ~(1<<(15-j));
			}
			
			pbuf[6] = (byte) ((short_value2 >> 8) & 0xff);
			pbuf[7] = (byte) (short_value2 & 0xff);
			
		}

	
		
		return pbuf;
		
	}
}