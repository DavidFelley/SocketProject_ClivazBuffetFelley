package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

import Client.Client;
import Client.CloseMyConnection;
import Client.Message;

public class AccepteClient extends Thread
{
	private Serialize serialize;
	private ServerFrame sf = null;
	private Socket clientSocketOnServer = null;
	private ArrayList<AccepteClient> listClientsConnected = null;
	private Client myClient = null;
	private ArrayList<Client> listOfClient = null;
	private BufferedReader buffin = null;

	//Envoie d'info pour le client
	private PrintWriter validate = null;
	private ObjectInputStream inStream = null;
	private ObjectOutputStream outStream = null;
	private int validation = -1;

	//Constructor
	public AccepteClient (Socket clientSocketOnServer, ArrayList<AccepteClient> clientsConnected, ServerFrame sf, Serialize serialize)
	{
		this.clientSocketOnServer = clientSocketOnServer;
		this.sf = sf;
		this.serialize = serialize;
		this.listClientsConnected = clientsConnected;
	}

	//overwrite the thread run()
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void run()
	{
		try
		{
			outStream = new ObjectOutputStream(clientSocketOnServer.getOutputStream());
			validate = new PrintWriter(clientSocketOnServer.getOutputStream());
			inStream = new ObjectInputStream(clientSocketOnServer.getInputStream());
			myClient = (Client) inStream.readObject();

			listOfClient = (ArrayList<Client>)(serialize.deSerializeObject());

			if (myClient.isExist())
			{
				//Controle si le client existe deja
				for (Client clientRegistered : listOfClient)
				{
					if(clientRegistered.getName().equalsIgnoreCase(myClient.getName()))
					{
						if(clientRegistered.getMdp().equals(myClient.getMdp()))
						{
							System.out.println("Mot de passe correct");
							validation = 1;

							//Affiche la confirmation du password dans le server
							sf.createLabel("Password Validate");
							break;
						}
						else
						{
							//Affiche l'échec du password
							sf.createLabel("False Password");
							System.out.println("Mot de passe incorrect");
							validation = 0;
							break;
						}
					}
					validation = 0;
				}
			}
			else
			{
				//Controle si le client existe deja
				for (Client clientRegistered : listOfClient)
				{
					if(clientRegistered.getName().equalsIgnoreCase(myClient.getName()))
					{
						validation = 2;
						break;
					}
				}

				if (validation != 2)
				{
					Client newClient = new Client(myClient.getName(), myClient.getMdp());
					listOfClient.add(newClient);
					serialize.serializeObject(listOfClient);
					validation = 1;
				}
			}

			outStream.writeInt(1);
			outStream.flush();
			


			//Si le client est validé
			if (validation== 1)
			{
				this.listClientsConnected.add(this);
				updateClientList();
				try {
					Object o;
					while((o = inStream.readObject())!=null) {
						if(o instanceof Message) {
							Message m = (Message)o;
							sf.createLabel(m.getClient().getName() + " : " + m.getMessage());
							for (AccepteClient accepteClient : listClientsConnected) {
								accepteClient.outStream.writeObject(m);
								accepteClient.outStream.flush();
							}
							
						}
						if(o instanceof CloseMyConnection) {
							CloseMyConnection cmc = (CloseMyConnection)o;
							if(myClient.getName().equals(cmc.getClient().getName())) {
								System.out.println("je quitte tout");
							}
						}
					}
				}
				catch(SocketException e) {
					listClientsConnected.remove(this);
					updateClientList();
					System.out.println("Client disconnected");
				}
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

	private void updateClientList() throws IOException {

		ArrayList<Client> alClient = new ArrayList<Client>();
		for (AccepteClient accepteClient : listClientsConnected) {
			alClient.add(accepteClient.myClient);
		}
		for (AccepteClient accepteClient : listClientsConnected) {
			accepteClient.outStream.writeObject(alClient);
			accepteClient.outStream.flush();
		}
		
	}
}