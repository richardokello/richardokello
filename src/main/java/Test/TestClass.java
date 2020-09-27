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

public class TestClass {

    public static void main(String[] args) {

//        String s = "abbcdde";
//        Stream<Character> characters = s.chars();
        try {
            ISOPackager packager = new TracomPackager();
            ISOMsg isoMsg;
            isoMsg = posUserCreation();
            //isoMsg = logout();
            //isoMsg = deletePosUser();
           //isoMsg = posUserLogin();
            //isoMsg = firstTimePosUserLogin();
            //isoMsg = resetUserPin();
            //isoMsg = changeUserPin();
            //isoMsg = loadUsersForTerminal();
            //isoMsg = disableUsers();
            //isoMsg = sendMoney();
            //isoMsg = receiveMoneyCLS();
            //isoMsg = GEPGInquiry();
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
            //server = "127.0.0.1";
            server = "41.215.130.247";
            //BaseChannel channel = new NCCChannel(ip, port, packager, TPDU);
            NACChannel channel = new NACChannel(server, 4123, packager, TPDU);
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
            Logger.getLogger(TestClass.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(TestClass.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private static ISOMsg posUserLogin() throws ISOException {
        ISOMsg msg = new ISOMsg();

        msg.setMTI("1100"); // mti
        msg.set(3, "001000"); // processing code
        msg.set(11, "000010"); // System trace audit number (STAN) if any
        msg.set(41, "PO400001"); // Card acceptor terminal identification(TID)
        msg.set(42, "100000RW0010408"); // 	Card acceptor identification code(MID)
        //msg.set(47, "02500811111111  026 024 180222233221053607111323 029 001 1 030 008 CuulKid2 035 004 9999 036 0040 000");
        msg.set(47,"026024161697313221017301104142030005Tindi031004Muia0330100723143866034008324747740360040000037010Supervisor039003Joy0400040000");
        return msg;
    }
    private static ISOMsg posUserCreation() throws ISOException {
        ISOMsg msg = new ISOMsg();

        msg.setMTI("1100"); // mti
        msg.set(3, "000020"); // processing code
        msg.set(11, "000010"); // System trace audit number (STAN)
        msg.set(41, "PO400001"); // Card acceptor terminal identification
        msg.set(42, "100000RW0010408"); // 	Card acceptor identification code
        msg.set(47, "02500811111111026024161277313221017301065310030008CuulKid2031007collins032016colo12@gmail.com03301007122781910340082901000103500411230360040000037008Merchant0400049991039005troon");; // additional data
        //msg.set(47, "02602416169731322101730110414230003Dee031001m033010071483328403401012345678900360041111037010Supervisor039001e0400041111026024161697313221017301104142030003Dee031001m03301007148332840340101234567890036003000037010Supervisor039001e0400041111");
        System.out.println(msg);
        return msg;
    }

    private static ISOMsg firstTimePosUserLogin() throws ISOException {
        ISOMsg msg = new ISOMsg();

        msg.setMTI("1100"); // mti
        msg.set(3, "011114"); // processing code
        msg.set(11, "000010"); // System trace audit number (STAN)
        msg.set(41, "PO400001"); // Card acceptor terminal identification
        msg.set(42, "100000RW0010408"); // 	Card acceptor identification code
        msg.set(47, "025008111111110260241612773132210173010653100290011030006KeSeal03500499990360049843");

        System.out.println("++++++++++++++++sending  iso msg+++++++++++++++++++++++++");
        System.out.println(msg);
        return msg;
    }
    //

    private static ISOMsg changeUserPin() throws ISOException {
        ISOMsg msg = new ISOMsg();

        msg.setMTI("1100"); // Mti
        msg.set(3, "001111"); // processing Code
        msg.set(11, "01000"); // system trace audit number (STAN)
        msg.set(41, "PO400001"); // TID
        msg.set(42, "100000RW0010408"); //MID
        msg.set(47, "02500811111111026024161277313221017301065310030006KeSeal036004999904000499990410043247"); // data
        return msg;
    }

    private static ISOMsg resetUserPin() throws ISOException {
        ISOMsg msg = new ISOMsg();
        msg.setMTI("1100"); // Mti
        msg.set(3, "001110"); // processing Code for reset password
        msg.set(11, "01000"); // system trace audit number (STAN)
        msg.set(41, "PO400001"); // TID
        msg.set(42, "100000RW0010408"); //MID
        msg.set(47, "026024161277313221017301065310030006KeSeal"); // data

        return msg;
    }
    private static ISOMsg disableUsers() throws ISOException {
        ISOMsg msg = new ISOMsg();
        msg.setMTI("1100"); // Mti
        msg.set(3, "000120"); // processing Code for reset password
        msg.set(11, "01000"); // system trace audit number (STAN)
        msg.set(41, "PO400001"); // TID
        msg.set(42, "100000RW0010408"); //MID
        msg.set(47, "026024161277313221017301065310030018KeSeal,troon,nyule"); // data

        return msg;
    }


    private static ISOMsg deletePosUser() throws ISOException {
        ISOMsg msg = new ISOMsg();

        msg.setMTI("1100"); // mti
        msg.set(3, "011113"); // processing code
        msg.set(11, "000010"); // System trace audit number (STAN) if any
        msg.set(41, "PO400001"); // Card acceptor terminal identification(TID)
        msg.set(42, "100000RW0010408"); // 	Card acceptor identification code(MID)
        msg.set(47, "02500811111111026024161277313221017301065310030008CoolKids"); // additional data where we
        System.out.println(msg);
        return msg;
    }

    private static ISOMsg logout() throws ISOException {
        ISOMsg msg = new ISOMsg();

        msg.setMTI("1100"); // mti
        msg.set(3, "000021"); // processing code
        msg.set(11, "000010"); // System trace audit number (STAN) if any
        msg.set(41, "PO400001"); // Card acceptor terminal identification(TID)
        msg.set(42, "100000RW0010408"); // 	Card acceptor identification code(MID)
        msg.set(47, "026024161277313221017301065310030006KeSeal"); // additional data where we
        System.out.println(msg);
        return msg;
    }

    private static ISOMsg loadUsersForTerminal() throws ISOException {
        ISOMsg msg = new ISOMsg();

        msg.setMTI("1100"); // mti
        msg.set(3, "011115"); // processing code
        msg.set(11, "000010"); // System trace audit number (STAN) if any
        msg.set(41, "PO400001"); // Card acceptor terminal identification(TID)
        msg.set(42, "100000RW0010408"); // 	Card acceptor identification code(MID)
        msg.set(47, "026024161277313221017301065310"); // additional data where we
        System.out.println(msg);
        return msg;
    }

    private static ISOMsg GEPGInquiry() throws ISOException {
        ISOMsg msg = new ISOMsg();

        msg.setMTI("1100"); // mti
        msg.set(3, "011111"); // processing code
        msg.set(11, "000010"); // System trace audit number (STAN) if any
        msg.set(41, "PO400001"); // Card acceptor terminal identification(TID)
        msg.set(42, "000254376218600"); // 	Card acceptor identification code(MID)
        msg.set(47, "991530197446"); // additional data where we
        msg.set(62, "SIMULATOR");
        System.out.println(msg);
        return msg;
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
        isoMsg.set(12, "1806140");
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
