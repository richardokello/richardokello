package ke.tra.ufs.webportal.service;

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
