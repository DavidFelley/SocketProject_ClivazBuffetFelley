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

	@SuppressWarnings("unchecked")
	private void launch() 
	{
		//Initialise la frame
		sf = new ServerFrame();

		//Initialise le fichier de logs
		log.createLogger();

		//cree les fichiers de sauvegarde utilisateurs si n'existent pas
		serialize.createFile();

		ServerSocket mySkServer;

		try 
		{
			mySkServer = new ServerSocket(45000, 5);

			//wait for a client connection
			while (true) 
			{
				clientSocket = mySkServer.accept();
				System.out.println("connection request received");
				
				log.write("Tentative de connection d'un client", "info");
				Thread t = new AccepteClient(clientSocket, listClientsConnected, sf, serialize, log);
				
				//starting the thread
				t.start();
			}
		} 
		catch (IOException e) 
		{
			log.write(e.getMessage().toString(), "severe");
		}
	}
}