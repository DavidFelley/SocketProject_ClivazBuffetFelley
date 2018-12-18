package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class AccepteClient implements Runnable {

	private Socket clientSocketOnServer;
	private int clientNumber;
	Frame frame ;

	private String clientID;
	private String clientIP;
	
	BufferedReader buffin = null;
	PrintWriter pout = null;

	//Constructor
	public AccepteClient (Socket clientSocketOnServer, int clientNo, Frame frame)
	{
		this.clientSocketOnServer = clientSocketOnServer;
		this.clientNumber = clientNo;
		this.frame = frame;

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
				//create an input stream to read data from the server
				buffin = new BufferedReader (new InputStreamReader (clientSocketOnServer.getInputStream()));

				//open the output data stream to write on the client
				pout = new PrintWriter(clientSocketOnServer.getOutputStream());


				frame.createLabel(buffin.readLine());
				
			}

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}

}