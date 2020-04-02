package Test;

import org.jpos.iso.ISODate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import ke.tra.com.tsync.packager.TracomPackager;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.NCCChannel;
import org.jpos.util.SimpleLogListener;

public class TestClass {

    public static void yyy(String[] args) {

        try {
            ISOPackager packager = new TracomPackager();
            System.out.printf("~~~~~~~~~~~~~~~~~~~~~~");
            ISOMsg isoMsg;
            // msg = sendMoney();
            isoMsg = receiveMoneyCLS();
            // msg = depotoCustomer();
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
            //BaseChannel channel = new NCCChannel(ip, port, packager, TPDU);
            ISOChannel channel = new NCCChannel("localhost", 9052, packager, TPDU);
            //ISOChannel channel = new NCCChannel(ip, port, packager, TPDU);
            // System.out.println("Count kwa sender : " + n + " \n");
            channel.connect();

            channel.send(isoMsg);
          //  logISOMsg(isoMsg, "\n~~~~Request~~~");
            ISOMsg incoming = channel.receive();
            // System.out.println("Count kwa receiver : " + n + " \n");
            channel.disconnect();
            byte[] data2 = incoming.pack();
            System.out.println(ISOUtil.hexdump(data2));
          //  logISOMsg(incoming, "Response");

        } catch (ISOException ex) {
            Logger.getLogger(TestClass.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(TestClass.class.getName()).log(Level.SEVERE, null, ex);
        }

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
