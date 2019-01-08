package Client;

import java.io.Serializable;
import java.time.LocalDate;

public class Message implements Serializable  
{
    private final String message;
    private final LocalDate date;
    private final Client myclient;
    public Message(String message, Client myclient) 
    {
        this.message = message;
        this.myclient = myclient;
        this.date = LocalDate.now();
    }

    public LocalDate getDate() 
    {
        return date;
    }

    public String getMessage() 
    {
        return message;
    }

    public Client getClient() 
    {
        return myclient;
    }
}
