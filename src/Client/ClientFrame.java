package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.Font;
import java.awt.Color;
import java.awt.CardLayout;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class ClientFrame
{
	//Variables of connection
	private Client myClient = null;
	private String login = "";
	private String password = "";
	private String ipClient = "";
	private String ipServer = "";
	private String [] listOfFiles = null ;
	private boolean exist;
	private Socket clientSocket = null;
	private ObjectOutputStream outStream = null;
	private ObjectInputStream inStream = null;
	private BufferedReader buffin = null;
	private JFileChooser myJfileChooser = new JFileChooser();
	private ArrayList<Client> listOfClients = new ArrayList<>();

	//Variables graphiques
	private JFrame frame;
	private JTextField jtxtfLogin;
	private JTextField jtxtfServer;
	private JPasswordField jtxtfPassword;
	private JPanel pnlSharedFiles ;
	private JPanel pnlListShared;
	private CardLayout myCardLayout = new CardLayout();
	private JPanel pnlMain = new JPanel(myCardLayout);
	private JPanel pnlServer;
	private JLabel lblLogin;
	private JLabel lblError;
	private JComboBox jcbobxForClient;
	private JTextField txtFMsgSend;
	private JPanel pnlFileListes;
	private JScrollPane scrollChat;
	private JTextArea txtAreaChat;


	/**
	 * Create the application.
	 */
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

		lblLogin = new JLabel("Login");
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

		lblError = new JLabel("",SwingConstants.CENTER);
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

		pnlFileListes = new JPanel();
		panelFiles.add(pnlFileListes, BorderLayout.CENTER);

		jcbobxForClient = new JComboBox<String>();
		jcbobxForClient.addActionListener(new SelectionChanged());
		panelFiles.add(jcbobxForClient, BorderLayout.NORTH);

		pnlSharedFiles = new JPanel();
		pnlSharedFiles.setBounds(731, 13, 251, 420);
		pnlServer.add(pnlSharedFiles);
		pnlSharedFiles.setLayout(new BorderLayout(10, 0));

		JPanel panel = new JPanel();
		pnlSharedFiles.add(panel, BorderLayout.NORTH);

		JLabel lblSharedFiles = new JLabel("My Files");
		panel.add(lblSharedFiles);

		pnlListShared= new JPanel();
		pnlSharedFiles.add(pnlListShared, BorderLayout.CENTER);

		JButton btnAddFile = new JButton("Add File");
		btnAddFile.addActionListener(new addFile());
		btnAddFile.setBounds(731, 448, 97, 25);
		pnlServer.add(btnAddFile);

		JButton btnDownload = new JButton("Download");
		btnDownload.setBounds(12, 448, 97, 25);
		pnlServer.add(btnDownload);

		JButton btnSend = new JButton("New button");
		btnSend.addActionListener(new sendMessage());
		btnSend.setBounds(632, 449, 89, 23);
		pnlServer.add(btnSend);

		txtFMsgSend = new JTextField();
		txtFMsgSend.setBounds(272, 448, 350, 22);
		pnlServer.add(txtFMsgSend);
		txtFMsgSend.setColumns(10);
		
		scrollChat = new JScrollPane();
		scrollChat.setBounds(273, 13, 449, 420);
		pnlServer.add(scrollChat);
		
		txtAreaChat = new JTextArea();
		scrollChat.setViewportView(txtAreaChat);

		jtxtfServer.setText("192.168.43.242");
		jtxtfLogin.setText("loan");
		jtxtfPassword.setText("1234");

		DefaultCaret caret = (DefaultCaret)txtAreaChat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		frame.setVisible(true);
	}

	private void addFileInList(String [] listOfFiles)
	{
		for (int i = 0; i < listOfFiles.length; i++)
		{
			JLabel lblfileName = new JLabel(listOfFiles[i]);
			pnlListShared.add(lblfileName);
		}

		pnlSharedFiles.validate();
		pnlSharedFiles.repaint();
	}

	private void connect() throws IOException
	{
		ipServer = jtxtfServer.getText();
		clientSocket = new Socket(ipServer, 45000);
		inStream = new ObjectInputStream(clientSocket.getInputStream());
		outStream = new ObjectOutputStream(clientSocket.getOutputStream());
		buffin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		login = jtxtfLogin.getText();
		password = jtxtfPassword.getText();
		ipClient = clientSocket.getLocalAddress().getHostAddress();
		listOfFiles = getListOfFiles();

		myClient = new Client(login, password, ipClient, listOfFiles, exist);

		outStream.writeObject(myClient);
		int controle = inStream.readInt();
		controleConnection(controle);

	}

	//CETTE METHODE DOIT PERMETTRE AU CLIENT D'ECOUTER EN PERMANENCE LE SERVEUR POUR SAVOIR SI DE NOUVELLES PERSONNES SONT CONNECTEES
	private void listenServer()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Object o = inStream.readObject();
						if(o instanceof Message) {
							Message m = (Message)o;
							String sender = m.getClient().getName().equals(myClient.getName()) ? "Me" : m.getClient().getName();
							txtAreaChat.append(sender + " : " + m.getMessage() + "\n");
						}

						if(o instanceof ArrayList){
							if(((ArrayList) o).size() > 0 && ((ArrayList) o).get(0) instanceof Client){
								listOfClients = (ArrayList<Client>)o;
								if(jcbobxForClient.getItemCount() >=1)
									jcbobxForClient.removeAllItems();
								for (Client thisClient : listOfClients) {
									jcbobxForClient.addItem(thisClient);
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void controleConnection(int value)
	{
		switch (value)
		{
			case 0:
				lblError.setText("Wrong user or password");
				frame.repaint();
				frame.validate();
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

			default :
				lblError.setText("Unknown Error please try again");
				frame.repaint();
				frame.validate();
		}
	}

	private String [] getListOfFiles()
	{
		File directory = new File("C:\\SharedDocuments");

		if(!directory.exists())
			directory.mkdir();

		String [] files = new String [directory.list().length];
		File [] lst = directory.listFiles();

		for (int i = 0; i < files.length; i++)
		{
			files[i] = lst[i].getName();
		}

		return files;
	}

	class LoginClick implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				exist = true;
				connect();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	class SignInClick implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				exist = false;
				connect();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	class addFile implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int resultat = myJfileChooser.showOpenDialog(pnlServer);

			if(resultat == myJfileChooser.CANCEL_OPTION)
			{
				myJfileChooser.cancelSelection();
				return;
			}

			if (resultat == myJfileChooser.APPROVE_OPTION)
			{
				saveToDirectory(myJfileChooser.getSelectedFile().getAbsolutePath());
				pnlSharedFiles.add(pnlListShared, BorderLayout.CENTER);
			}
		}
	}

	private void saveToDirectory(String path)
	{
		try
		{
			File file = new File(path);
			Path sourceDirectory = Paths.get(path);
			Path targetDirectory = Paths.get("C:\\SharedDocuments\\"+file.getName());

			//copy source to target using Files Class
			Files.copy(sourceDirectory, targetDirectory);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private class sendMessage implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				outStream.writeObject(new Message(txtFMsgSend.getText(), myClient));
				outStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private class SelectionChanged implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			pnlFileListes.removeAll();
			if(jcbobxForClient.getItemCount()> 0){
				for (String myFile : listOfClients.get(jcbobxForClient.getSelectedIndex()).getListOfFiles()) {
					JLabel theLabel = new JLabel(myFile);
					theLabel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							// ICI ON TROUVE COMMENT ENVOYER FICHIER A L'AUTRE GUSSE
						}
					});
					pnlFileListes.add(theLabel);
				}
				System.out.println(listOfClients.get(jcbobxForClient.getSelectedIndex()).getListOfFiles());
			}
		}
	}
}
