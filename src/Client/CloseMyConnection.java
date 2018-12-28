package Client;

public class CloseMyConnection {
	private final Client client;	
	
	public CloseMyConnection(Client client) {
		this.client = client;
	}
	
	public Client getClient() {
		return client;
	}
}
