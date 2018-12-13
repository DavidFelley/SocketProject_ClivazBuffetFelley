package Server;

import java.io.File;
import java.io.IOException;

import Client.Client;

public class Files 
{

	public File file = null;
	
	
	//Methode that create directory
	public void mkdir(String path)
	{
		
		File a = new File(path);
		
		if(!a.exists())
			a.mkdirs();
	}
	
	//Methode that create files
	public void touch(String path)
	{
		File a = new File(path);
		
		if(!a.exists())
		{
			try{
				
				a.createNewFile();
			}catch(IOException e)
			{
				//Le log apparaitra ici
			}
		}
	}
	
	
	
}
