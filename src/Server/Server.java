package Server;
import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
	
	
	
	public Server() 
	{
	
		InetAddress localAddress = null;
		ServerSocket mySkServer;
		String interfaceName = "eth1";
		
		Frame frame = new Frame();

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
			mySkServer = new ServerSocket(45000,10,localAddress);
			
			frame.createLabel("Default Timeout :" + mySkServer.getSoTimeout());
			frame.createLabel("Used IpAddress :" + mySkServer.getInetAddress());

			frame.createLabel("Listening to Port :" + mySkServer.getLocalPort());

		
			
		
			//wait for a client connection
			while(true)
			{
     		   Socket clientSocket = mySkServer.accept();
     		  
               System.out.println("connection request received");
               
               Thread t = new Thread(new AccepteClient(clientSocket,ClientNo, frame));
               
               Register.register(clientSocket, ClientNo);
               ClientNo++;
     		   //starting the thread
    		   t.start();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}

	
	
}