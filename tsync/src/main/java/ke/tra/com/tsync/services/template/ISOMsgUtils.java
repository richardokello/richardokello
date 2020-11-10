package ke.tra.com.tsync.services.template;


import entities.FailedTransactions;
import entities.OnlineActivity;
import org.jpos.iso.ISOMsg;

/**
 * Kamoni
 */

public interface ISOMsgUtils{
    public OnlineActivity convertIsoToPojo(ISOMsg messageRequest);
    public FailedTransactions convertFailedIsoToPojo(ISOMsg messageRequest);

}
