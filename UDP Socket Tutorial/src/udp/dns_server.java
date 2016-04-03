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
	static String hostFile;

public static void main(String[] args)throws Exception{


	//Command names + Command line processing
	/*
	 * FORMAT
	 * (1) accept a port number with -p parameter
	 * (2) accept hostfile with -f parameter
	 */

	int port = 0; //port numberwi
	
	//HostFile need to insert bw that reads from plain txt and stores input

	try {		
		if(args.length == 0) // error checking, can't be blank
		{
			port = 42069;
			hostFile = "hosts.txt";
		} else if( (args.length == 2) && args[0].equals("-p")) {
			port = Integer.parseInt(args[1]);
			System.out.println("Got PORT NUMBER = " + port);
		} else if(args.length == 2 && args[0].equals("-f")){
			port = 42069;
			hostFile = args[0];
		} else if((args.length == 4) && args[0].equals("-p") && args[2].equals("-f")){
			port = Integer.parseInt(args[1]);
			hostFile = args[3];
			System.out.println("Got PORT NUMBER = " + port);
		} else if((args.length == 4) && args[0].equals("-f") && args[2].equals("-p")) {
			port = Integer.parseInt(args[3]);
			hostFile = args[1];
		} else {
			System.out.println("You're stupid. Do it right.");
			System.exit(1);
		}
	} catch(NumberFormatException nfe) {
		System.out.println("You're stupid. Do it right.");
		System.exit(1);
	}


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

	//Query String begins on index 12 (Index 12 is the byte count, whereas 13 is the start of the String)
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

	//Question Type


	int QType = ((pbuf[start] & 0xff) << 8) + (pbuf[start+1] & 0xff);

	for(int j=0; j <16; j++){
		if(j != 15){
			QType &= ~(1<<(15-j));
		} else {
			QType |= (1<<(15-j));
		}
	}

	pbuf[start] = (byte) ((QType >> 8) & 0xff);
	pbuf[start+1] = (byte) (QType & 0xff);

	int QClass = ((pbuf[start+2] & 0xff) << 8) + (pbuf[start+3] & 0xff);

	for(int j=0; j <16; j++){
		if(j != 15){
			QClass &= ~(1<<(15-j));
		} else {
			QClass |= (1<<(15-j));
		}
	}

	pbuf[start+2] = (byte) ((QClass >> 8) & 0xff);
	pbuf[start+3] = (byte) (QClass & 0xff);
	System.out.println("");

	responseSize = start + 4;

	System.out.println("The domain name you are looking for is: " + domainName);

	File f = new File(hostFile);
	//String[] domains = parser.parse(f);
	Domain[] domains  = parser.parse(f);
	boolean domainExists = false;

	for(int i = 0; i < domains.length; i++){
		String ip = domains[i].getAddress();
		String name = domains[i].getHost();

		if(domainName.equalsIgnoreCase(name)){
			domainExists = true;
			//set qr to 1
			v |= (1<<(15-0));
			//set aa to 1
			v |= (1<<(15-5));
			//set tc to 0
			v &= ~(1<<(15-6));
			//set ra to 0
			v &= ~(1<<(15-8));
			//set rd to 0
			v &= ~(1<<(15-7));

			//Set rcode to 3 for error (address not found)
			v &= ~(1<<(15-12)); 
			v &= ~(1<<(15-13));
			v &= ~(1<<(15-14));
			v &= ~(1<<(15-15));

			pbuf[2] = (byte) ((v >> 8) & 0xff);
			pbuf[3] = (byte) (v & 0xff);

			int ANCount = ((pbuf[6] & 0xff) << 8) + (pbuf[7] & 0xff);
			System.out.println("ANCount 16 bits = " + ANCount);

			for(int j=0; j <16; j++){
				if(j != 15){
					ANCount &= ~(1<<(15-j));
				} else {
					ANCount |= (1<<(15-j));
				}
			}

			pbuf[6] = (byte) ((ANCount >> 8) & 0xff);
			pbuf[7] = (byte) (ANCount & 0xff);

			System.out.println("The ip address: " + ip);



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


			pbuf[curr] = (byte) one;
			pbuf[curr+1] = (byte) two;
			pbuf[curr+2] = (byte) three;
			pbuf[curr+3] = (byte) four;
			responseSize = curr+4;


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

	if(domainExists == false) {

		//set qr to 1
		v |= (1<<(15-0));
		//set aa to 1
		v |= (1<<(15-5));
		//set tc to 0
		v &= ~(1<<(15-6));
		//set ra to 0
		v &= ~(1<<(15-8));
		//set rd to 0
		v &= ~(1<<(15-7));

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
		System.out.println("ANCount 16 bits = " + ANCount);

		for(int j=0; j <16; j++){
			ANCount &= ~(1<<(15-j));
		}

		pbuf[6] = (byte) ((ANCount >> 8) & 0xff);
		pbuf[7] = (byte) (ANCount & 0xff);

	}

	return pbuf;

}


}