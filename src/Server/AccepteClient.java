package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

public class AccepteClient implements Runnable {

	private Socket clientSocketOnServer;
	private int clientNumber;
	Frame frame ;
	
	private String clientID;
	private String clientIP;
	

	
	//Constructor
	public AccepteClient (Socket clientSocketOnServer, int clientNo, Frame frame)
	{
		this.clientSocketOnServer = clientSocketOnServer;
		this.clientNumber = clientNo;
		this.frame = frame;

	}
	//overwrite the thread run()
	public void run() {

		try {
			frame.createLabel("Client Nr "+clientNumber+ " is connected");
			frame.createLabel("Socket is available for connection"+ clientSocketOnServer);
				
				
				
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