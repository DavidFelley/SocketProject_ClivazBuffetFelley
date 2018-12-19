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
		
		btnPower.addActionListener(new ButtonExit() );
		
		btnPower.setBounds(12, 587, 128, 57);
		panelServer.add(btnPower);


		JLabel lblChat = new JLabel("CHAT");
		lblChat.setForeground(Color.RED);
		lblChat.setBackground(Color.WHITE);
		lblChat.setBounds(962, 422, 56, 16);
		panelServer.add(lblChat);
		
		panelInfoServer.setBackground(Color.GRAY);

		panelInfoServer.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED, Color.RED, Color.RED));

		panelInfoServer.setBounds(755, 35, 315, 372);

		panelInfoServer.setLayout(new BoxLayout(panelInfoServer, BoxLayout.Y_AXIS));

		JLabel lblServer = new JLabel("SERVER");
		lblServer.setForeground(Color.RED);
		lblServer.setBounds(948, 0, 59, 29);
		panelServer.add(lblServer);



		JPanel panelChat = new JPanel();
		panelChat.setBackground(Color.GRAY);
		panelChat.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED, Color.RED, Color.RED));
		panelChat.setBounds(831, 451, 315, 193);
		panelServer.add(panelChat);

		JPanel panelListFiles = new JPanel();
		panelListFiles.setBackground(Color.GRAY);
		panelListFiles.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED, Color.RED, Color.RED));
		panelListFiles.setBounds(30, 39, 295, 372);
		panelServer.add(panelListFiles);
		panelListFiles.setLayout(new BorderLayout(0, 0));

		Choice choice = new Choice();
		panelListFiles.add(choice, BorderLayout.NORTH);

		JLabel LabelListOfFiles = new JLabel("LIST OF FILES");
		LabelListOfFiles.setForeground(Color.RED);
		LabelListOfFiles.setBounds(130, 3, 90, 23);
		panelServer.add(LabelListOfFiles);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(822, 42, 324, 304);
		scrollPane.setViewportView(panelInfoServer);
		panelServer.add(scrollPane);

		frmServer.repaint();
		frmServer.validate();
		frmServer.setVisible(true);
	}

	public void createLabel(String message)
	{
		JLabel lbl = new JLabel( message);

		panelInfoServer.add(lbl);
		panelInfoServer.validate();
		panelInfoServer.repaint();
	}

	

	private static void addPopup(Component component, final JPopupMenu popup)
	{
		component.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) 
			{
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}

class ButtonExit implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent e) {

		
		System.exit(0);
	}
	
}
