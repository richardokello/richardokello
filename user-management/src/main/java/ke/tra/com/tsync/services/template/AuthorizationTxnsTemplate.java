package ke.tra.com.tsync.services.template;

import ke.tra.com.tsync.wrappers.PosUserWrapper;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.util.HashMap;

public interface AuthorizationTxnsTemplate {


    ISOMsg processAuthbyProcode(ISOMsg isoMsg, PosUserWrapper wrapper) throws ISOException;
}
