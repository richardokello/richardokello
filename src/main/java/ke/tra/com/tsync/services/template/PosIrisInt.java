package ke.tra.com.tsync.services.template;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.util.HashMap;

public interface PosIrisInt {

    ISOMsg receiveHeartBeat(ISOMsg isoMsg) throws ISOException;
}
