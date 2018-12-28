package Server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import Client.Client;

public class Server  
{
	private ServerFrame sf;
	private Socket clientSocket = null;
	private Serialize serialize = new Serialize();
	private ArrayList<AccepteClient> clientsConnected = new ArrayList<>();

	public Server() 
	{
		launch();
	}

	@SuppressWarnings("unchecked")
	private void launch() 
	{
		//Initialise la frame
		sf = new ServerFrame();
		
		//cree les fichiers de sauvegarde utilisateurs si n'existent pas
		serialize.createFile();
		
		ServerSocket mySkServer;

		try 
		{
			mySkServer = new ServerSocket(45000,5);

			//wait for a client connection
			while(true)
			{
				clientSocket = mySkServer.accept();
				System.out.println("connection request received");
				
				Thread t = new AccepteClient(clientSocket, clientsConnected, sf, serialize);

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