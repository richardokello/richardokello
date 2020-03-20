package ke.tra.com.tsync.utils;

import ke.tra.com.tsync.h2pkgs.repo.GatewaySettingsCache;
import org.jpos.iso.BaseChannel;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.PostChannel;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.util.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class FlexChannelUtil {
    private static final org.slf4j.Logger FLEXllogger = LoggerFactory.getLogger(FlexChannelUtil.class.getName());
    public String getFlexIP() {
        return flexIP;
    }

    public void setFlexIP(String flexIP) {
        this.flexIP = flexIP;
    }

    public String getFlexIPPort() {
        return flexIPPort;
    }

    public void setFlexIPPort(String flexIPPort) {
        this.flexIPPort = flexIPPort;
    }

    private static  String flexIP;
    private static  String flexIPPort;
    private ISO87APackager flexpackager = new ISO87APackager();
    private static BaseChannel instance;
    private static volatile FlexChannelUtil flexChannelUtil;
    private  Logger logger = new Logger();

    @Autowired private FlexMessages flexMessages;
  //  @Autowired private GatewaySettingsCache gatewaySettingsCache;

    public static FlexChannelUtil getInstance(String _flexIP, String _flexIPPort) {
        flexIP =_flexIP;
        flexIPPort = _flexIPPort;
        //setFlexIPPort(flexIPPort);

        if(flexChannelUtil==null){
            FLEXllogger.info("initializing FlexChannelUtil");
            flexChannelUtil = new FlexChannelUtil();
        }else {
            FLEXllogger.info("flexChannelUtil already initialized  {}", flexIP, flexIPPort);
        }
        return flexChannelUtil;
    }

    public FlexChannelUtil(){
        try {
            FLEXllogger.info("initializing channel details: IP  {} PORT  {}", getFlexIP(), getFlexIPPort());

            setInstance(new PostChannel(getFlexIP(), Integer.valueOf(getFlexIPPort()), getFlexpackager()));
            getInstance().connect();
            if(getInstance().isConnected()){
                FLEXllogger.info("Connection test to flex UP ..   Flex ---  with : IP  {} PORT  {}", flexIP, flexIPPort);
            }
        } catch (Exception ez) {

            try {
                getInstance().disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FLEXllogger.error("FlexChannelUtilINIT",ez);
        }
    }

    public ISO87APackager getFlexpackager() {
        return flexpackager;
    }

    public void setFlexpackager(ISO87APackager flexpackager) {
        this.flexpackager = flexpackager;
    }

    public BaseChannel getInstance() {
        return instance;
    }

    public void setInstance(BaseChannel instance) {
        this.instance = instance;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
