package ke.tra.com.tsync.services.template;

import ke.tra.com.tsync.entities.FailedTransactions;
import ke.tra.com.tsync.entities.OnlineActivity;
import org.jpos.iso.ISOMsg;

/**
 * Kamoni
 */

public interface ISOMsgUtils{
    public OnlineActivity convertIsoToPojo(ISOMsg messageRequest);
    public  FailedTransactions convertFailedIsoToPojo(ISOMsg messageRequest);

}
