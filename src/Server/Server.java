package Server;
import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
	
	protected static Logger myLogger;
	protected static SimpleDateFormat formater = null;
	protected static Date date;
	protected static Calendar cal;
	protected static int refDay;
	private FileHandler fh;
	
	public Server() 
	{
		createLogs();
	
	}

	public static void main(String[] args){
		
		Server a = new Server ();

		InetAddress localAddress = null;
		ServerSocket mySkServer;
		String interfaceName = "eth1";
		
		

		int ClientNo = 1;
		
		try {
			NetworkInterface ni = NetworkInterface.getByName(interfaceName);
	        Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
			while(inetAddresses.hasMoreElements()) {
	            InetAddress ia = inetAddresses.nextElement();
	            
	            if(!ia.isLinkLocalAddress()) {
	               if(!ia.isLoopbackAddress()) {
	            	   System.out.println(ni.getName() + "->IP: " + ia.getHostAddress());
	            	   localAddress = ia;
	               }
	            }   
            }
			
			//Warning : the backlog value (2nd parameter is handled by the implementation
			mySkServer = new ServerSocket(45000,10,localAddress);
			System.out.println("Default Timeout :" + mySkServer.getSoTimeout());
			System.out.println("Used IpAddress :" + mySkServer.getInetAddress());
			System.out.println("Listening to Port :" + mySkServer.getLocalPort());
			
		
			//wait for a client connection
			while(true)
			{
     		   Socket clientSocket = mySkServer.accept();
               System.out.println("connection request received");
               Thread t = new Thread(new AccepteClient(clientSocket,ClientNo));
               ClientNo++;
     		   //starting the thread
    		   t.start();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
		
		
	}
	public  void createLogs() {
		// Déclaration des variables
		myLogger = Logger.getLogger("TestLog");
		formater = null;
		date = new Date();
		cal = Calendar.getInstance();
		cal.setTime(date);
		 // Jour de référence pour le fichier log au lancement du serveur
		refDay = cal.get(Calendar.DAY_OF_MONTH);
		formater = new SimpleDateFormat("yyyyMMdd");

		try 
		{
			fh = new FileHandler("C:/Leaf/server/logs/" + formater.format(date) + ".log", true);
			myLogger.addHandler(fh);

			// Utilisation du format défini
			SocketFormatter myFormatter = new SocketFormatter();
			fh.setFormatter(myFormatter);
			
		} 
		catch (SecurityException ex) 
		{
			myLogger.setLevel(Level.SEVERE);
			myLogger.severe("SecurityException :"+ex.toString());
		} 
		catch (IOException ex) 
		{
			myLogger.setLevel(Level.SEVERE);
			myLogger.severe("IOException :"+ex.toString());
		}
	}
}