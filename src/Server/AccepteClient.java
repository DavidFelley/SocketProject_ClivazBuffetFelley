package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;

import Client.Client;

public class AccepteClient implements Runnable {

	public static ArrayList<Client> list  ;

	private Socket clientSocketOnServer;
	private int clientNumber;
	ServerFrame frame ;
	private Client client;

	private String clientID;
	private String clientIP;
	

	private ObjectInputStream in = null;

	BufferedReader buffin = null;
	PrintWriter pout = null;

	Serialize serialize = new Serialize("Client//client.zer");
	
	//Constructor
	public AccepteClient (Socket clientSocketOnServer, int clientNo, ServerFrame frame,ArrayList<Client> list)
	{
		this.clientSocketOnServer = clientSocketOnServer;
		this.clientNumber = clientNo;
		this.frame = frame;
		this.list=list;
	}
	//overwrite the thread run()
	public void run() 
	{

		try {
			frame.createLabel("Client Nr "+clientNumber+ " is connected");
			frame.createLabel("Socket is available for connection"+ clientSocketOnServer);

			//open the output data stream to write on the client
			PrintWriter pout = new PrintWriter(clientSocketOnServer.getOutputStream());

			//write the message on the output stream
			pout.println(clientNumber);
			pout.flush();	


			while(true)
			{

				in = new ObjectInputStream(clientSocketOnServer.getInputStream());

				try {

					client = (Client) in.readObject();

					list.add(client);

					
					 serialize.serializeObject(list);

					for (int i = 0; i < list.size(); i++)
					{
						System.out.println(client.getName()+" "+ client.getMdp());
					}


				} catch (ClassNotFoundException e) {

					System.out.println("Probleme concernant l'object");
					e.printStackTrace();
				}

			}


		} 
		catch (IOException e) 
		{

			e.printStackTrace();
		} 


	}



}