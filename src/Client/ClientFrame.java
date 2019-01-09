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
import javax.swing.border.BevelBorder;

public class ClientFrame 
{
	private Client myClient = null;
    private String[] listOfFiles = null;
    private boolean exist;
    private Socket clientRequestSocket = null;
    private ServerSocket myHostClient = null;
    private ObjectOutputStream outStream = null;
    private OutputStream outStreamClienttoClient = null;
    private ObjectInputStream inStream = null;
    private ObjectInputStream inStreamClienttoClient = null;
    private JFileChooser myJfileChooser = new JFileChooser();
    private ArrayList<Client> listOfClients = new ArrayList<>();
    private Socket requestClient;
    private InputStream inStreamRequestClient;
    private ObjectOutputStream outStreamRequestClient;
    private String[] newListOfFile = null;
    private String directoryFiles = "C:\\SharedDocuments\\";

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
    private JLabel lblErrorServer;

    public ClientFrame() 
    {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() 
    {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 1000, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        frame.getContentPane().add(pnlMain);

        JPanel panelLogin = new JPanel();
        pnlMain.add(panelLogin, "panelLogin");
        panelLogin.setBackground(Color.LIGHT_GRAY);
        panelLogin.setLayout(null);

        jtxtfLogin = new JTextField();
        jtxtfLogin.setSelectedTextColor(Color.WHITE);
        jtxtfLogin.setSelectionColor(Color.BLUE);
        jtxtfLogin.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        jtxtfLogin.setBounds(332, 78, 330, 22);
        panelLogin.add(jtxtfLogin);
        jtxtfLogin.setColumns(10);

        jtxtfServer = new JTextField();
        jtxtfServer.setSelectedTextColor(Color.WHITE);
        jtxtfServer.setSelectionColor(Color.BLUE);
        jtxtfServer.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        jtxtfServer.setBounds(332, 396, 330, 22);
        panelLogin.add(jtxtfServer);
        jtxtfServer.setColumns(10);

        jtxtfPassword = new JPasswordField();
        jtxtfPassword.setSelectedTextColor(Color.WHITE);
        jtxtfPassword.setSelectionColor(Color.BLUE);
        jtxtfPassword.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        jtxtfPassword.setBounds(332, 237, 330, 22);
        panelLogin.add(jtxtfPassword);

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setBackground(Color.GRAY);
        lblLogin.setForeground(Color.BLACK);
        lblLogin.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 20));
        lblLogin.setBounds(332, 43, 191, 34);
        panelLogin.add(lblLogin);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 20));
        lblPassword.setBounds(332, 208, 191, 16);
        panelLogin.add(lblPassword);

        JLabel lblServerIp = new JLabel("Server IP");
        lblServerIp.setForeground(Color.BLACK);
        lblServerIp.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 20));
        lblServerIp.setBounds(332, 367, 191, 16);
        panelLogin.add(lblServerIp);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK, new Color(255, 255, 255), new Color(0, 0, 0)));
        btnLogin.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 15));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setBackground(Color.LIGHT_GRAY);
        btnLogin.setBounds(266, 528, 97, 25);
        btnLogin.addActionListener(new LoginClick());
        panelLogin.add(btnLogin);

        JButton btnSignIn = new JButton("Sign in");
        btnSignIn.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK, Color.WHITE, Color.BLACK));
        btnSignIn.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        btnSignIn.setBackground(Color.LIGHT_GRAY);
        btnSignIn.setForeground(Color.BLACK);
        btnSignIn.setBounds(629, 528, 97, 25);
        btnSignIn.addActionListener(new SignInClick());
        panelLogin.add(btnSignIn);

        lblError = new JLabel("", SwingConstants.CENTER);
        lblError.setFont(new Font("Arial", Font.PLAIN, 13));
        lblError.setForeground(Color.BLACK);
        lblError.setBounds(236, 458, 522, 22);
        panelLogin.add(lblError);

        pnlServer = new JPanel();
        pnlMain.add(pnlServer, "panelServer");
        pnlServer.setBackground(Color.LIGHT_GRAY);
        pnlServer.setLayout(null);

        JPanel panelFiles = new JPanel();
        panelFiles.setBounds(12, 13, 251, 420);
        pnlServer.add(panelFiles);
        panelFiles.setLayout(new BorderLayout(0, 0));

        jcbobxForClient = new JComboBox<String>();
        jcbobxForClient.setBackground(Color.WHITE);
        jcbobxForClient.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 13));
        jcbobxForClient.addActionListener(new SelectionChanged());
        panelFiles.add(jcbobxForClient, BorderLayout.NORTH);

        model = new DefaultListModel<>();

        JlstFile = new JList<>(model);
        JlstFile.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        JlstFile.setSelectionBackground(Color.BLUE);
        JlstFile.setSelectionForeground(Color.WHITE);
        panelFiles.add(JlstFile, BorderLayout.CENTER);

        pnlSharedFiles = new JPanel();
        pnlSharedFiles.setBounds(731, 13, 251, 420);
        pnlServer.add(pnlSharedFiles);
        pnlSharedFiles.setLayout(new BorderLayout(10, 0));

        JPanel panel = new JPanel();
        panel.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 13));
        panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel.setForeground(new Color(0, 0, 0));
        panel.setBackground(Color.WHITE);
        pnlSharedFiles.add(panel, BorderLayout.NORTH);

        JLabel lblSharedFiles = new JLabel("My shared files");
        lblSharedFiles.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 13));
        panel.add(lblSharedFiles);

        pnlListShared = new JPanel();
        pnlListShared.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        pnlListShared.setBackground(Color.WHITE);
        pnlSharedFiles.add(pnlListShared, BorderLayout.CENTER);

        JButton btnAddFile = new JButton("Add File");
        btnAddFile.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK, Color.WHITE, Color.BLACK));
        btnAddFile.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        btnAddFile.setBackground(Color.LIGHT_GRAY);
        btnAddFile.setForeground(Color.BLACK);
        btnAddFile.addActionListener(new addFile());
        btnAddFile.setBounds(731, 448, 97, 25);
        pnlServer.add(btnAddFile);

        JButton btnDownload = new JButton("Download");
        btnDownload.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK, Color.WHITE, Color.BLACK));
        btnDownload.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        btnDownload.setBackground(Color.LIGHT_GRAY);
        btnDownload.setForeground(Color.BLACK);
        btnDownload.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        btnDownload.addActionListener(new DownloadButtonClick());
        btnDownload.setBounds(12, 448, 97, 25);
        pnlServer.add(btnDownload);

        JButton btnSend = new JButton("Send");
        btnSend.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK, Color.WHITE, Color.BLACK));
        btnSend.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        btnSend.setBackground(Color.LIGHT_GRAY);
        btnSend.setForeground(Color.BLACK);
        btnSend.addActionListener(new sendMessage());
        btnSend.setBounds(634, 449, 89, 23);
        pnlServer.add(btnSend);

        txtFMsgSend = new JTextField();
        txtFMsgSend.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        txtFMsgSend.setSelectedTextColor(Color.WHITE);
        txtFMsgSend.setSelectionColor(Color.BLUE);
        txtFMsgSend.addKeyListener(new KeySender());
        txtFMsgSend.setBounds(272, 448, 350, 22);
        pnlServer.add(txtFMsgSend);
        txtFMsgSend.setColumns(10);

        JScrollPane scrollChat = new JScrollPane();
        scrollChat.setBounds(273, 13, 449, 420);
        pnlServer.add(scrollChat);

        txtAreaChat = new JTextArea();
        txtAreaChat.setSelectedTextColor(Color.WHITE);
        txtAreaChat.setSelectionColor(Color.BLUE);
        txtAreaChat.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        txtAreaChat.setEditable(false); //pour ne pas editer le chat
        scrollChat.setViewportView(txtAreaChat);
        
        lblErrorServer = new JLabel("", SwingConstants.CENTER);
        lblErrorServer.setForeground(Color.BLACK);
        lblErrorServer.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        lblErrorServer.setBounds(236, 523, 522, 22);
        pnlServer.add(lblErrorServer);

        //Afin de faire des tests plsu rapidement nous mettons des donn�e en dur (mettre en commentaire par la suiste)
        jtxtfServer.setText("");
        jtxtfLogin.setText("");
        jtxtfPassword.setText("");

        DefaultCaret caret = (DefaultCaret) txtAreaChat.getCaret();
        caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);

        frame.setVisible(true);
    }


    /***
     * Method of initialization of the list of shared files from the Client
     * 
     * @param listOfFiles
     */
    private void initSharedList(String[] listOfFiles) 
    {
        for (String listOfFile : listOfFiles) 
        {
            createLabel(listOfFile);
        }
        
    }
    
    /**
     * Method of creation all labels in the list of shared files
     * 
     * @param nameLbl
     */
    private void createLabel(String nameLbl)
    {
    	JLabel lblfileName = new JLabel(nameLbl);
        pnlListShared.add(lblfileName);       
        pnlSharedFiles.validate();
        pnlSharedFiles.repaint();
    }

    /**
     * Method that launch the connection to the server
     */
    private void connect()
    {
        String ipServer = jtxtfServer.getText();
        Socket clientSocket;
        String ipClient = "";
        
		try 
		{
			clientSocket = new Socket(ipServer, 45000);
		

        ipClient = clientSocket.getLocalAddress().getHostAddress();
        inStream = new ObjectInputStream(clientSocket.getInputStream());
        outStream = new ObjectOutputStream(clientSocket.getOutputStream());
        
		} 
		catch (IOException e) 
		{
			lblError.setText("Unable to reach the server!");
            frame.repaint();
            frame.validate();
		}
        String login = jtxtfLogin.getText();
        String password = jtxtfPassword.getText();

        listOfFiles = getListOfFiles();

        myClient = new Client(login, password, ipClient, listOfFiles, exist);

        try 
        {
			outStream.writeObject(myClient);
			int controle = inStream.readInt();
			controleConnection(controle);
		} 
        catch (IOException e) 
        {
			//e.printStackTrace();
		}
    }

    /**
     * Method that listen continuously to the server and change the frame
     * 
     * Listen to Client connection
     * Listen to new Message
     * Listen to new File shared
     * 
     */
    private void listenServer() 
    {
        new Thread(new Runnable() 
        {
            @SuppressWarnings("unchecked")
			@Override
            public void run() 
            {
                while(true) 
                {
                    try 
                    {
                        Object o = inStream.readObject();
                        
                        
                        /**
                         * If the object received is a message
                         * 
                         */
                        if (o instanceof Message) 
                        {
                            Message m = (Message) o;
                            String sender;
                            /**
                             * If we are the sender of the Message we show "Me"
                             * 
                             */
                            if (m.getClient().getName().equals(myClient.getName())) 
                            {
                                sender = "Me";
                            } 
                            else 
                            {
                                sender = m.getClient().getName();
                            }
                            showNewMessage(sender, m.getMessage());
                        }

                      
                        /**
                         * If the object received is an ArrayList
                         * 
                         */
                        if (o instanceof ArrayList) 
                        {
                            if (((ArrayList) o).size() > 0) 
                            {
                            	listOfClients = (ArrayList<Client>) o;

                                if (jcbobxForClient.getItemCount() >= 1) 
                                {
                                    jcbobxForClient.removeAllItems();
                                }
                                for (Client thisClient : listOfClients) 
                                {
                                    jcbobxForClient.addItem(thisClient);
                                }
                            }
                        }
                    } 
                    catch (IOException | ClassNotFoundException e) 
                    {
                        lblError.setText("Connection to server lost!");
                        myCardLayout.show(pnlMain, "panelLogin");
                        outStream = null;
                        inStream = null;
                    }
                }
            }
        }).start();

        /**
         * Thread listening to Client trying to reach a File
         * 
         */
        new Thread(new Runnable() 
        {         
            @Override
            public void run() 
            {
                try 
                {
                    myHostClient = new ServerSocket(45001, 10, InetAddress.getByName(myClient.getIp()));

                    Object oClient = null;
                    while (true) 
                    {
                       /**
                        * Creation of the socket between the two Client
                        * 
                        */
                        clientRequestSocket = myHostClient.accept();
                        inStreamClienttoClient = new ObjectInputStream(clientRequestSocket.getInputStream());
                        outStreamClienttoClient = clientRequestSocket.getOutputStream();
                        try 
                        {
                            oClient = inStreamClienttoClient.readObject();
                        } 
                        catch (ClassNotFoundException | IOException e) 
                        {
                            e.printStackTrace();
                        }

                        /**
                         * If the object received is a FileRequest
                         * 
                         */
                        if (oClient instanceof FileRequest) 
                        {
                            FileRequest frClient = (FileRequest) oClient;
                            File dlDirectory = new File(directoryFiles);
                            if (!dlDirectory.exists() || !frClient.getTarget().getIp().equals(myClient.getIp()))
                            {
                                return; 
                            }                         
                            File monFichier = null;
                            for (File f : dlDirectory.listFiles()) 
                            {
                                if (f.getName().equals(frClient.getNameFile())) 
                                {
                                    monFichier = f;
                                    break;
                                }
                            }                           
                            Files.copy(Paths.get(monFichier.getAbsolutePath()), outStreamClienttoClient);
                            clientRequestSocket.close();
                        }
                    }
                } 
                catch (IOException e1) 
                {
                    e1.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Method showing the Message received with Time
     * 
     * @param sender
     * @param message
     */
    private void showNewMessage(String sender, String message) 
    {
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("HH:mm");
        String dateFormated = formatDate.format(LocalDateTime.now());

        txtAreaChat.append("[" + dateFormated  + "] " + sender + " : " + message + "\n");
    }

    /**
     * Method listening to server answer after the connection request
     * 
     * @param value
     */
    
    private void controleConnection(int value) 
    {
        switch (value) 
        {
            case 0:
                lblError.setText("User or Password incorrect!");
                frame.repaint();
                frame.revalidate();
                break;
            case 1:
                initSharedList(myClient.getListOfFiles());
                myCardLayout.show(pnlMain, "panelServer");
                listenServer();
                break;

            case 2:
                lblError.setText("User already exists!");
                frame.repaint();
                frame.validate();
                break;

            default:
                lblError.setText("Unknown error please try again!");
                frame.repaint();
                frame.validate();
        }
    }

    /**
     * Method of creation of the Client.ListOfFiles
     * 
     * @return files
     */
    private String[] getListOfFiles() {
        File directory = new File(directoryFiles);

        if (!directory.exists()) 
        {
            directory.mkdir(); //cr�ation du dossier
        }
        
        String[] files = new String[directory.list().length];
        File[] lst = directory.listFiles();

        for (int i = 0; i < files.length; i++) 
        {
            files[i] = lst[i].getName();
        }
        
        return files;
    }

    /**
     * Method of copy of a File in the SharedDocument directory
     * 
     * @param path
     */
    private void saveToDirectory(String path) 
    {
        try 
        {
            File file = new File(path);
            Path sourceDirectory = Paths.get(path);
            Path targetDirectory = Paths.get(directoryFiles + file.getName());

            Files.copy(sourceDirectory, targetDirectory);
            
            createLabel(file.getName());
            
            newListOfFile = getListOfFiles();
            outStream.writeObject(newListOfFile);
            outStream.flush();            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    /**
     * ActionListener for the connection button
     * 
     */
    class LoginClick implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            exist = true;
			connect();
        }
    }

    /**
     * ActionListener for the sign in button 
     *
     */
    class SignInClick implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            exist = false;
			connect();
        }
    }

    /**
     * ActionListener for the add file button
     *
     */
    class addFile implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            int resultat = myJfileChooser.showOpenDialog(pnlServer);

            if (resultat == JFileChooser.CANCEL_OPTION) 
            {
                myJfileChooser.cancelSelection();
                return;
            }

            if (resultat == JFileChooser.APPROVE_OPTION) 
            {
                saveToDirectory(myJfileChooser.getSelectedFile().getAbsolutePath());
                pnlSharedFiles.add(pnlListShared, BorderLayout.CENTER);
            }
        }
    }

    /** 
     * ActionListener for the send message button
     *
     */
    private class sendMessage implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            sendMessage();
        }
    }

    /**
     * Method for sending a Message to the server
     * 
     */
	private void sendMessage() 
	{
		try 
		{
            outStream.writeObject(new Message(txtFMsgSend.getText(), myClient));
            outStream.flush();
            txtFMsgSend.setText("");
        } 
		catch (IOException e1) 
		{
            e1.printStackTrace();
        }
	}
	
	/**
	 * ActionListener for the list of shared file from other Clients 
	 *
	 */
    private class SelectionChanged implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            model.removeAllElements(); //enl�ve l'affichage des fichiers dans la liste

            if (jcbobxForClient.getItemCount() > 0) { //si il y a quelque chose dans la liste
                //remplissage du panel par rapport aux clients connecter.
                for (String myFile : listOfClients.get(jcbobxForClient.getSelectedIndex()).getListOfFiles()) 
                {
                    model.addElement(myFile);
                }
            }
        }
    }
    
    /**
     * ActionListener for the dowload button
     *
     */
    private class DownloadButtonClick implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            String myFile = model.get(JlstFile.getSelectedIndex());         
            Client target = listOfClients.get(jcbobxForClient.getSelectedIndex());
            FileRequest fr = new FileRequest(myFile, myClient, target);

            new Thread(new Runnable() 
            {
                @Override
                public void run() 
                {
                    //se connecter au server
                    try 
                    {
                        requestClient = new Socket(fr.getTarget().getIp(), 45001);
                        inStreamRequestClient = requestClient.getInputStream();
                        outStreamRequestClient = new ObjectOutputStream(requestClient.getOutputStream());
                        outStreamRequestClient.writeObject(fr);
                        Files.copy(inStreamRequestClient, Paths.get(directoryFiles + fr.getNameFile()));
                    } 
                    catch (IOException e) 
                    {
                        lblErrorServer.setText("Please select a file to download!");
                        frame.repaint();
                        frame.validate();
                    }
                }
            }).start();
        }
    }
    
    /**
     *KeyListener for the enter key
     *
     */
    private class KeySender extends KeyAdapter 
    {
    	public void keyPressed(KeyEvent e) 
    	{
            if(e.getKeyCode() == KeyEvent.VK_ENTER)
            {
            	sendMessage();
            }
    	}
	}
}
