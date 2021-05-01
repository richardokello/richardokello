//package co.ke.tracom.bprgateway;
//
//import co.ke.tracom.bprgateway.core.tracomchannels.tcp.TCPClient;
//import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
//import org.junit.Test;
//
//public class T24Simulator {
//
//  @Test
//  public void simulate_multiple() throws InterruptedException {
//    TCPClient tcpClient = new TCPClient();
//    //  tcpClient.establishConnection(
//    // "01030000AENQUIRY.SELECT,,TRUSER1/123456/RW0010408,BPR.EUCL.GET.DATA,METER.NO:EQ=07061657156,TXN.AMT:EQ=3520");
//    //  tcpClient.establishConnection(
//    // "01030000AENQUIRY.SELECT,,TRUSER1/123456/RW0010425,BPR.EUCL.GET.DATA,METER.NO:EQ=07061657156,TXN.AMT:EQ=3520");
//    tcpClient.establishConnection(
//        "01030000AENQUIRY.SELECT,,TRUSER1/123456/RW0010425,BPR.EUCL.GET.DATA,METER.NO:EQ=07061657156,TXN.AMT:EQ=3520");
//    // tcpClient.establishConnection(
//    // "00990000AENQUIRY.SELECT,,########U/########A/RW0010425,BPR.EUCL.GET.DATA,METER.NO:EQ=07061657156,TXN.AMT:EQ=3520");
//    // tcpClient.establishConnection(
//    // "00990000AENQUIRY.SELECT,,INPUTT/123123/RW0010404,BPR.MIB.AC.BAL,ACCOUNT:EQ=404219792810387,TXN.REF:EQ=T20080823514888515,CHARGE:EQ=N,CHANNEL::=Mobile");
//    //    tcpClient.establishConnection(
//    // "00990000AENQUIRY.SELECT,,TRUSER1/123456,ECL.ENQUIRY.DETS,ID:EQ=593412948060277,TRANS.TYPE.ID:EQ=CUSTDET");
//    // tcpClient.establishConnection(
//    // "00990000AENQUIRY.SELECT,,TRUSER1/123456,ECL.ENQUIRY.DETS,ID:EQ=504416645410141,TRANS.TYPE.ID:EQ=CUSTDET");
//    //    tcpClient.establishConnection(
//    // "00990000AENQUIRY.SELECT,,TRUSER1/123456,ECL.ENQUIRY.DETS,ID:EQ=518423163110115,TRANS.TYPE.ID:EQ=CUSTDET");
//    //    tcpClient.establishConnection(
//    // "00990000AENQUIRY.SELECT,,TRUSER1/123456,ECL.ENQUIRY.DETS,ID:EQ=543460993410161,TRANS.TYPE.ID:EQ=CUSTDET");
//
//    Thread.sleep(15000);
//  }
//
//  @Test
//  public void check_length() throws Exception {
//    String savedOFSMsg =
//        "01080000AENQUIRY.SELECT,,########U/########A/RW0010425,BPR.EUCL.GET.DATA,METER.NO:EQ=07061657156,TXN.AMT:EQ=3520";
//
//    String[] myStrings = savedOFSMsg.split(",");
//
//    String stringtoMask = myStrings[2];
//
//    String[] userpass = stringtoMask.split("/");
//
//    String unmaskedcommand = savedOFSMsg.replaceAll(userpass[0], "TRUSER1");
//    unmaskedcommand = unmaskedcommand.replaceAll(userpass[1], "123456");
//
//    System.out.printf("\n\n\n OFS to CBS (MASKED)  ~~  %s \n", savedOFSMsg);
//    String minus4 = unmaskedcommand.substring(4);
//    String tot24str = String.format("%04d", minus4.length()) + minus4;
//    System.out.println("tot24str = " + tot24str);
//
//    TCPClient tcpClient = new TCPClient();
//    tcpClient.establishConnection(tot24str);
//
////    Assert.assertSame(108, tot24str.length());
//
//    Thread.sleep(15000);
//  }
//
//  @Test
//  public void decrypt_t24_credentials() {
//    String t24user = "CxnQ4y6Vka6IACAK3R7M2g==";
//    String t24pass = "4qNmGRfp2kjOMcmlwkljjw==";
//
//    try {
//      StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//      encryptor.setPassword("BPRAGENCY098");
//      encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
//      System.out.println("t24user = " + encryptor.decrypt(t24user));
//      System.out.println("t24pass = " + encryptor.decrypt(t24pass));
//
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//}
