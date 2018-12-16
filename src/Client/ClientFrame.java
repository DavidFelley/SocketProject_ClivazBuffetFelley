package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import javax.swing.border.LineBorder;

public class ClientFrame 
{
	//Variables of connection
	private String login = "";
	private String password = "";
	private String ipServer = "";
	private String ipClient = "";
	private Socket clientSocket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	
	//Variables graphiques
	private JFrame frame;
	private JTextField loginField;
	private JTextField serverField;
	private JPasswordField passwordField;
	
	CardLayout cardlayout = new CardLayout();
	JPanel mainPanel = new JPanel(cardlayout);

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
		panelLogin.add(btnSignIn);

		JPanel panelServer = new JPanel();
		mainPanel.add(panelServer, "panelServer");
		panelServer.setBackground(Color.DARK_GRAY);
		panelServer.setLayout(null);
		
		frame.setVisible(true);
		
	}

	private void connect() throws IOException
	{	
		clientSocket = new Socket();
		
		ipServer = serverField.getText();

		InetSocketAddress serverSocket = new InetSocketAddress(ipServer, 45000);
		
		clientSocket.connect(serverSocket, 5);
		
		oos = new ObjectOutputStream(clientSocket.getOutputStream());
		ois = new ObjectInputStream(clientSocket.getInputStream());
		
		oos.writeObject(sendInformations());
		
	}

	
	private Client sendInformations()
	{
		login = loginField.getText();
		
		ipClient = clientSocket.getLocalAddress().getHostAddress();
		
		File listOfFiles[] = getListOfFiles();
		
		Client client = new Client(login, ipClient, listOfFiles);
		
		return client;
		
	}
	
	private File[] getListOfFiles()
	{
		File directory = new File("C:\\SharedDocuments");
		
		if(!directory.exists())
			directory.mkdir();
		
		File  files [] = directory.listFiles();
		
		return files;
	}
	
	class LoginClick implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			try 
			{
				connect();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			
			cardlayout.show(mainPanel, "panelServer");
			
		}

	}
}
