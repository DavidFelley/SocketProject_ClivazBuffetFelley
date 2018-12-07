package Client;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class ClientServer {

	public static void main(String[] args){

		InetAddress localAddress = null;
		ServerSocket mySkServer;
		String interfaceName = "eth1";

		int ClientNo = 1;
		
		try {
			NetworkInterface ni = NetworkInterface.getByName(interfaceName);
	        Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
			while(inetAddresses.hasMoreElements()) {
	            InetAddress ia = inetAddresses.nextElement();
	            
	            if(!ia.isLinkLocalAddress()) {
	               if(!ia.isLoopbackAddress()) {
	            	   System.out.println(ni.getName() + "->IP: " + ia.getHostAddress());
	            	   localAddress = ia;
	               }
	            }   
            }
			
			//Warning : the backlog value (2nd parameter is handled by the implementation
			mySkServer = new ServerSocket(45007,10,localAddress);
			System.out.println("Default Timeout :" + mySkServer.getSoTimeout());
			System.out.println("Used IpAddress :" + mySkServer.getInetAddress());
			System.out.println("Listening to Port :" + mySkServer.getLocalPort());
			
			//wait for a client connection
			while(true)
			{
     		   Socket clientSocket = mySkServer.accept();
               System.out.println("connection request received");
               Thread t = new Thread(new AcceptClient(clientSocket,ClientNo));
               ClientNo++;
     		   //starting the thread
    		   t.start();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}