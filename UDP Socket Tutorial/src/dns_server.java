import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

//Group members: NICOLE YSON, MIKHAIL PRIGORCODEHIY, ...


public class dns_server{ //it doesn't want me to name it dns-server
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
				examine(recievePacket.getData(), recievePacket.getLength());
				System.out.println("Packet received.");
				String sentence = new String(Arrays.copyOfRange(recieveData, 13, 24), StandardCharsets.UTF_8);
				//System.out.println("Recieved: " + sentence); 
				//Website = Arrays.copyOfRange(recieveData, 12, 16);
				//System.out.println("The website is: " + Website.toString());
				InetAddress IPAddress = recievePacket.getAddress(); 
				int port1 = recievePacket.getPort();
				 
			}
	//Parse the DNS message
		//Parse the DNS header
			//Error check for network vs host byte order
		// Parse out Query string
	
			
	//Create a response
			
	//Process the Hosts file
			
	}
	

	static void examine(byte[] pbuf, int plen) {

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
		
		int short_value3 = ((pbuf[8] & 0xff) << 8) + (pbuf[9] & 0xff);
		System.out.println("NSCount 16 bits = " + short_value3);
		
		int short_value4 = ((pbuf[10] & 0xff) << 8) + (pbuf[11] & 0xff);
		System.out.println("ARCount 16 bits = " + short_value4);
		
		int short_value5 = ((pbuf[10] & 0xff) << 8) + (pbuf[11] & 0xff);
		System.out.println("Request Name 16 bits = " + short_value5);
		
		int size = Integer.parseInt(String.format("%d" , pbuf[12]));
		int start = 13;
		
		
		System.out.print(String.format("%d ", pbuf[13]));
		int check = 0;
		
		do{
			
			if(check == 1){
				System.out.print(".");
			} else {
				check = 1;
			}
		
		for (int i = 0; i < size; i++){
			System.out.print(String.format("%c", pbuf[start]));
			start++;
		}
		
		
		size = Integer.parseInt(String.format("%d", pbuf[start]));
		start++;
		
		} while(!((pbuf[start] <= ' ') || (pbuf[start] > '~')));
		/*if ((pbuf[i] <= ' ') || (pbuf[i] > '~'))
			System.out.print(String.format("%02x ", pbuf[i]));
		else
			System.out.print(String.format("%c  ", pbuf[i]));*/
		
		System.out.println("");
		
		// for example rd, recursion desired = bit 7
		int rd = ((v >> 15-7) & 1);
		//System.out.println("rd = " + rd);

		// example of setting a bit. Let's set qr to 1
		v |= (1<<(15-0));
		v |= (1<<(15-14));
		v |= (1<<(15-15));
		
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

		
		// write v back to the packet buffer
		pbuf[2] = (byte) ((v >> 8) & 0xff);
		pbuf[3] = (byte) (v & 0xff);
		
		
	}
}