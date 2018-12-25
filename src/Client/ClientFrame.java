package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JComboBox;

public class ClientFrame 
{
	//Variables of connection
	private Client client = null;
	private String login = "";
	private String password = "";
	private String ipClient = "";
	private String ipServer = "";
	private String [] listOfFiles = null ;
	private boolean exist;
	private Socket clientSocket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream ois = null;
	private BufferedReader buffin = null;
	private JFileChooser fc = new JFileChooser();
	private ArrayList<Client> listOfClients = new ArrayList<>();

	//Variables graphiques
	private JFrame frame;
	private JTextField loginField;
	private JTextField serverField;
	private JPasswordField passwordField;
	private JPanel panelSharedFiles ;
	private JPanel panelListShared;
	private CardLayout cardlayout = new CardLayout();
	private JPanel mainPanel = new JPanel(cardlayout);
	private JPanel panelServer;
	private JLabel lblLogin;
	private JLabel lblError;
	private JComboBox cbOnlineUser;

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

		frame.getContentPane().add(mainPanel);

		JPanel panelLogin = new JPanel();
		mainPanel.add(panelLogin, "panelLogin");
		panelLogin.setBackground(Color.DARK_GRAY);
		panelLogin.setLayout(null);

		loginField = new JTextField();
		loginField.setBounds(332, 78, 330, 22);
		panelLogin.add(loginField);
		loginField.setColumns(10);

		serverField = new JTextField();
		serverField.setBounds(332, 396, 330, 22);
		panelLogin.add(serverField);
		serverField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(332, 237, 330, 22);
		panelLogin.add(passwordField);

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

		panelServer = new JPanel();
		mainPanel.add(panelServer, "panelServer");
		panelServer.setBackground(Color.DARK_GRAY);
		panelServer.setLayout(null);

		JPanel panelFiles = new JPanel();
		panelFiles.setBounds(12, 13, 251, 420);
		panelServer.add(panelFiles);
		panelFiles.setLayout(new BorderLayout(0, 0));

		JPanel panelListFiles = new JPanel();
		panelFiles.add(panelListFiles, BorderLayout.CENTER);

		cbOnlineUser = new JComboBox<String>();
		panelFiles.add(cbOnlineUser, BorderLayout.NORTH);

		panelSharedFiles = new JPanel();
		panelSharedFiles.setBounds(731, 13, 251, 420);
		panelServer.add(panelSharedFiles);
		panelSharedFiles.setLayout(new BorderLayout(10, 0));

		JPanel panel = new JPanel();
		panelSharedFiles.add(panel, BorderLayout.NORTH);

		JLabel lblSharedFiles = new JLabel("My Files");
		panel.add(lblSharedFiles);

		panelListShared= new JPanel();
		panelSharedFiles.add(panelListShared, BorderLayout.CENTER);

		JButton btnAddFile = new JButton("Add File");
		btnAddFile.addActionListener(new addFile());
		btnAddFile.setBounds(731, 448, 97, 25);
		panelServer.add(btnAddFile);

		JButton btnDownload = new JButton("Download");
		btnDownload.setBounds(12, 448, 97, 25);
		panelServer.add(btnDownload);

		frame.setVisible(true);
	}

	private void addFileInList(String [] listOfFiles)
	{
		for (int i = 0; i < listOfFiles.length; i++) 
		{
			JLabel lblfileName = new JLabel(listOfFiles[i]);
			panelListShared.add(lblfileName);
		}

		panelSharedFiles.validate();
		panelSharedFiles.repaint();
	}

	private void connect() throws IOException
	{	
		ipServer = serverField.getText();
		clientSocket = new Socket(ipServer, 45000);

		out = new ObjectOutputStream(clientSocket.getOutputStream());
		buffin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		login = loginField.getText();
		password = passwordField.getText();
		ipClient = clientSocket.getLocalAddress().getHostAddress();
		listOfFiles = getListOfFiles();

		client = new Client(login, password, ipClient, listOfFiles, exist);

		out.writeObject(client);

		String controle = buffin.readLine();
		
		controleConnection(controle);

	}

	//CETTE METHODE DOIT PERMETTRE AU CLIENT D'ECOUTER EN PERMANENCE LE SERVEUR POUR SAVOIR SI DE NOUVELLES PERSONNES SONT CONNECTEES
	private void listenServer()
	{
		while(true)
		{
			try 
			{
				System.out.println(buffin.readLine());
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void controleConnection(String value)
	{
		switch (value) 
		{
		case "0":
			lblError.setText("Wrong user or password");
			frame.repaint();
			frame.validate();

		case "1":
			addFileInList(client.getListOfFiles());
			cardlayout.show(mainPanel, "panelServer");
			listenServer();

		case "2":
			lblError.setText("User already exist");
			frame.repaint();
			frame.validate();

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
			int resultat = fc.showOpenDialog(panelServer);

			if(resultat == fc.CANCEL_OPTION)
			{
				fc.cancelSelection();
				return;
			}

			if (resultat == fc.APPROVE_OPTION) 
			{
				saveToDirectory(fc.getSelectedFile().getAbsolutePath());
				panelSharedFiles.add(panelListShared, BorderLayout.CENTER);
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
}
