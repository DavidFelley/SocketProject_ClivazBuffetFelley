package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import Client.Client;

public class AccepteClient extends Thread 
{
	private Serialize serialize;
	private ServerFrame sf = null;
	private Socket clientSocketOnServer = null;
	private Client client = null;
	private ArrayList<Client> list = null;

	//Envoie d'info pour le client
	private PrintWriter validate = null;
	private ObjectInputStream in = null;
	private String validation = "";
	private ArrayList<AccepteClient> clientsConnected;

	//Constructor
	public AccepteClient (Socket clientSocketOnServer, ArrayList<AccepteClient> clientsConnected, ServerFrame sf, Serialize serialize)
	{
		this.clientSocketOnServer = clientSocketOnServer;
		this.clientsConnected = clientsConnected;
		this.sf = sf;
		this.serialize = serialize;
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
			
			list = (ArrayList<Client>)(serialize.deSerializeObject());

			if (client.isExist()) 
			{
				//Controle si le client existe deja
				for (Client clientRegistered : list)
				{
					if(clientRegistered.getName().equalsIgnoreCase(client.getName()))
					{
						if(clientRegistered.getMdp().equals(client.getMdp()))
						{
							System.out.println("Mot de passe correct");
							validation = "1";

							//Affiche la confirmation du password dans le server
							sf.createLabel("Password Validate");
							break;
						}
						else
						{
							//Affiche l'échec du password
							sf.createLabel("False Password");
							System.out.println("Mot de passe incorrect");
							validation = "0";
							break;
						}
					}
					validation = "0";
				}
			}
			else
			{
				//Controle si le client existe deja
				for (Client clientRegistered : list) 
				{
					if(clientRegistered.getName().equalsIgnoreCase(client.getName()))
					{
						validation = "2";
						break;
					}
				}
				
				if (!validation.equals("2")) 
				{
					Client newClient = new Client(client.getName(), client.getMdp());
					list.add(newClient);
					validation = "1";
				}
			
			}
			
			serialize.serializeObject(list);
			validate.println(validation);
			validate.flush();

			//Si le client est validé
			if (validation.equals("1"))
			{
				//On l'ajoute à la liste des threads actifs
				clientsConnected.add(this);
				sf.createLabel(client.getName()+" is connected");
				while(true)
				{
					//Le server écoute en permanence
				}
			}
			else
			{
				clientSocketOnServer.close();
				this.sleep(3000);
				this.stop();
			}
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