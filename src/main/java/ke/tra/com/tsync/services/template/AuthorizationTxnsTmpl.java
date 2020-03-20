package ke.tra.com.tsync.services.template;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.util.HashMap;

public interface AuthorizationTxnsTmpl {


    ISOMsg processAuthbyProcode(ISOMsg isoMsg, HashMap<String, Object> fieldDataMap) throws ISOException;
}
