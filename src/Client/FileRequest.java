package Client;

import java.io.Serializable;

public class FileRequest implements Serializable
{
    private final String nameFile;
    private final Client sender;
    private final Client target;

    public FileRequest(String nameFile, Client sender, Client target) 
    {
        this.nameFile = nameFile;
        this.sender = sender;
        this.target = target; //tout l'objet client (donc accès ip adress , name etc)
    }

    public String getNameFile() 
    {
        return nameFile;
    }

    public Client getSender() 
    {
        return sender;
    }

    public Client getTarget() 
    {
        return target;
    }
}