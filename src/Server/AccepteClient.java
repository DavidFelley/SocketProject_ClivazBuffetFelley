package Server;

import java.io.BufferedWriter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

import Client.Client;

public class AccepteClient implements Runnable {

	public static ArrayList<Client> list  ;

	private Socket clientSocketOnServer;
	private int clientNumber;
	private ServerFrame frame ;
	private Client client;

	private String clientID;
	private String clientIP;
	private ArrayList<Client> clients= null;

	

	//Envoie d'info pour le server
	private BufferedWriter buffinOut = null;
	private PrintWriter validate = null;
	private ObjectInputStream in = null;
	private String validation = "";



	Serialize serialize = new Serialize("Client//client.zer");

	//Constructor
	public AccepteClient (Socket clientSocketOnServer,  ServerFrame frame,ArrayList<Client> list)
	{
		this.clientSocketOnServer = clientSocketOnServer;
		this.frame = frame;
		this.list=list;
	}
	//overwrite the thread run()
	@SuppressWarnings("unchecked")
	public void run() 
	{

		try {
			

			PrintWriter validate = new PrintWriter(clientSocketOnServer.getOutputStream());
			in = new ObjectInputStream(clientSocketOnServer.getInputStream());
			while(true)
			{




				try {
					client = (Client) in.readObject();
					
					frame.createLabel(client.getName()+" is connected");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				//Controle si le client existe deja
				clients=(ArrayList<Client>)(serialize.deSerializeObject());


				for (Client user : clients) 
				{
					
					
					if(user.getName().equals(client.getName()))
					{
						System.out.println("le client existe deja");
						
						if(user.getMdp().equals(client.getMdp()))
						{
							System.out.println("Mot de passe correct");
							validation = "1";
							break;

						}
						else
						{
							System.out.println("Mot de passe incorrect");
							validation = "0";
							break;
						}
						
					}
					validation = "0";

				}
				
				validate.println(validation);
				validate.flush();
				System.out.println("validation" + validation);


				//list.add(client);


				serialize.serializeObject(list);

				

				
			} 
		


		}catch (IOException e) 
		{

			e.printStackTrace();
		} 


	}



}