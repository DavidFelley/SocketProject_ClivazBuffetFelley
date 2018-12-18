package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.Color;
import java.awt.CardLayout;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.Button;
import java.awt.GridLayout;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JScrollBar;

public class ClientFrame 
{
	//Variables of connection
	private String login = "";
	private String password = "";
	private String ipServer = "";
	private String ipClient = "";
	String  files [] ;
	private Socket clientSocket = null;
	private BufferedReader buffin;
	private PrintWriter pout;
	private JFileChooser fc = new JFileChooser();

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

		panelServer = new JPanel();
		mainPanel.add(panelServer, "panelServer");
		panelServer.setBackground(Color.DARK_GRAY);
		panelServer.setLayout(null);

		JPanel panelFiles = new JPanel();
		panelFiles.setBounds(0, 0, 280, 420);
		panelServer.add(panelFiles);

		JList listSharedFiles = new JList();
		panelFiles.add(listSharedFiles);

		panelSharedFiles = new JPanel();
		panelSharedFiles.setBounds(703, 0, 291, 420);
		panelServer.add(panelSharedFiles);
		panelSharedFiles.setLayout(new BorderLayout(10, 0));

		JPanel panel = new JPanel();
		panelSharedFiles.add(panel, BorderLayout.NORTH);

		JLabel lblSharedFiles = new JLabel("Shared Files");
		panel.add(lblSharedFiles);

		panelListShared= new JPanel();
		panelSharedFiles.add(panelListShared, BorderLayout.CENTER);

		JButton btnAddFile = new JButton("Add File");
		btnAddFile.addActionListener(new addFile());
		btnAddFile.setBounds(703, 446, 97, 25);
		panelServer.add(btnAddFile);

		frame.setVisible(true);

	}

	private void addFileInList(String path)
	{
		JLabel lblfileName = new JLabel(path);
		panelListShared.add(lblfileName);

		panelSharedFiles.validate();
		panelSharedFiles.repaint();

	}

	private void connect() throws IOException
	{	
		clientSocket = new Socket();

		ipServer = serverField.getText();

		InetSocketAddress serverSocket = new InetSocketAddress(ipServer, 45000);

		clientSocket.connect(serverSocket);

		//create an input stream to read data from the server
		buffin = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));

		//open the output data stream to write on the client
		pout = new PrintWriter(clientSocket.getOutputStream());

		getListOfFiles();

		login = loginField.getText();

		System.out.println(buffin.readLine());

	}

	private String[] getListOfFiles()
	{
		File directory = new File("C:\\SharedDocuments");

		if(!directory.exists())
			directory.mkdir();

		files = new String [directory.list().length];

		for (int i = 0; i < directory.list().length; i++) 
		{
			File [] lst = directory.listFiles();
			files[i] = lst[i].getAbsolutePath();

			pout.println(files[i]);
			pout.flush();

			addFileInList(files[i]);
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
				connect();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}

			cardlayout.show(mainPanel, "panelServer");

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
			}
		}
	}


	private void saveToDirectory(String path) 
	{

		try{
			File file = new File(path);

			if(file.renameTo(new File("C:\\SharedDocuments\\" + file.getName())));

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
