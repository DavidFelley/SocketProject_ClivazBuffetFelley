package Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

import Client.Client;

public class AccepteClient extends Thread {

	public static ArrayList<Client> list  ;

	private Socket clientSocketOnServer;
	private Client client;
	private ArrayList<Client> clients= null;

	//Envoie d'info pour le server
	private PrintWriter validate = null;
	private ObjectInputStream in = null;
	private String validation = "";
	private ArrayList<AccepteClient> clientsConnected;
	Serialize serialize = new Serialize("Client//client.zer");

	//Constructor
	public AccepteClient (Socket clientSocketOnServer, ArrayList<AccepteClient> clientsConnected)
	{
		this.clientSocketOnServer = clientSocketOnServer;
		this.clientsConnected = clientsConnected;
	}

	//overwrite the thread run()
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void run() 
	{
		try 
		{
			validate = new PrintWriter(clientSocketOnServer.getOutputStream());
			in = new ObjectInputStream(clientSocketOnServer.getInputStream());

			client = (Client) in.readObject();
			
			//Controle si le client existe deja
			clients = (ArrayList<Client>)(serialize.deSerializeObject());

			for (Client user : clients) 
			{
				if(user.getName().equals(client.getName()))
				{
					System.out.println("le client existe deja");

					if(user.getMdp().equals(client.getMdp()))
					{
						System.out.println("Mot de passe correct");
						validation = "1";

						//Affiche la confirmation du password dans le server
						//frame.createLabel("Password Validate");
						//frame.createLabel(client.getName()+" is connected");
						break;
					}
					else
					{
						//Affiche l'échec du password
						//frame.createLabel("False Password");
						System.out.println("Mot de passe incorrect");
						validation = "0";
						break;
					}
				}
				validation = "0";
			}

			validate.println(validation);
			validate.flush();
			
			if (validation.equals("1"))
			{
				clientsConnected.add(this);
				while(true)
				{
					
				}
			}
			else
			{
				clientSocketOnServer.close();
				this.sleep(3000);
				this.stop();
				
			}
			

			//list.add(client);
			//serialize.serializeObject(list);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}