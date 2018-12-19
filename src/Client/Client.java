package Client;

import java.io.Serializable;

public class Client implements Serializable 
{
	private String name;
	private String ip;
	private String [] listOfFiles;
	
	public Client(String name, String ip, String [] listOffiles) 
	{
		this.name = name;
		this.ip = ip;
		this.listOfFiles = listOffiles;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public String[] getListOfFiles() 
	{
		return listOfFiles;
	}

	public void setListOfFiles(String[] listOfFiles) 
	{
		this.listOfFiles = listOfFiles;
	}
	
	@Override
	public String toString() {
		return name ;
	}
}
