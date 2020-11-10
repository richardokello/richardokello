package ke.tra.com.tsync.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.FailedTransactions;
import entities.OnlineActivity;

import ke.tra.com.tsync.services.template.ISOMsgUtils;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class ISOMsgTOEntityImp implements ISOMsgUtils {

    private static final org.slf4j.Logger ilogger = LoggerFactory.getLogger(ISOMsgTOEntityImp.class);

    @Override
    public OnlineActivity convertIsoToPojo(ISOMsg messageRequest) {

        final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        final OnlineActivity onlineActivity = mapper.convertValue(isoToHashMap(messageRequest), OnlineActivity.class);
        ilogger.info("convertIsoToPojo", onlineActivity.toString());
        return onlineActivity;

    }

    @Override
    public FailedTransactions convertFailedIsoToPojo(ISOMsg messageRequest) {

        final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        final FailedTransactions failedTransactions = mapper.convertValue(isoToHashMap(messageRequest), FailedTransactions.class);
        ilogger.info("convertIsoToPojo", failedTransactions.toString());
        return failedTransactions;

    }

    public HashMap<String,String>isoToHashMap(ISOMsg messageRequest){

        Integer[] donotsaveFieldList = {52,53,55}; //load from config
        Supplier<Stream<Integer>> doNotSaveStreamSuppier= () -> Stream.of(donotsaveFieldList);
        HashMap<String, String> isomap = new HashMap<>();
        for (int i = 0; i <= messageRequest.getMaxField(); i++) {
            final int currentField = i;
            if (messageRequest.hasField(i)) {
                if( doNotSaveStreamSuppier.get().anyMatch( x -> x!=currentField)){
                   // System.out.println("true   :" + i);
                    String fieldname = "field" + StringUtils.leftPad(i + "", 3, "0");
                    isomap.put(fieldname, messageRequest.getString(i));
                }
            }
        }

        return isomap;
    }





}


