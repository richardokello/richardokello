package ke.tra.com.tsync.org.jpos.hostparticipants;


import org.jpos.core.Card;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.InvalidCardException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.ContextConstants;
import org.jpos.transaction.TransactionParticipant;

import java.io.Serializable;


public class SelectDestination implements TransactionParticipant, Configurable {
	Configuration cfg;
	@Override
	public int prepare(long id, Serializable context) {
		Context ctx = (Context) context;
		ISOMsg m = (ISOMsg) ctx.get(ContextConstants.REQUEST.toString());
		if (m != null && (m.hasField(2) || m.hasField(35))) {
			try {
				Card card = Card.builder().isomsg(m).build();
				String s = cfg.get("bin." + card.getBin(), null);
				if (s != null) {
					ctx.put(ContextConstants.DESTINATION.toString(), s);
				}
			} catch (InvalidCardException ignore) {
				// use default destination
			}
		}
		System.out.println(":::: = SelectDestination Processing...");
		return PREPARED | NO_JOIN | READONLY;
	}
	public void setConfiguration (Configuration cfg) {
		this.cfg = cfg;
	}
}