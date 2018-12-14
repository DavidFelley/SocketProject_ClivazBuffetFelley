package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

public class AccepteClient implements Runnable {

	private Socket clientSocketOnServer;
	private int clientNumber;
	
	private String clientID;
	private String clientIP;
	

	private void Client(String clientID, String clientIP )
	{
		this.clientID = clientID;
		this.clientIP = clientIP;
		
	}
	//Constructor
	public AccepteClient (Socket clientSocketOnServer, int clientNo)
	{
		this.clientSocketOnServer = clientSocketOnServer;
		this.clientNumber = clientNo;

	}
	//overwrite the thread run()
	public void run() {

		try {
				System.out.println("Client Nr "+clientNumber+ " is connected");
				System.out.println("Socket is available for connection"+ clientSocketOnServer);
				
				
				//open the output data stream to write on the client
				PrintWriter pout = new PrintWriter(clientSocketOnServer.getOutputStream());
				
				//write the message on the output stream
				pout.println(clientNumber);
				pout.flush();	
				

				
				
			
				
				
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}