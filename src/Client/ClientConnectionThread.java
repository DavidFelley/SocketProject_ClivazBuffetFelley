package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientConnectionThread {

	public void launchThread(String serverName, int port)
	{
		//Ip of the server
		InetAddress serverAddress;

		try {
			serverAddress = InetAddress.getByName(serverName);
			System.out.println("Get the address of the server : "+ serverAddress);

			//try to connect to the server
			Socket clientSocket = new Socket(serverAddress,port);

			System.out.println("We got the connexion to  "+ serverAddress);

		}catch (UnknownHostException e) {

			e.printStackTrace();
		}catch (IOException e) {
			System.out.println("server connection error, dying.....");
		}catch(NullPointerException e){
			System.out.println("Connection interrupted with the server");
		}

	}
	
	public String getClientIp() 
	{
		String clientAddress = "";
		
		try 
		{
			clientAddress = InetAddress.getLocalHost().getHostAddress();
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		
		return clientAddress;
	}
}