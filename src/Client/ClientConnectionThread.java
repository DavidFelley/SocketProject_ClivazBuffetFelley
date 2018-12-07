package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientConnectionThread {
	public static void main(String[] args) {
		
		//Ip of the server
		InetAddress serverAddress;
        String serverName = "192.168.1.143";

		try {
			serverAddress = InetAddress.getByName(serverName);
			System.out.println("Get the address of the server : "+ serverAddress);

			//try to connect to the server
			Socket mySocket = new Socket(serverAddress,45000);

			System.out.println("We got the connexion to  "+ serverAddress);
			
			//get an input stream from the socket to read data from the server
			BufferedReader buffin = new BufferedReader (new InputStreamReader (mySocket.getInputStream()));
			
			System.out.println("wait message from server...");
			String message_distant = buffin.readLine().trim();
			
			//display message received by the server
			System.out.println("\nIdentification received from server:\n" + message_distant);
			
			
			//wait a bit before exit
			Thread.sleep(3000);
						
			System.out.println("\nTerminate client program...");
			buffin.close();
			mySocket.close();


		}catch (UnknownHostException e) {

			e.printStackTrace();
		}catch (IOException e) {
			System.out.println("server connection error, dying.....");
		}catch(NullPointerException e){
			System.out.println("Connection interrupted with the server");
		}
	    catch (InterruptedException e) {
			System.out.println("interrupted exception");		
	    }
	}
}