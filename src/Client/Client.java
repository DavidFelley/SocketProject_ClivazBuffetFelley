package Client;

import java.io.Serializable;

public class Client implements Serializable 
{
	private String name;
	private String mdp;
	private String ip;
	private String [] listOfFiles;
	private boolean exist;
	
	/**
	 * Full client constructor
	 * 
	 * @param name
	 * @param mdp
	 * @param ip
	 * @param listOffiles
	 * @param exist
	 */
	public Client(String name, String mdp, String ip, String [] listOffiles, boolean exist) 
	{
		this.name = name;
		this.mdp = mdp;
		this.ip = ip;
		this.listOfFiles = listOffiles;
		this.exist = exist;
	}
	
	/**
	 * Client constructor for connection
	 * 
	 * @param name
	 * @param mdp
	 */	
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
