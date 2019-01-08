package Server;

import Client.Client;
import Client.CloseMyConnection;
import Client.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AccepteClient extends Thread 
{
	private Serialize serialize;
	private ServerFrame sf = null;
	private Socket clientSocketOnServer = null;
	private ArrayList<AccepteClient> listClientsConnected = null;
	private Client myClient = null;
	private BufferedReader buffin = null;
	private Logging log;
	private ObjectOutputStream outStream = null;
	private int validation = 0;

	//Constructor
	public AccepteClient(Socket clientSocketOnServer, ArrayList<AccepteClient> clientsConnected, ServerFrame sf, Serialize serialize, Logging log) 
	{
		this.clientSocketOnServer = clientSocketOnServer;
		this.sf = sf;
		this.serialize = serialize;
		this.listClientsConnected = clientsConnected;
		this.log = log;
	}

	//overwrite the thread run()
	@SuppressWarnings({"unchecked", "deprecation"})
	public void run() 
	{
		try 
		{
			outStream = new ObjectOutputStream(clientSocketOnServer.getOutputStream());

			ObjectInputStream inStream = new ObjectInputStream(clientSocketOnServer.getInputStream());

			myClient = (Client) inStream.readObject();

			ArrayList<Client> listOfClient = (ArrayList<Client>) (serialize.deSerializeObject());

			if (myClient.isExist()) 
			{
				//Controle si le client existe deja
				for (Client clientRegistered : listOfClient)
				{
					if (clientRegistered.getName().equalsIgnoreCase(myClient.getName()))
					{
						log.write(myClient.getName()+" reconnu", "info");
						if (clientRegistered.getMdp().equals(myClient.getMdp()))
						{
							log.write("Mot de passe validé, connection validée", "info");
							System.out.println("Mot de passe correct");
							validation = 1;
							//Affiche la confirmation du password dans le server
							showMessage("System", "Password Validate");
							break;
						} 
						else 
						{
							//Affiche l'échec du password
							log.write("Mot de passe incorrecte, connection refusée", "info");
							showMessage("System", "Wrong password");
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
					if (clientRegistered.getName().equalsIgnoreCase(myClient.getName())) 
					{
						log.write(myClient.getName()+" existe déjà, connection refusée", "info");
						validation = 2;
						break;
					}
				}
				if (validation != 2) 
				{
					Client newClient = new Client(myClient.getName(), myClient.getMdp());
					log.write(myClient.getName()+" créé, connection validée", "info");
					listOfClient.add(newClient);
					serialize.serializeObject(listOfClient);
					validation = 1;
				}
			}

			outStream.writeInt(validation);
			outStream.flush();

			//Si le client est validé
			if (validation == 1) 
			{
				this.listClientsConnected.add(this);
				updateClientList();
				try 
				{
					Object o;
					while ((o = inStream.readObject()) != null) 
					{					
						if(o instanceof String [])
						{
							String[] newList = (String[]) o;
							myClient.setListOfFiles(newList);
							updateFileClient();
						}
						
						//si un client nous envoie un message nous l'affichons
						if (o instanceof Message) 
						{
							Message m = (Message) o;
							//sf.createLabel(m.getClient().getName() + " : " + m.getMessage());
							showMessage(m.getClient().getName(), m.getMessage());
							for (AccepteClient accepteClient : listClientsConnected) 
							{
								accepteClient.outStream.writeObject(m); //écrit dans gui du srv
								accepteClient.outStream.flush();
							}

						}
						if (o instanceof CloseMyConnection) 
						{
							CloseMyConnection cmc = (CloseMyConnection) o;
							if (myClient.getName().equals(cmc.getClient().getName())) 
							{
								System.out.println("je quitte tout");
							}
						}
					}
				} 
				catch (SocketException e) 
				{			
					log.write(e.getMessage().toString(), "warning");
					listClientsConnected.remove(this);
					updateClientList();

				}
			} 
			else 
			{
				clientSocketOnServer.close();
				log.write("Connection fermée", "info");
				sleep(3000);
				this.stop();
			}
		} 
		catch (IOException | ClassNotFoundException | InterruptedException e)
		{
			log.write(e.getMessage().toString(), "severe");
		} 
	}

	private void updateClientList() throws IOException 
	{
		ArrayList<Client> alClient = new ArrayList<Client>();
		for (AccepteClient accepteClient : listClientsConnected) 
		{
			alClient.add(accepteClient.myClient);
		}
		for (AccepteClient accepteClient : listClientsConnected) 
		{
			accepteClient.outStream.writeObject(alClient);
			accepteClient.outStream.flush();
		}
	}
	
	private void updateFileClient() throws IOException 
	{
		ArrayList<Client> alClient = new ArrayList<Client>();
		for (AccepteClient accepteClient : listClientsConnected) 
		{
			if(accepteClient.myClient != myClient)
				alClient.add(accepteClient.myClient);
			else
				alClient.add(myClient);
		}
		for (AccepteClient accepteClient : listClientsConnected) 
		{
			accepteClient.outStream.writeObject(alClient);
			accepteClient.outStream.flush();
		}
	}

	private void showMessage(String sender, String msg) 
	{
		DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm");

		//Get formatted String
		String dateFormated = formatDate.format(LocalDateTime.now());

		sf.createLabel("(" + dateFormated  + ") " + sender + " : " + msg);
	}
}