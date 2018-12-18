package Server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server  {

	Socket clientSocket = null;
	BufferedReader buffin = null;
	PrintWriter pout = null;

	BufferedWriter write = null;
	PrintWriter write2 = null;




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
				clientSocket = mySkServer.accept();

				System.out.println("connection request received");

				Thread t = new Thread(new AccepteClient(clientSocket,ClientNo, frame));


				
				Register.register(clientSocket, ClientNo);
				ClientNo++;
				//starting the thread
				t.start();
				
				while(true)
				{
					//create an input stream to read data from the server
					buffin = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));

					//open the output data stream to write on the client
					pout = new PrintWriter(clientSocket.getOutputStream());


					pout.println("miam");
					pout.flush();

					System.out.println(buffin.readLine());
				}
			
			}
			
			

		} catch (IOException e) {

			e.printStackTrace();
		} 

	}



}