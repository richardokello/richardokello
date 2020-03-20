package ke.tra.com.tsync.services;

import ke.tra.com.tsync.entities.TmsDeviceHeartbeat;
import ke.tra.com.tsync.repository.TMSHeartBeatsRepo;
import ke.tra.com.tsync.services.template.PosIrisInt;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class POSIrisTxns implements PosIrisInt {

    @Autowired
    TMSHeartBeatsRepo tmsHeartBeatsRepo;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(POSIrisTxns.class);

    @Override
    public ISOMsg receiveHeartBeat(ISOMsg isoMsg) {
        //check if heartbeat String presemt

        try {
            String messages = isoMsg.getString(48);

            String[] ld = null;
            String[] lvl = null;
            String[] tmp = null;
            String[] nw = null;
            String[] obj = null;
            String[] os = null;
            String[] telliumManagerVersion = null;
            String count = null;
            String version = null;
            String serialnumber = null;

            String[] message = messages.split(",");
            ld = message[0].split(":"); //batteryPercentage
            lvl = message[1].split(":"); //CHARGING_STATUS
            tmp = message[2].split(":"); //DEVICE_TEMPERATURE
            nw = message[3].split(":"); //SIGNAL_STRENGTH
            obj = message[4].split(":"); //OBJ
            os = message[5].split(":"); //OS_VERSION
            telliumManagerVersion = message[6].split(":"); //TM_VERSION
            serialnumber = message[7].split(":")[1]; //SERIAL_NO
            count = message[8]; //??
            version = message[9]; //APPLICATION_VERSION

            TmsDeviceHeartbeat tmsDeviceHeartbeat = new TmsDeviceHeartbeat();
            tmsDeviceHeartbeat.setApplicationVersion(version);
            tmsDeviceHeartbeat.setBatteryPercentage(ld[1]);
            tmsDeviceHeartbeat.setChargingStatus(lvl[1]);
            tmsDeviceHeartbeat.setOsVersion(os[1]);
            tmsDeviceHeartbeat.setSerialNo(serialnumber);
            tmsDeviceHeartbeat.setSignalStrength(nw[1]);
            tmsDeviceHeartbeat.setTid(isoMsg.getString(41));
            tmsDeviceHeartbeat.setDeviceTemperature(Short.valueOf(tmp[1]));
            tmsDeviceHeartbeat.setObj(obj[1]);
            tmsDeviceHeartbeat.setTmVersion(telliumManagerVersion[1]);

            tmsHeartBeatsRepo.save(tmsDeviceHeartbeat);


        }catch (Exception e){
            e.printStackTrace();
        }

        isoMsg.set(39,"00");
        return  isoMsg;
    }
}
