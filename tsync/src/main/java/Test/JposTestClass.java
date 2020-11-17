package Test;

import org.jpos.iso.ISODate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import ke.tra.com.tsync.packager.TracomPackager;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.NACChannel;
import org.jpos.util.SimpleLogListener;

public class JposTestClass {

    public static void main(String[] args) {


        try {
            ISOPackager packager = new TracomPackager();
            ISOMsg isoMsg;

            //isoMsg = sendMoney();
            //isoMsg = receiveMoneyCLS();
            //isoMsg = sendBillerInquiry();
            isoMsg = postMessegaToBiller();

            // msg = depotoCustomer();Error sending request to ufs-tms
            byte[] TPDU = new byte[5];
            TPDU[0] = 60;
            TPDU[1] = 00;
            TPDU[2] = 01;
            TPDU[3] = 00;
            TPDU[4] = 00;

            //{60, 00, 01, 00, 00,00,01};
            //byte[] TPDU = {60, 00, 01, 80, 00};
            isoMsg.setPackager(packager);
            org.jpos.util.Logger jPosLogger = new org.jpos.util.Logger();
            jPosLogger.addListener(new SimpleLogListener(System.out));

            byte[] data = isoMsg.pack();
            System.out.println(ISOUtil.hexdump(data));
            String server;
            int port ;
            server = "127.0.0.1"; port = 8621;
            //QA server
            //server = "41.215.130.247"; port = 9066;
//            server = "192.168.1.121"; port = 8621;
            // dev server
//            server = "41.215.130.247";
//            port = 4123;
            //BaseChannel channel = new NCCChannel(ip, 4123, packager, TPDU);
            NACChannel channel = new NACChannel(server, port, packager, TPDU);
            //ISOChannel channel = new NCCChannel(ip, port, packager, TPDU);

            channel.connect();

            channel.send(isoMsg);
            System.out.println("+++++++++++++++++");

            ISOMsg incoming = channel.receive();

            // System.out.println("Count kwa receiver : " + n + " \n");
            channel.disconnect();
            byte[] data2 = incoming.pack();
            System.out.println(ISOUtil.hexdump(data2));
            //  logISOMsg(incoming, "Response");

        } catch (ISOException ex) {
            Logger.getLogger(JposTestClass.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(JposTestClass.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static ISOMsg sendBillerInquiry() throws ISOException{
        // EG GEPG
        ISOMsg m = new ISOMsg();
        m.setMTI("1100");
        m.set(3,"011111");
        m.set(4,"000000000000");
        m.set(7,"2011061407");
        m.set(41,"80000086");
        m.set(42,"000118000009600");
        m.set(63,"991080008364");
        System.out.println("Sending biller inquiry");
        return m;
    }

    public static ISOMsg postMessegaToBiller() throws ISOException{
        // sends an advice for biller after payment has been made

        ISOMsg m = new ISOMsg();
        m.setMTI("1100");
        m.set(3,"011112");
        m.set(4,"000000000000");
        m.set(7,"2011061407");
        m.set(37, "031101000002");
        m.set(41,"80000086");
        m.set(42,"000118000009600");
        m.set(63,"20201106162024845#karim abdallah#karimabdallah4@gmail.com#255655647355#Tanzania Police Force#140401#TZS#NOVEMBER INTERM 22ND FEE#2020-11-30T23:59:59#PART#449680#991080008364#PIP6372158461#680.00#429941");
        return m;
    }



    public static ISOMsg getPurchase() throws ISOException {
        Date today = new Date();
        String mti = "0700";
        String date_now = ISODate.formatDate(today, "YYYYMMddhhmm");
        System.out.println("Diete : " + date_now);
        String f_11 = "678697";
        String f_32 = "888880";
        String f_37 = "000000000643";
        ISOMsg isoMsg = new ISOMsg(mti);
        isoMsg.set(2, "1140431000");
        isoMsg.set(3, "510000");
        isoMsg.set(4, "000000047500");
        isoMsg.set(11, f_11);
        isoMsg.set(12, date_now);
        isoMsg.set(37, f_37);
        isoMsg.set(41, "00PPT001");
        isoMsg.set(42, "00000BOA_PPT001");
        return isoMsg;
    }

    public static ISOMsg sendMoney() throws ISOException {
        Date today = new Date();
        String mti = "1200";
        String date_now = ISODate.formatDate(today, "YYYYMMddhhmm");
        System.out.println("Diete : " + date_now);
        String f_11 = "061409";
        String f_32 = "888880";
        ISOMsg isoMsg = new ISOMsg(mti);
        isoMsg.set(3, "410000");
        isoMsg.set(4, "000000000700");
        isoMsg.set(11, f_11);
        isoMsg.set(12, "180614091056");
        isoMsg.set(41, "PO400001");
        isoMsg.set(42, "100000RW0010408");
        isoMsg.set(47, "2507Mv T1.030100745454545360652522637014");
        isoMsg.set(60, "0787386036#0727719125#1195080002014104#1");
        isoMsg.set(62, "SIMULATOR");
        return isoMsg;
    }

    public static ISOMsg depotoCustomerValidation() throws ISOException {
        Date today = new Date();
        String mti = "1200";
        String date_now = ISODate.formatDate(today, "YYYYMMddhhmm");
        String f_11 = "061409";
        String f_32 = "888880";
        ISOMsg isoMsg = new ISOMsg(mti);
        isoMsg.set(3, "440000");
        isoMsg.set(4, "000000000600");
        isoMsg.set(11, f_11);
        isoMsg.set(12, "190615064331");
        isoMsg.set(41, "PO400001");
        isoMsg.set(42, "100000RW0010408");
        isoMsg.set(47, "2507Mv T1.030100745454545360652522637014");
        isoMsg.set(60, "190615124435789");
        isoMsg.set(62, "SIMULATOR");
        return isoMsg;
    }

    public static ISOMsg depotoCustomer() throws ISOException {
        Date today = new Date();
        String mti = "1200";
        String date_now = ISODate.formatDate(today, "YYYYMMddhhmm");
        String f_11 = "161409";
        String f_32 = "888880";
        ISOMsg isoMsg = new ISOMsg(mti);
        isoMsg.set(3, "440000");
        isoMsg.set(4, "000000000800");
        isoMsg.set(11, f_11);
        isoMsg.set(12, "190615064331");
        isoMsg.set(25, "1510");
        isoMsg.set(41, "PO400001");
        isoMsg.set(42, "100000RW0010408");
        isoMsg.set(47, "2507Mv T1.030100745454545360652522637014");
        isoMsg.set(60, "190615124435789");
        isoMsg.set(62, "SIMULATOR");
        return isoMsg;
    }

    public static ISOMsg receiveMoneyCLS() throws ISOException {
        Date today = new Date();
        String mti = "0200";
        String date_now = ISODate.formatDate(today, "YYYYMMddhhmm");
        System.out.println("Diete : " + date_now);
        String f_11 = "061410";
        String f_32 = "888880";
        String f_37 = "000000000643";

        ISOMsg isoMsg = new ISOMsg(mti);

        isoMsg.set(2, "123456836147");
        isoMsg.set(3, "420000");
        isoMsg.set(4, "000000000700");
        isoMsg.set(11, f_11);
        isoMsg.set(12, "180614091056");

        isoMsg.set(41, "PO400001");
        isoMsg.set(42, "100000RW0010408");
        isoMsg.set(47, "2507Mv T1.030100745454545360652522637014");
        isoMsg.set(60, "0727719125#691631");
        isoMsg.set(62, "SIMULATOR");
        return isoMsg;
    }






}
