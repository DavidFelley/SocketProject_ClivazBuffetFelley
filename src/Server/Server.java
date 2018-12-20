package Server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import Client.Client;

public class Server  {

	Socket clientSocket = null;

	BufferedWriter write = null;
	PrintWriter write2 = null;
	ArrayList<Client> clients= null;
	
	Serialize serialize = new Serialize("Client//client.zer");


	@SuppressWarnings("unchecked")
	public Server() 
	{
		System.out.println("Liste des Users : ");

		clients=(ArrayList<Client>)(serialize.deSerializeObject());
		for (Client client : clients) 
		{
			System.out.println(client.getName()+" "+client.getMdp()+" "+client.getIp());
		}
		
		InetAddress localAddress = null;
		ServerSocket mySkServer;
		String interfaceName = "eth1";

		ServerFrame frame = new ServerFrame();

		int ClientNo = 1;

		try {
			serialize.createFile();
			
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

				Thread t = new Thread(new AccepteClient(clientSocket,ClientNo, frame, clients));




				//starting the thread
				t.start();




			}



		} catch (IOException e) {

			e.printStackTrace();
		} 



	}

	

}