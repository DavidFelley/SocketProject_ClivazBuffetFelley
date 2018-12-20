package Server;
import java.awt.Frame;
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

	private Socket clientSocket = null;
	private BufferedWriter write = null;
	private PrintWriter write2 = null;
	private ArrayList<Client> clients= null;
	
	Serialize serialize = new Serialize("Client//client.zer");
	ServerFrame frame = new ServerFrame();


	@SuppressWarnings("unchecked")
	public Server() 
	{
		frame.createLabel("User list : ");
		
		

		clients=(ArrayList<Client>)(serialize.deSerializeObject());
		for (Client client : clients) 
		{
			frame.createLabel(client.getName()+" "+client.getMdp());
		}
		
		InetAddress localAddress = null;
		ServerSocket mySkServer;
		String interfaceName = "eth1";


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

						
			//wait for a client connection
			while(true)
			{
				clientSocket = mySkServer.accept();


				Thread t = new Thread(new AccepteClient(clientSocket, frame, clients));




				//starting the thread
				t.start();




			}



		} catch (IOException e) {

			e.printStackTrace();
		} 



	}

	

}