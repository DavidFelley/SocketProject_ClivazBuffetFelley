package Client;

import java.io.Serializable;

public class Client implements Serializable 
{
	/*
	 * Création de l'objet client 
	 */
	private String name;
	private String mdp;
	private String ip;
	private String [] listOfFiles;
	private boolean exist;
	
	public Client(String name, String mdp, String ip, String [] listOffiles, boolean exist) 
	{
		this.name = name;
		this.mdp = mdp;
		this.ip = ip;
		this.listOfFiles = listOffiles;
		this.exist = exist;
	}
	
	public Client(String name, String mdp) 
	{
		this.name = name;
		this.mdp = mdp;
	}

	public boolean isExist() 
	{
		return exist;
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

	public void setListOfFiles(String[] newListOfFiles) 
	{
		this.listOfFiles = newListOfFiles;
	}
	
	@Override
	public String toString() {
		return name ;
	}
}
