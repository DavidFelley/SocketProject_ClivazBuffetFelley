package Client;

import java.io.File;
import java.io.FileFilter;

public class FrameClient {

	public static void main(String[] args) 
	{
		//Launch the thread connection for the client
		ClientConnectionThread clientThread = new ClientConnectionThread();
		clientThread.launchThread("192.168.1.143");
		
		//Get the list of files and Ip to create the Client Object
		File [] files = getListOfFile();
		String ip = clientThread.getClientIp();
		
		Client client = new Client(ip, files);
		
		System.out.println("ip " + ip);
		
		
	}
	
	public static File[] getListOfFile()
	{
		File directory = new File("C:/SharedDocuments");
		
		if(!directory.exists())
			directory.mkdir();
		
		File [] listOfFiles = directory.listFiles();

		
		return listOfFiles;
	}

}
