package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Serialize 
{
	private String path;

	public Serialize(String path) 
	{
		super();
		this.path = path;
	}

	public void serializeObject(Object o) 
	{

		try 
		{
			FileOutputStream fichier = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(o);
			oos.flush();
			oos.close();
		}
		catch (java.io.IOException e) 
		{
			e.printStackTrace();
		}
	}

	public Object deSerializeObject() 
	{ 
		Object cs = null;
		
		try 
		{
			FileInputStream fichier = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fichier);
			cs=ois.readObject();
		}
		catch (Exception e) 
		{
			cs = new Object();
		}

		return cs;
	}

	public void createFile()
	{
		File f = new File("Client//client.zer"); 

		// tester si le parent (qui est un r�pertoire existe) 

		File p = new File("Client"); 

		try
		{
			// cr�e le rep 
			if (!p.exists()) 
				p.mkdirs(); 

			if (!f.exists())
				f.createNewFile();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
