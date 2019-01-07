package Client;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientFrame {
    //Variables of connection
    private Client myClient = null;
    private String[] listOfFiles = null;
    private boolean exist;
    private Socket clientRequestSocket = null;
    private ServerSocket myHostClient = null;
    private ObjectOutputStream outStream = null;
    private OutputStream outStreamClienttoClient = null;
    private ObjectInputStream inStream = null; // discution avec le server
    private ObjectInputStream inStreamClienttoClient = null; // discussion avec le client pour le dl
    private JFileChooser myJfileChooser = new JFileChooser();
    private ArrayList<Client> listOfClients = new ArrayList<>();
    private Socket requestClient;
    private InputStream inStreamRequestClient;
    private ObjectOutputStream outStreamRequestClient;

    private String directoryFiles = "C:\\SharedDocuments\\";

    //Variables graphiques
    private JFrame frame;
    private JTextField jtxtfLogin;
    private JTextField jtxtfServer;
    private JPasswordField jtxtfPassword;
    private JPanel pnlSharedFiles;
    private JPanel pnlListShared;
    private CardLayout myCardLayout = new CardLayout();
    private JPanel pnlMain = new JPanel(myCardLayout);
    private JPanel pnlServer;
    private JLabel lblError;
    private JComboBox jcbobxForClient;
    private JTextField txtFMsgSend;
    private JTextArea txtAreaChat;
    private DefaultListModel<String> model;
    private JList<String> JlstFile;

    /**
     * Create the application.
     */
    public ClientFrame() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 1000, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        frame.getContentPane().add(pnlMain);

        JPanel panelLogin = new JPanel();
        pnlMain.add(panelLogin, "panelLogin");
        panelLogin.setBackground(Color.DARK_GRAY);
        panelLogin.setLayout(null);

        jtxtfLogin = new JTextField();
        jtxtfLogin.setBounds(332, 78, 330, 22);
        panelLogin.add(jtxtfLogin);
        jtxtfLogin.setColumns(10);

        jtxtfServer = new JTextField();
        jtxtfServer.setBounds(332, 396, 330, 22);
        panelLogin.add(jtxtfServer);
        jtxtfServer.setColumns(10);

        jtxtfPassword = new JPasswordField();
        jtxtfPassword.setBounds(332, 237, 330, 22);
        panelLogin.add(jtxtfPassword);

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setForeground(Color.RED);
        lblLogin.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblLogin.setBounds(332, 43, 191, 34);
        panelLogin.add(lblLogin);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.RED);
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblPassword.setBounds(332, 208, 191, 16);
        panelLogin.add(lblPassword);

        JLabel lblServerIp = new JLabel("Server IP");
        lblServerIp.setForeground(Color.RED);
        lblServerIp.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblServerIp.setBounds(332, 367, 191, 16);
        panelLogin.add(lblServerIp);

        JButton btnLogin = new JButton("Login");
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(Color.RED);
        btnLogin.setBounds(266, 528, 97, 25);
        btnLogin.addActionListener(new LoginClick());
        panelLogin.add(btnLogin);

        JButton btnSignIn = new JButton("Sign in");
        btnSignIn.setBackground(Color.RED);
        btnSignIn.setForeground(Color.WHITE);
        btnSignIn.setBounds(629, 528, 97, 25);
        btnSignIn.addActionListener(new SignInClick());
        panelLogin.add(btnSignIn);

        lblError = new JLabel("", SwingConstants.CENTER);
        lblError.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblError.setForeground(Color.RED);
        lblError.setBounds(236, 458, 522, 22);
        panelLogin.add(lblError);

        pnlServer = new JPanel();
        pnlMain.add(pnlServer, "panelServer");
        pnlServer.setBackground(Color.DARK_GRAY);
        pnlServer.setLayout(null);

        JPanel panelFiles = new JPanel();
        panelFiles.setBounds(12, 13, 251, 420);
        pnlServer.add(panelFiles);
        panelFiles.setLayout(new BorderLayout(0, 0));

        jcbobxForClient = new JComboBox<String>();
        jcbobxForClient.addActionListener(new SelectionChanged());
        panelFiles.add(jcbobxForClient, BorderLayout.NORTH);

        model = new DefaultListModel<>();

        JlstFile = new JList<>(model);
        panelFiles.add(JlstFile, BorderLayout.CENTER);

        pnlSharedFiles = new JPanel();
        pnlSharedFiles.setBounds(731, 13, 251, 420);
        pnlServer.add(pnlSharedFiles);
        pnlSharedFiles.setLayout(new BorderLayout(10, 0));

        JPanel panel = new JPanel();
        pnlSharedFiles.add(panel, BorderLayout.NORTH);

        JLabel lblSharedFiles = new JLabel("My Files");
        panel.add(lblSharedFiles);

        pnlListShared = new JPanel();
        pnlSharedFiles.add(pnlListShared, BorderLayout.CENTER);

        JButton btnAddFile = new JButton("Add File");
        btnAddFile.addActionListener(new addFile());
        btnAddFile.setBounds(731, 448, 97, 25);
        pnlServer.add(btnAddFile);

        JButton btnDownload = new JButton("Download");
        btnDownload.addActionListener(new DownloadButtonClick());
        btnDownload.setBounds(12, 448, 97, 25);
        pnlServer.add(btnDownload);

        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(new sendMessage());
        btnSend.setBounds(632, 449, 89, 23);
        pnlServer.add(btnSend);

        txtFMsgSend = new JTextField();
        txtFMsgSend.addKeyListener(new KeySender());
        txtFMsgSend.setBounds(272, 448, 350, 22);
        pnlServer.add(txtFMsgSend);
        txtFMsgSend.setColumns(10);

        JScrollPane scrollChat = new JScrollPane();
        scrollChat.setBounds(273, 13, 449, 420);
        pnlServer.add(scrollChat);

        txtAreaChat = new JTextArea();
        txtAreaChat.setEditable(false); //pour ne pas editer le chat
        scrollChat.setViewportView(txtAreaChat);

        //Afin de faire des tests plsu rapidement nous mettons des donn�e en dur (mettre en commentaire par la suiste)
        jtxtfServer.setText("192.168.43.242");
        jtxtfLogin.setText("loan");
        jtxtfPassword.setText("1234");

        DefaultCaret caret = (DefaultCaret) txtAreaChat.getCaret();
        caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);

        frame.setVisible(true);
    }

    /* ajout de chaque fichier dans le dossier dans une liste*/
    private void addFileInList(String[] listOfFiles) {
        for (String listOfFile : listOfFiles) {
            JLabel lblfileName = new JLabel(listOfFile);
            pnlListShared.add(lblfileName);
        }
        pnlSharedFiles.validate();
        pnlSharedFiles.repaint();
    }

    /* Connection du client vers le server suivant les infos donn�e
     */
    private void connect() throws IOException 
    {
        String ipServer = jtxtfServer.getText();
        Socket clientSocket = new Socket(ipServer, 45000);

        String ipClient = clientSocket.getLocalAddress().getHostAddress();
        inStream = new ObjectInputStream(clientSocket.getInputStream());
        outStream = new ObjectOutputStream(clientSocket.getOutputStream());

        String login = jtxtfLogin.getText();
        String password = jtxtfPassword.getText();

        listOfFiles = getListOfFiles();

        myClient = new Client(login, password, ipClient, listOfFiles, exist);

        outStream.writeObject(myClient);
        int controle = inStream.readInt();
        controleConnection(controle);
    }

    /*
     * mise a jour de l'interface graphique du client par rapport au changement
     * nouveau message , nouveau liste de fichiers , nouveau client connect� , ...
     */
    private void listenServer() 
    {
        new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                while (true) 
                {
                    try 
                    {
                        Object o = inStream.readObject();
                        /*
                         * si l'objet mis a jour est un message
                         * alors on met a jout le texte area
                         */
                        if (o instanceof Message) 
                        {
                            Message m = (Message) o;
                            String sender;
                            //lors de l'envoie du message , si c'est nous qui envoyont le message nous voyons "Me" a la place de notre pseudo
                            if (m.getClient().getName().equals(myClient.getName())) {
                                sender = "Me";
                            } 
                            else 
                            {
                                sender = m.getClient().getName();
                            }
                            showNewMessage(sender, m.getMessage());
                        }

                        /*
                         * si l'objet mis a jour est un ArrayList
                         *liste des fichiers client (Loan)
                         */

                        if (o instanceof ArrayList) 
                        {
                            if (((ArrayList) o).size() > 0 && ((ArrayList) o).get(0) instanceof Client) 
                            {
                                listOfClients = (ArrayList<Client>) o;
                                if (jcbobxForClient.getItemCount() >= 1) 
                                {
                                    jcbobxForClient.removeAllItems();
                                }
                                for (Client thisClient : listOfClients) 
                                {
                                    jcbobxForClient.addItem(thisClient);
                                    System.out.println(thisClient.getName());
                                    System.out.println(listOfClients);
                                }
                            }
                        }
                    } 
                    catch (IOException | ClassNotFoundException e) 
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() //Thread ꤨange fichier  , partie h??(le client qui poss饥 le fichier)
        {
            //donc ici je suis le client qui poss饥 le fichier
            @Override
            public void run() {
                //crꢴion d'un socketsrv
                try {
                    myHostClient = new ServerSocket(45001, 10, InetAddress.getByName(myClient.getIp()));

                    Object oClient = null;
                    while (true) {
                        /*
                         *  Crꢴion du server pour l'hote du fichier
                         */

                        clientRequestSocket = myHostClient.accept();
                        inStreamClienttoClient = new ObjectInputStream(clientRequestSocket.getInputStream());
                        outStreamClienttoClient = clientRequestSocket.getOutputStream();

                        try {
                            // J'꤯ute si quelqu'un veut un fichier
                            oClient = inStreamClienttoClient.readObject();
                        } catch (ClassNotFoundException | IOException e) {
                            e.printStackTrace();
                        }
                        //a tester si vraim,ent besoin de 2 inputstream diff곥nt

                        if (oClient instanceof FileRequest) {
                            FileRequest frClient = (FileRequest) oClient;
                            //Cherche le fichier correspondant
                            //on part du principe que le dossier d'envoie et le m뮥 que celui de reception
                            File dlDirectory = new File(directoryFiles);
                            if (!dlDirectory.exists() || !frClient.getTarget().getIp().equals(myClient.getIp())) // + verification du fichier dans le dossier
                            {
                                return; //pas besoin de continuer
                            }
                            //rꤵp곡tion du fichier convoit顰ar le client "clientRequest"
                            File monFichier = null;
                            //prise du fichier dans le client qui correspond
                            for (File f : dlDirectory.listFiles()) {
                                if (f.getName().equals(frClient.getNameFile())) {
                                    monFichier = f;
                                    break;
                                }
                            }
                            System.out.println(monFichier.getAbsolutePath());
                            // envoie du fichier
                            Files.copy(Paths.get(monFichier.getAbsolutePath()), outStreamClienttoClient);
                            clientRequestSocket.close();
                        }
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }).start();
    }

    private void showNewMessage(String sender, String message) {


        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("HH:mm");

        //Get formatted String
        String dateFormated = formatDate.format(LocalDateTime.now());


        txtAreaChat.append("[" + dateFormated  + "] " + sender + " : " + message + "\n");
    }

    private void controleConnection(int value) 
    {
        switch (value) 
        {
            case 0:
                lblError.setText("Wrong user or password");
                frame.repaint();
                frame.revalidate();
                break;
            case 1:
                addFileInList(myClient.getListOfFiles());
                myCardLayout.show(pnlMain, "panelServer");
                listenServer();
                break;

            case 2:
                lblError.setText("User already exist");
                frame.repaint();
                frame.validate();
                break;

            default:
                lblError.setText("Unknown Error please try again");
                frame.repaint();
                frame.validate();
        }
    }

    //cr�ation de la listes des fichiers
    private String[] getListOfFiles() {
        File directory = new File(directoryFiles);

        if (!directory.exists()) {
            directory.mkdir(); //cr�ation du dossier
        }
        String[] files = new String[directory.list().length];
        File[] lst = directory.listFiles();

        for (int i = 0; i < files.length; i++) {
            files[i] = lst[i].getName();
        }
        return files;
    }

    private void saveToDirectory(String path) {
        try {
            File file = new File(path);
            Path sourceDirectory = Paths.get(path);
            Path targetDirectory = Paths.get(directoryFiles + file.getName());

            //copy source to target using Files Class
            Files.copy(sourceDirectory, targetDirectory);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class LoginClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                exist = true;
                connect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    class SignInClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                exist = false;
                connect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    class addFile implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int resultat = myJfileChooser.showOpenDialog(pnlServer);

            if (resultat == JFileChooser.CANCEL_OPTION) {
                myJfileChooser.cancelSelection();
                return;
            }

            if (resultat == JFileChooser.APPROVE_OPTION) {
                saveToDirectory(myJfileChooser.getSelectedFile().getAbsolutePath());
                pnlSharedFiles.add(pnlListShared, BorderLayout.CENTER);
            }
        }
    }

    private class sendMessage implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sendMessage();
        }


    }

	private void sendMessage() {
		try {
            outStream.writeObject(new Message(txtFMsgSend.getText(), myClient));
            outStream.flush();
            txtFMsgSend.setText("");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
	}
	
    private class SelectionChanged implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("avant remove" + Arrays.toString(listOfFiles));
            model.removeAllElements(); //enl�ve l'affichage des fichiers dans la liste

            if (jcbobxForClient.getItemCount() > 0) { //si il y a quelque chose dans la liste
                System.out.println("hello ma liste est plus grande que 0");
                //remplissage du panel par rapport aux clients connecter.
                for (String myFile : listOfClients.get(jcbobxForClient.getSelectedIndex()).getListOfFiles()) {
                    System.out.println("nom du fichier" + myFile);
                    model.addElement(myFile);
                }
                System.out.println(Arrays.toString(listOfClients.get(jcbobxForClient.getSelectedIndex()).getListOfFiles()));
            }
        }
    }

    /*
     * ici on crꥠle c??"Client" du dl
     */
    private class DownloadButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String myFile = model.get(JlstFile.getSelectedIndex());
            System.out.println(myFile);
            Client target = listOfClients.get(jcbobxForClient.getSelectedIndex());
            FileRequest fr = new FileRequest(myFile, myClient, target);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //se connecter au server
                    try {
                        requestClient = new Socket(fr.getTarget().getIp(), 45001);
                        System.out.println(fr.getTarget().getIp());
                        inStreamRequestClient = requestClient.getInputStream();
                        System.out.println(3);
                        outStreamRequestClient = new ObjectOutputStream(requestClient.getOutputStream());
                        System.out.println(4);
                        outStreamRequestClient.writeObject(fr);
                        Files.copy(inStreamRequestClient, Paths.get(directoryFiles + fr.getNameFile()));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
    
    private class KeySender extends KeyAdapter {
    	public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
            	sendMessage();
            }
    	}
	}
}
