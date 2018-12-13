package Client;

import java.io.File;

public class Client
{
	private String name;
	private String ip;
	private File[] listOfFiles;
	
	//Objet client 
	public Client(String name, String ip, File[] listOfFiles)
	{
		this.name = name;
		this.ip = ip;
		this.listOfFiles = listOfFiles;
	}
	
	//Temporary constructor without name
	public Client( String ip, File[] listOfFiles)
	{
		this.ip = ip;
		this.listOfFiles = listOfFiles;
	}
	
	public String getName() 
	{
		return name;
	} 

	public String getIp() 
	{
		return ip;
	}

	public File[] getListOfFiles() 
	{
		return listOfFiles;
	}

	@Override
	public String toString() 
	{
		return name;
	}

}
