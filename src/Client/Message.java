package Client;

import java.io.Serializable;
import java.time.LocalDate;

public class Message implements Serializable  {
    private final String message;
    private final LocalDate date;
    private final Client client;
    public Message(String message, Client client) {
        this.message = message;
        this.client = client;
        this.date = LocalDate.now();
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public Client getClient() {
        return client;
    }
}
