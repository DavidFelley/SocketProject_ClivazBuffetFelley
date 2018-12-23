package Server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import Client.Client;

public class Server  
{
	private ServerFrame sf;
	private Socket clientSocket = null;
	private ArrayList<Client> clients= null;
	private Serialize serialize = new Serialize("Client//client.zer");
	private ArrayList<AccepteClient> clientsConnected;

	public Server() 
	{
		launch();
	}

	@SuppressWarnings("unchecked")
	private void launch() 
	{
		//Initialise la frame
		sf = new ServerFrame();
		
		//Affiche les users existants dans la frame server
		sf.createLabel("User list : ");

		clients = (ArrayList<Client>)(serialize.deSerializeObject());

		for (Client client : clients)
			sf.createLabel(client.getName());
	
		ServerSocket mySkServer;

		try 
		{
			//Warning : the backlog value 2nd parameter is handled by the implementation
			mySkServer = new ServerSocket(45000,10);

			//wait for a client connection
			while(true)
			{
				clientSocket = mySkServer.accept();
				System.out.println("connection request received");

				Thread t = new AccepteClient(clientSocket, clientsConnected);

				//starting the thread
				t.start();
			}
		} 

		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}
}