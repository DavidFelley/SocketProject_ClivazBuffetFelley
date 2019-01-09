package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server 
{
	private ServerFrame sf;
	private Socket clientSocket = null;
	private Serialize serialize = new Serialize();
	private ArrayList<AccepteClient> listClientsConnected = new ArrayList<>();
	private Logging log = new Logging();

	public Server() 
	{
		launch();
	}

	/**
	 * Method launching the server
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void launch() 
	{
		/**
		 * Initialization of the Frame, Logger File and Serialization File
		 * 
		 */
		sf = new ServerFrame();
		log.createLogger();
		serialize.createFile();

		ServerSocket mySkServer;

		try 
		{
			mySkServer = new ServerSocket(45000, 5);

			/**
			 * Waiting for a Client connection
			 * 
			 */
			while (true) 
			{
				clientSocket = mySkServer.accept();				
				log.write("Tentative de connection d'un client", "info");
				Thread t = new AccepteClient(clientSocket, listClientsConnected, sf, serialize, log);
				t.start();
			}
		} 
		catch (IOException e) 
		{
			log.write(e.getMessage().toString(), "severe");
		}
	}
}