package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerFrame sf;
    private Socket clientSocket = null;
    private Serialize serialize = new Serialize();
    private ArrayList<AccepteClient> listClientsConnected = new ArrayList<>();

    public Server() {
        launch();
    }

    @SuppressWarnings("unchecked")
    private void launch() {
        //Initialise la frame
        sf = new ServerFrame();

        //cree les fichiers de sauvegarde utilisateurs si n'existent pas
        serialize.createFile();

        ServerSocket mySkServer;

        try {
            mySkServer = new ServerSocket(45000, 5);

            //wait for a client connection
            while (true) {
                clientSocket = mySkServer.accept();
                System.out.println("connection request received");

                Thread t = new AccepteClient(clientSocket, listClientsConnected, sf, serialize);

                //starting the thread
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}