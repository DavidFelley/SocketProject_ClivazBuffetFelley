package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

public class AcceptClient implements Runnable {

	private Socket clientSocketOnServer;
	private int clientNumber;

	//Constructor
	public AcceptClient (Socket clientSocketOnServer, int clientNo)
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
				
				clientSocketOnServer.close();	
				Thread.sleep(3000);
				
				System.out.println("end of connection to the client " + clientNumber);
				
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}