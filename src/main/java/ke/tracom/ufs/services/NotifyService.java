package ke.tracom.ufs.services;

import ke.tracom.ufs.utils.enums.MessageType;

/**
 * Used to handle notifications either to email or remote clients
 * @author cornelius
 *
 */
public interface NotifyService {
	
	/**
	 * 
	 * @param emailAddress
	 * @param title
	 * @param message
	 */
	public void sendEmail(String emailAddress, String title, String message);

}
