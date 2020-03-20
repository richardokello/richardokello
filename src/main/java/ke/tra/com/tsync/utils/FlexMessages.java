package ke.tra.com.tsync.utils;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;


@SuppressWarnings("Duplicates")
@Component
public class FlexMessages {

    @Autowired
    private GeneralFuncs generalFuncs;

    private static final org.slf4j.Logger FLEXllogger = LoggerFactory.getLogger(FlexMessages.class.getName());

    public ISOMsg FlexBalanceMessage(ISOMsg isoFromPos) throws ISOException {
        ISOMsg isoMsg = new ISOMsg();
        /**
         * y = year (yy or yyyy) M = month (MM) d = day in month (dd) h = hour
         * (0-12) (hh) H = hour (0-23) (HH) m = minute in hour (mm) s = seconds
         * (ss) S = milliseconds (SSS) z = time zone text (e.g. Pacific Standard
         * Time...) Z = time zone, time offset (e.g. -0800)
         */
        //System.out.println(" Date " + new java.text.SimpleDateFormat("MMddhhmmS").format(new java.util.Date()));
        isoMsg.setMTI("0200");
        isoMsg.set(2, "6273489999999999");
        isoMsg.set(3, "310000");
        isoMsg.set(4, "000000000000");
        isoMsg.set(7, new java.text.SimpleDateFormat("MMddhhmmss").format(new java.util.Date())); //MMDDhhmmss
        isoMsg.set(11, ISOUtil.padleft(generalFuncs.getNextStanNumber(), 6, '0'));
        isoMsg.set(12, new java.text.SimpleDateFormat("hhmmss").format(new java.util.Date()));//hhmmss
        isoMsg.set(13, new java.text.SimpleDateFormat("MMdd").format(new java.util.Date())); //CCYYMM
        isoMsg.set(18, "6010");
        isoMsg.set(22, "901");
        isoMsg.set(25, "00");
        isoMsg.set(26, "12");
        isoMsg.set(28, "C00000000"); // was in sample
        isoMsg.set(32, "627348");
        isoMsg.set(33, "627348");
        isoMsg.set(35, "6273489999999999=200750100000");


        isoMsg.set(37,isoFromPos.getString(37));
        isoMsg.set(41, "22838680");
        isoMsg.set(42, "627348000000006");
        isoMsg.set(43, "Carltech Printing Services-Kenyatta HiKE");
        // isoMsg.set(47, "025007Mv T1.0030006MRB0010350055555503700140260141519WL81344394");// hii toa
        isoMsg.set(49, "404");
        isoMsg.set(60, "27");

        isoMsg.set(102, "0015270214001");
       // isoMsg.set(102, isoFromPos.getString(102));
        return isoMsg;
    }


    public ISOMsg FlexFundTransfer(String debit_acc, String credit_acc, String amount , String refno)  {
        try {
            // for deposit debit float credit customer account
            ISOMsg ftFlex = new ISOMsg();

            //String de4 = String.format(Locale.ENGLISH, "%03d",amount );

            //We need a flex field for narration

            //System.out.println(" Date " + new java.text.SimpleDateFormat("MMddhhmmS").format(new java.util.Date()));
            ftFlex.setMTI("0200");
            ftFlex.set(2, "6273489999999999");
            ftFlex.set(3, "420000");
            ftFlex.set(4, amount);
            ftFlex.set(7, new java.text.SimpleDateFormat("ddMMhhmmss").format(new java.util.Date())); //MMDDhhmmss
            ftFlex.set(11, ISOUtil.padleft(generalFuncs.getNextStanNumber(), 6, '0'));
            ftFlex.set(12, new java.text.SimpleDateFormat("hhmmss").format(new java.util.Date()));//hhmmss
            ftFlex.set(13, new java.text.SimpleDateFormat("MMdd").format(new java.util.Date())); //CCYYMM
            ftFlex.set(18, "6010");
            ftFlex.set(22, "901");
            ftFlex.set(25, "00");
            ftFlex.set(26, "12");
            ftFlex.set(28, "C00000000"); // was in sample
            ftFlex.set(32, "627348");
            ftFlex.set(33, "627348");
            ftFlex.set(35, "6273489999999999=200750100000");
            ftFlex.set(37,refno);
           // ftFlex.set(37, new java.text.SimpleDateFormat("MMddhhmmS").format(new java.util.Date()));
            ftFlex.set(41, "22838680");
            ftFlex.set(42, "627348000000006");
            ftFlex.set(43, "Carltech Printing Services-Kenyatta HiKE");
            ftFlex.set(49, "404");
            ftFlex.set(60, "27");
            ftFlex.set(102, debit_acc);
            ftFlex.set(103, credit_acc);
            return ftFlex;

        } catch (Exception e) {
            return null;
        }
    }


    public ISOMsg networkMessageToFlex(NETWORKMESSAGETYPE networkmessagetype) {
        ISOMsg isomsg = new ISOMsg();
        try {
            isomsg.setMTI("0800");
            isomsg.set(7, new java.text.SimpleDateFormat("MMddhhmmss").format(new java.util.Date())); //MMDDhhmmss
            isomsg.set(11, ISOUtil.padleft(generalFuncs.getNextStanNumber(), 6, '0'));
            isomsg.set(70, ISOUtil.padleft(String.valueOf(networkmessagetype.getMessageCode()), 3, '0'));
        } catch (ISOException e) {
            e.printStackTrace();
        }//301 / 001 / 002

        System.out.println("the msg " + isomsg.toString());
        return isomsg;
    }

    //Flex Network  echo
}
