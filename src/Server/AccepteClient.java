package Server;

import Client.Client;
import Client.CloseMyConnection;
import Client.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AccepteClient extends Thread {
    private Serialize serialize;
    private ServerFrame sf = null;
    private Socket clientSocketOnServer = null;
    private ArrayList<AccepteClient> listClientsConnected = null;
    private Client myClient = null;
    private BufferedReader buffin = null;

    private ObjectOutputStream outStream = null;
    private int validation = -1;

    //Constructor
    public AccepteClient(Socket clientSocketOnServer, ArrayList<AccepteClient> clientsConnected, ServerFrame sf, Serialize serialize) {
        this.clientSocketOnServer = clientSocketOnServer;
        this.sf = sf;
        this.serialize = serialize;
        this.listClientsConnected = clientsConnected;
    }

    //overwrite the thread run()
    @SuppressWarnings({"unchecked", "deprecation"})
    public void run() 
    {
        try 
        {
            outStream = new ObjectOutputStream(clientSocketOnServer.getOutputStream());
            
            ObjectInputStream inStream = new ObjectInputStream(clientSocketOnServer.getInputStream());
            
            myClient = (Client) inStream.readObject();

            ArrayList<Client> listOfClient = (ArrayList<Client>) (serialize.deSerializeObject());

            if (myClient.isExist()) 
            {
                //Controle si le client existe deja
                for (Client clientRegistered : listOfClient)
                {
                    if (clientRegistered.getName().equalsIgnoreCase(myClient.getName()))
                    {
                        if (clientRegistered.getMdp().equals(myClient.getMdp()))
                        {
                            System.out.println("Mot de passe correct");
                            validation = 1;

                            //Affiche la confirmation du password dans le server
							showMessage("System", "Password Validate");
                            break;
                        } 
                        else 
                        {
                            //Affiche l'�chec du password
							showMessage("System", "Wrong password");
                            System.out.println("Mot de passe incorrect");
                            validation = 0;
                            break;
                        }
                    }
                    validation = 0;
                }
            } 
            else 
            {
                //Controle si le client existe deja
                for (Client clientRegistered : listOfClient) 
                {
                    if (clientRegistered.getName().equalsIgnoreCase(myClient.getName())) 
                    {
                        validation = 2;
                        break;
                    }
                }
                if (validation != 2) 
                {
                    Client newClient = new Client(myClient.getName(), myClient.getMdp());
                    listOfClient.add(newClient);
                    serialize.serializeObject(listOfClient);
                    validation = 1;
                }
            }

            outStream.writeInt(validation);
            outStream.flush();


            //Si le client est valid�
            if (validation == 1) 
            {
                this.listClientsConnected.add(this);
                updateClientList();
                try 
                {
                    Object o;
                    while ((o = inStream.readObject()) != null) 
                    {
                        //si un client nous envoie un message nous l'affichons
                        if (o instanceof Message) 
                        {
                            Message m = (Message) o;
							//sf.createLabel(m.getClient().getName() + " : " + m.getMessage());
							showMessage(m.getClient().getName(), m.getMessage());
                            for (AccepteClient accepteClient : listClientsConnected) 
                            {
                                accepteClient.outStream.writeObject(m); //�crit dans gui du srv
                                accepteClient.outStream.flush();
                            }

                        }
                        if (o instanceof CloseMyConnection) 
                        {
                            CloseMyConnection cmc = (CloseMyConnection) o;
                            if (myClient.getName().equals(cmc.getClient().getName())) 
                            {
                                System.out.println("je quitte tout");
                            }
                        }
                    }
                } 
                catch (SocketException e) 
                {
                    listClientsConnected.remove(this);
                    updateClientList();
                    System.out.println("Client disconnected");
                }
            } 
            else 
            {
                clientSocketOnServer.close();
                sleep(3000);
                this.stop();
            }
        } 
        catch (IOException | ClassNotFoundException | InterruptedException e)
        {
            e.printStackTrace();
        } // TODO Auto-generated catch block
        // TODO Auto-generated catch block

    }

    private void updateClientList() throws IOException 
    {

        ArrayList<Client> alClient = new ArrayList<Client>();
        for (AccepteClient accepteClient : listClientsConnected) 
        {
            alClient.add(accepteClient.myClient);
        }
        for (AccepteClient accepteClient : listClientsConnected) 
        {
            accepteClient.outStream.writeObject(alClient);
            accepteClient.outStream.flush();
        }

    }
	
		private void showMessage(String sender, String msg) 
		{

            DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm");

            //Get formatted String
            String dateFormated = formatDate.format(LocalDateTime.now());


            sf.createLabel("(" + dateFormated  + ") " + sender + " : " + msg);
    }
}