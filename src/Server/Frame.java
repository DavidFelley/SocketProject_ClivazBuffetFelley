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

public class Frame {

	private JFrame frmServer;
	JPanel panel_1 = new JPanel();

	/**
	 * Create the application.
	 */
	public Frame() 
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
		frmServer.setAlwaysOnTop(true);
		frmServer.setForeground(Color.GRAY);
		frmServer.setVisible(true);
		frmServer.setBounds(100, 100, 1250, 703);
		frmServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		frmServer.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		
		JButton btnNewButton = new JButton("ShutDown");
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		btnNewButton.setBackground(Color.RED);
		btnNewButton.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED, Color.RED, Color.RED));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				System.exit(0);
			}
		});
		btnNewButton.setBounds(12, 587, 128, 57);
		panel.add(btnNewButton);
		

		JLabel lblNewLabel_1 = new JLabel("CHAT");
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setBackground(Color.WHITE);
		lblNewLabel_1.setBounds(962, 422, 56, 16);
		panel.add(lblNewLabel_1);
		panel_1.setBackground(Color.GRAY);
		
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED, Color.RED, Color.RED));
		
		panel_1.setBounds(755, 35, 315, 372);
		
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JLabel lblNewLabel = new JLabel("SERVER");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(948, 0, 59, 29);
		panel.add(lblNewLabel);
		
		
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.GRAY);
		panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED, Color.RED, Color.RED));
		panel_2.setBounds(831, 451, 315, 193);
		panel.add(panel_2);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.GRAY);
		panel_3.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED, Color.RED, Color.RED));
		panel_3.setBounds(30, 39, 295, 372);
		panel.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		Choice choice = new Choice();
		panel_3.add(choice, BorderLayout.NORTH);
		
		JLabel lblNewLabel_2 = new JLabel("LIST OF FILES");
		lblNewLabel_2.setForeground(Color.RED);
		lblNewLabel_2.setBounds(130, 3, 90, 23);
		panel.add(lblNewLabel_2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(822, 42, 324, 304);
		scrollPane.setViewportView(panel_1);
		panel.add(scrollPane);
		
		frmServer.repaint();
		frmServer.validate();	
	}
	
	public void createLabel(String message)
	{
		JLabel lbl = new JLabel( message);
		
		panel_1.add(lbl);
		panel_1.validate();
		panel_1.repaint();
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
