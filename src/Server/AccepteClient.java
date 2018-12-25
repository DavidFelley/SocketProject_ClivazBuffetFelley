package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private String validation = "";

	//Constructor
	public AccepteClient (Socket clientSocketOnServer, ArrayList<AccepteClient> clientsConnected, ServerFrame sf, Serialize serialize)
	{
		this.clientSocketOnServer = clientSocketOnServer;
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
			ois = new ObjectInputStream(clientSocketOnServer.getInputStream());

			client = (Client) ois.readObject();
			
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
					serialize.serializeObject(list);
					validation = "1";
				}
			}
			
			validate.println(validation);
			validate.flush();

			//Si le client est validé
			if (validation.equals("1"))
			{
				
				//ICI ON DOIT DONNER AU CLIENT QUI VIENT DE SE CONNECTER LA LISTE DES CLIENTS DEJA CO
				//+ ON DOIT DONNER AUX CLIENTS DEJA CO LE NOUVEAU CONNECTE
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