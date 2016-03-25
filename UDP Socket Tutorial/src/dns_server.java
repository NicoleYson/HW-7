import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

//Group members: NICOLE YSON, MIKHAIL PRIGOZHIY, ...


public class dns_server{ //it doesn't want me to name it dns-server
	public static void main(String[] args)throws Exception{

		
		//Command names + Command line processing
			/*
			 * FORMAT
			 * (1) accept a port number with -p parameter
			 * (2) accept hostfile with -f parameter
			 */
		
		int port; //port number
		
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
		DatagramSocket serverSocket = new DatagramSocket(); 
			byte[] recieveData = new byte[1024]; 
			byte[] sendData = new byte[1024];
			while(true){
				DatagramPacket recievePacket = new DatagramPacket(recieveData, recieveData.length);
				serverSocket.receive(recievePacket);
				String sentence = new String(recievePacket.getData());
				System.out.println("Recieved: " + sentence); 
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
}