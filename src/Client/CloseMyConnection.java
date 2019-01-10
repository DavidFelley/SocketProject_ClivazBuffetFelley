/**
 * Obsolet Class.
 */

package Client;

public class CloseMyConnection 
{
	private final Client myclient;	

	public CloseMyConnection(Client myclient) 
	{
		this.myclient = myclient;
	}

	public Client getClient() 
	{
		return myclient;
	}
}
