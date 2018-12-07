package Server;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

    // ====================================================================================================== //
  	// ------------------------------------ Class : SocketFormatter ---------------------------------------   //
  	// ====================================================================================================== //
  	// Nom de la méthode : SocketFormatter                                                                    //
  	// Description de la méthode : Cette classe gère le format du fichier log du client.                      //
  	// ------------------------------------------------------------------------------------------------------ //
  	// Extension : Formatter                                                                                  //
  	// Entrée(s) : -                                                                                          //
  	// Sortie : -                                                                                             //
  	// ------------------------------------------------------------------------------------------------------ //
  	// Remarque : -                                                                                           //
  	// ------------------------------------------------------------------------------------------------------ //
 	public class SocketFormatter extends Formatter {

 		// CONSTRUCTEUR
 		public SocketFormatter() {
 			super();
 		}

 		// FORMAT
 		public String format(LogRecord record) {

 			// Création d'un StringBuffer pour contenir le format
 			StringBuffer sb = new StringBuffer();

 			// Récupérer la date et l'ajouter au buffer
 			Date date = new Date(record.getMillis());
 			sb.append(date.toString());
 			sb.append(";");

 			sb.append(record.getSourceClassName());
 			sb.append(";");

 			// Récupérer le nom du niveau et l'ajouter au buffer
 			sb.append(record.getLevel().getName());
 			sb.append(";");

 			sb.append(formatMessage(record));
 			sb.append("\r\n");

 			return sb.toString();
 		}
 	}