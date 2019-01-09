package Server;

import Client.Client;

import java.io.*;
import java.util.ArrayList;


public class Serialize 
{
    private String path = "Client//client.zer";
    private Client admin = new Client("Admin", "1234");
    private ArrayList<Client> list = new ArrayList<>();

    public Serialize() 
    {
        super();
    }

    /**
     * serializeObject constructor
     * 
     * @param o
     */
    public void serializeObject(Object o) 
    {
        try 
        {
            FileOutputStream fichier = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fichier);
            oos.writeObject(o);
            oos.flush();
            oos.close();
        } catch (java.io.IOException e) 
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
            cs = ois.readObject();
        } 
        catch (Exception e) 
        {
            cs = new Object();
        }

        return cs;
    }

    /**
     * Method of creation of the list of Client registered with default creation of the admin Client
     * 
     */
    public void createFile() 
    {
        File f = new File("Client\\client.zer");

        try 
        {
            if (!f.getParentFile().exists())
                f.getParentFile().mkdir();

            if (!f.exists()) 
            {
                f.createNewFile();
                list.add(admin);
                serializeObject(list);
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
