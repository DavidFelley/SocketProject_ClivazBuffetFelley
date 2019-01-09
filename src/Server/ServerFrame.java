package Server;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerFrame {

    private JFrame frmServer;
    private static JPanel pnlInfoServer = new JPanel();

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

        JPanel pnlServer = new JPanel();
        pnlServer.setBackground(Color.LIGHT_GRAY);
        frmServer.getContentPane().add(pnlServer, BorderLayout.CENTER);
        pnlServer.setLayout(null);

        JButton btnPower = new JButton("ShutDown");
        btnPower.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        btnPower.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        btnPower.setBackground(Color.LIGHT_GRAY);
        btnPower.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK, Color.WHITE, Color.BLACK));

        btnPower.addActionListener(new powerOff());
        btnPower.setBounds(512, 587, 128, 57);
        pnlServer.add(btnPower);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(89, 11, 1046, 539);
        pnlInfoServer.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        scrollPane.setViewportView(pnlInfoServer);
        scrollPane.setVisible(true);
        pnlServer.add(scrollPane);


        pnlInfoServer.setBackground(Color.WHITE);
        pnlInfoServer.setBounds(36, 11, 1050, 543);
        pnlInfoServer.setLayout(new BoxLayout(pnlInfoServer, BoxLayout.Y_AXIS));

        frmServer.setVisible(true);
    }

    public static void createLabel(String message) {
        JLabel lbl = new JLabel(message);

        pnlInfoServer.add(lbl);
        pnlInfoServer.validate();
        pnlInfoServer.repaint();
    }

    class powerOff implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            System.exit(0);
        }
    }
}
