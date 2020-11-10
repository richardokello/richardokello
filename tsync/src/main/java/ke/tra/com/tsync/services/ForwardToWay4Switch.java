package ke.tra.com.tsync.services;

import ke.tra.com.tsync.packager.TRCMpackagerv1;
import ke.tra.com.tsync.services.template.SendToSwitch;
import org.jpos.iso.*;
import org.jpos.iso.channel.PostChannel;
import org.jpos.iso.channel.XMLChannel;
import org.jpos.iso.packager.ISO87BPackager;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ForwardToWay4Switch implements SendToSwitch {

    @Value("${card_switch_ip}")
    private String switchIp;

    @Value("${card_switch_port}")
    private String switchPort;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ForwardToWay4Switch.class);

    @Override
    public ISOMsg sendtoSwitch(ISOMsg isoMsg) throws ISOException, IOException {
       // ISOMsg switchres;
        ISOPackager sbmpackager = new TRCMpackagerv1();
        isoMsg.setPackager(sbmpackager);
        System.out.println("port ip " + switchPort + " " + switchIp);
        ISOMsg isoMsg1 = new ISOMsg();

        /**
        try {
            isoMsg.setPackager(sbmpackager);
            byte[] data = isoMsg.pack();
            System.out.println("REQUEST TO SWITCH RAW : " + new String(data));
            System.out.println(ISOUtil.hexdump(data));
            BaseChannel channel = new PostChannel(switchIp, Integer.valueOf(switchPort), sbmpackager);
            //  BaseChannel channel = new XMLChannel(ip, port, packager);
            // XMLChannel
            channel.setTimeout(5);
            channel.connect();
            channel.send(isoMsg);
            ISOMsg incoming = channel.receive();
            byte[] data2 = incoming.pack();
            System.out.println("RESPONSE FROM SWITCH RAW : " + new String(data2));

            channel.disconnect();
        }catch (Exception ie) {

            ie.printStackTrace();
        } **/

        isoMsg.setResponseMTI();
        return isoMsg;
    }
}
