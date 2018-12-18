package Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Register 
{

	public static void register(Socket clientSocket, int num)
	{
		
		
		File f = new File("RegisterClient");
	
		if(!f.exists())
			System.out.println("le dossier n existe pas");
			f.mkdirs();
		
			
		try {			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("RegisterClient/Client")));
			// normalement si le fichier n'existe pas, il est crée à la racine du projet
			
			writer.close();
			}
			catch (IOException e)
			{
			e.printStackTrace();
			}
		System.out.println("Client registered !!!");
		
		
		
	}
	
	

	
}
