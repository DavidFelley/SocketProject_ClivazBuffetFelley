package Server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Cursor;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Choice;

public class ServerFrame {

	private JFrame frmServer;
	private JPanel panelInfoServer = new JPanel(); 
	/**
	 * Create the application.
	 */
	public ServerFrame() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frmServer = new JFrame();
		frmServer.setResizable(false);
		frmServer.setTitle("SERVER");
		frmServer.setForeground(Color.GRAY);
		frmServer.setBounds(100, 100, 1250, 703);
		frmServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panelServer = new JPanel();
		panelServer.setBackground(Color.GRAY);
		frmServer.getContentPane().add(panelServer, BorderLayout.CENTER);
		panelServer.setLayout(null);

		JButton btnPower = new JButton("ShutDown");
		btnPower.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		btnPower.setBackground(Color.RED);
		btnPower.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED, Color.RED, Color.RED));
		
		btnPower.addActionListener(new powerOff());
		btnPower.setBounds(512, 587, 128, 57);
		panelServer.add(btnPower);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(89, 11, 1046, 539);
		scrollPane.setViewportView(panelInfoServer);
		scrollPane.setVisible(true);
		panelServer.add(scrollPane);
		
		
		panelInfoServer.setBackground(Color.GRAY);
		panelInfoServer.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED, Color.RED, Color.RED));
		panelInfoServer.setBounds(36, 11, 1050, 543);
		panelInfoServer.setLayout(new BoxLayout(panelInfoServer, BoxLayout.Y_AXIS));

		frmServer.setVisible(true);
	}

	public void createLabel(String message)
	{
		JLabel lbl = new JLabel( message);

		panelInfoServer.add(lbl);
		panelInfoServer.validate();
		panelInfoServer.repaint();
	}
	
	class powerOff implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			System.exit(0);
		}
	}
}
