package ke.tra.com.tsync.services;

import ke.tra.com.tsync.services.template.FlexTransactions;
import ke.tra.com.tsync.utils.FlexChannelUtil;
import ke.tra.com.tsync.utils.FlexMessages;
import ke.tra.com.tsync.utils.GeneralFuncs;
import org.jpos.iso.*;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.util.SimpleLogListener;
import org.jpos.util.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

import java.util.Date;


@Service
public class ToCBSTxns implements FlexTransactions {

    // BaseChannel channel;
    private static final org.slf4j.Logger tocbslogger = LoggerFactory.getLogger(ToCBSTxns.class);

    @Value("${flex_ip}")
    private String flex_ip;

    @Value("${flex_ip_port}")
    private String flex_ip_port;

    @Autowired
    private GeneralFuncs generalFuncs;

    @Autowired
    private FlexMessages flexMessages;

    public  BaseChannel getChannelInstance(FlexChannelUtil flexChannelUtil) throws InterruptedException, IOException {
        //check if channel  is connected and try to reconnect if its not
        if(flexChannelUtil.getInstance().isConnected()){
            tocbslogger.info("TOCBS Already connected to flex at  {}", new Date().toString());
        }else{
            flexChannelUtil.getInstance().connect();
            tocbslogger.info("reconnected to flex at  {}", new Date().toString());
            flexChannelUtil.getInstance();
            Thread.sleep(200);
        }
        return flexChannelUtil.getInstance();
    }

    public ISOMsg sendToFlex(ISOMsg fromPOS) {
        tocbslogger.info(" sendToFlex  ip  {} port {} ", flex_ip, flex_ip_port);
        // tocbslogger.info("Testing database Link {}" , generalFuncs.getAgentBlance("0015088118001"));
        FlexChannelUtil flexChannelUtil = FlexChannelUtil.getInstance(flex_ip, flex_ip_port);
        ISO87APackager flexpackager = flexChannelUtil.getFlexpackager();

        ISOMsg toFlexISO = new ISOMsg(); //message to Flex
        try {
            BaseChannel instance =getChannelInstance(flexChannelUtil);
            if(instance.isConnected()) {
                Logger logger = new Logger();
                logger.addListener(new SimpleLogListener(System.out));
                flexpackager.setLogger(logger, instance.getRealm());// uncooment ikikztta
                instance.setPackager(flexpackager);
                //send to flex

                if(fromPOS.getMTI().equalsIgnoreCase("0800")){
                    toFlexISO = fromPOS;
                }else {// not an echo message to flex
                    if (fromPOS.hasField(3)) {
                        //move this to a new function
                        switch (fromPOS.getString(3)) {
                            case "310000":
                                toFlexISO = flexMessages.FlexBalanceMessage(toFlexISO);
                                break;
                            case "420000":
                                //String debit_acc, String credit_acc, String amount , String narration
                                toFlexISO = flexMessages.FlexFundTransfer(
                                        fromPOS.getString(102),
                                        fromPOS.getString(103),
                                        fromPOS.getString(4),
                                        fromPOS.getString(72)
                                );
                                break;

                            default:
                                break;
                        }
                    } else {
                        fromPOS.set(39, "96");
                    }
                }
                if(toFlexISO.hasField(47))
                    toFlexISO.unset(47);
                toFlexISO.setPackager(flexpackager);
                toFlexISO.setDirection(1);
                String iso = generalFuncs.logISOMsg(toFlexISO,toFlexISO.getDirection());
                tocbslogger.info("TO FLEX REQUEST RAW :  {}   " ,iso );
                //  tocbslogger.info("FLEX REQUEST hexdump : %\n {} ", ISOUtil.hexdump(data));
                instance.setTimeout(10000);
                instance.send(toFlexISO);
                Thread.sleep(200);
                ISOMsg incoming = instance.receive();

                if(incoming.hasField(39))
                    fromPOS.set(39,incoming.getString(39));

                if (toFlexISO.hasField(3)) {
                    //move this to a new function
                    switch (toFlexISO.getString(3)) {
                        case "310000":
                            //TODO check appropriate balance request default response code
                            fromPOS.set(39, "06");
                            //IF Agent has negative balance declibe txn ..so check if amont is a C
                            // get available balance
                            String de54 = incoming.getString(54);
                            String balance = de54.substring(8, 20);
                            if (de54.charAt(7) == 'C') {
                                fromPOS.set(39, "00");
                            }
                            tocbslogger.info("\nFlex Available Balance {} \n ", balance);
                            String isobal = ISOUtil.formatAmount(Integer.parseInt(balance), 12);
                            tocbslogger.info("\nFlex Available Balance ISO Amount {} \n ", isobal);
                            fromPOS.set(4, balance);
                            fromPOS.set(54, de54);
                            break;

                        case "420000":
                            //TODO check appropriate balance request default response code
                            //IF Agent has negative balance declibe txn ..so check if amont is a C
                            // get available balance
                           // String de54 = incoming.getString(54);
                            //String balance = de54.substring(8, 20);
                            //if (de54.charAt(7) == 'C') {
                              //  isomsg2.set(39, "00");
                            //}
                            //tocbslogger.info("\nFlex Available Balance {} \n ", balance);
                            //String isobal = ISOUtil.formatAmount(Integer.parseInt(balance), 12);
                            //tocbslogger.info("\nFlex Available Balance ISO Amount {} \n ", isobal);
                            //isomsg2.set(4, balance);
                            //isomsg2.set(54, de54);
                            break;

                        default:
                            break;
                    }
                } else {
                    tocbslogger.info("No field 3");
                }

                tocbslogger.info("FLEX RESPONSE RAW : " + generalFuncs.logISOMsg(incoming, incoming.getDirection()));
                generalFuncs.logISOMsg(incoming, incoming.getDirection());

                if (incoming.getString(39).equalsIgnoreCase("00")) {
                    fromPOS.set(39, "00");
                } else {
                    fromPOS.set(39, incoming.getString(39));
                }
                // if channel connected send message else respond with remote system unavailab;e
            }else{
                fromPOS.set(39, "96");
                tocbslogger.info("unable to connect tpo flex  for {}  ",toFlexISO);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fromPOS.set(39, "96");
            tocbslogger.error("IO Exception connection Errod Flex  %n  {} %n  {} %n {} ", e,toFlexISO);
            tocbslogger.info("IO Exception connection Errod Flex  %n  {} %n  {} %n {} ", e,toFlexISO);
            //System malfunction, System malfunction or certain field error conditions
            //e.printStackTrace();
        } catch (ISOException e) {
            fromPOS.set(39, "96"); //could not send to flex
           tocbslogger.error("~~~~ ISO Exception  Flex : %n  {} %n  {}   ~~~ End of IsoException",  e,toFlexISO);
            tocbslogger.info("~~~~ ISO Exception  Flex : %n  {} %n  {}   ~~~ End of IsoException",  e,toFlexISO);

            //e.printStackTrace();
        }catch (InterruptedException e){
            fromPOS.set(39, "96"); //could not send to flex
            tocbslogger.info("ISO Exception connection  Flex : %n  {} %n  {} %n {}",  e,toFlexISO);
            tocbslogger.error("ISO Exception connection  Flex : %n  {} %n  {} %n {}",  e,toFlexISO);
        } catch (Exception ee){
            fromPOS.set(39, "96"); //could not send to flex
            tocbslogger.info(" Exception connection  Flex : %n  {} %n  {} %n {}",  ee,toFlexISO);
            tocbslogger.error(" Exception connection  Flex : %n  {} %n  {} %n {}",  ee,toFlexISO);
        }
        finally {
            String de39final = fromPOS.getString(39);
            tocbslogger.info("Final response DE 39: {}", de39final );
        }
        tocbslogger.info("~~~~~~ End  ~~~~~~");
        return fromPOS;
    }

    public void saveCashCollectionReconData(ISOMsg isoMsg, String string) {
        generalFuncs.saveCashCollectionReconData(isoMsg,string);
    }
}
