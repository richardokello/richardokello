package ke.tra.com.tsync.services.template;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.io.IOException;

public interface SendToSwitch {

    ISOMsg sendtoSwitch(ISOMsg isoMsg) throws ISOException, IOException;
}
