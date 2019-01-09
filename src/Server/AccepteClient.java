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

	/**
	 * AcceptClient constructor
	 * 
	 * 
	 * @param clientSocketOnServer
	 * @param clientsConnected
	 * @param sf
	 * @param serialize
	 * @param log
	 */
	public AccepteClient(Socket clientSocketOnServer, ArrayList<AccepteClient> clientsConnected, ServerFrame sf, Serialize serialize, Logging log) 
	{
		this.clientSocketOnServer = clientSocketOnServer;
		this.sf = sf;
		this.serialize = serialize;
		this.listClientsConnected = clientsConnected;
		this.log = log;
	}

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
				/**
				 * Controle of a Client connection
				 * 
				 */
				for (Client clientRegistered : listOfClient)
				{
					if (clientRegistered.getName().equalsIgnoreCase(myClient.getName()))
					{
						log.write(myClient.getName()+" find", "info");
						if (clientRegistered.getMdp().equals(myClient.getMdp()))
						{
							log.write("Password validate, access granted", "info");
							validation = 1;
							//Affiche la confirmation du password dans le server
							showMessage("System", "Password Validate");
							break;
						} 
						else 
						{
							//Affiche l'échec du password
							log.write("Wrong password, connection refused", "info");
							showMessage("System", "Wrong password");
							validation = 0;
							break;
						}
					}
					validation = 0;
				}
			} 
			else 
			{
				/**
				 * Controle of a Client registration
				 * 
				 */
				for (Client clientRegistered : listOfClient) 
				{
					if (clientRegistered.getName().equalsIgnoreCase(myClient.getName())) 
					{
						log.write(myClient.getName()+" already exists, connection refused", "info");
						validation = 2;
						break;
					}
				}
				if (validation != 2) 
				{
					Client newClient = new Client(myClient.getName(), myClient.getMdp());
					log.write("new user "+myClient.getName()+" created, connection granted", "info");
					listOfClient.add(newClient);
					serialize.serializeObject(listOfClient);
					validation = 1;
				}
			}

			outStream.writeInt(validation);
			outStream.flush();

			/**
			 * Validation of connection
			 * 
			 */
			if (validation == 1) 
			{
				listClientsConnected.add(this);
				updateClientList();
				try 
				{
					Object o;
					while ((o = inStream.readObject()) != null) 
					{					
						/**
						 * If the Server receive a String[] (When a user add a new File)
						 * 
						 */
						if(o instanceof String [])
						{
							String[] newList = (String[]) o;		
							myClient.setListOfFiles(newList);
							log.write("New shared file from "+myClient.getName(), "info");
							updateFileClient();
						}
						
						/**
						 * If the Server receive a Message
						 * 
						 */
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
				log.write("Connection closed", "info");
				sleep(3000);
				this.stop();
			}
		} 
		catch (IOException | ClassNotFoundException | InterruptedException e)
		{
			log.write(e.getMessage().toString(), "severe");
		} 
	}

	/**
	 * Method that update the ClientList at each new connection
	 * 
	 * @throws IOException
	 */
	private void updateClientList() throws IOException 
	{
		ArrayList<Client> alClient = new ArrayList<Client>();
		for (AccepteClient accepteClient : listClientsConnected) 
		{
			alClient.add(accepteClient.myClient);
		}
		for (AccepteClient accepteClient : listClientsConnected) 
		{
			if(accepteClient != this)
			{
				accepteClient.outStream.writeObject(alClient);
				accepteClient.outStream.flush();
			}
			
		}
	}
	
	/**
	 * Method that update the File[] of a Client
	 * 
	 * @throws IOException
	 */
	private void updateFileClient() throws IOException 
	{
		ArrayList<Client> ClientAddFile = new ArrayList<Client>();
		for (AccepteClient clientConnected : listClientsConnected) 
		{			
			if(clientConnected.myClient != myClient)
				ClientAddFile.add(clientConnected.myClient);
			else
				ClientAddFile.add(myClient);
		}
		
		for (AccepteClient client : listClientsConnected) 
		{
			if(client != this)
			{
				client.outStream.reset();
				client.outStream.writeObject(ClientAddFile);
				client.outStream.flush();
			}
		}
	}

	/**
	 * Method that send a received Message to all Client connected
	 * 
	 * @param sender
	 * @param msg
	 */
	private void showMessage(String sender, String msg) 
	{
		DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm");

		//Get formatted String
		String dateFormated = formatDate.format(LocalDateTime.now());

		sf.createLabel("(" + dateFormated  + ") " + sender + " : " + msg);
	}
}