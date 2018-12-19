package Client;

import java.io.Serializable;

public class Client implements Serializable 
{
	private String name;
	private String mdp;
	private String ip;
	private String [] listOfFiles;
	
	public Client(String name, String mdp, String ip, String [] listOffiles) 
	{
		this.name = name;
		this.mdp = mdp;
		this.ip = ip;
		this.listOfFiles = listOffiles;
	}

	public String getName() 
	{
		return name;
	}
	
	public String getMdp() 
	{
		return mdp;
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
