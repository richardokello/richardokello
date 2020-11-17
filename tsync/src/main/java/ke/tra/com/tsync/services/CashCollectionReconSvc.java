package ke.tra.com.tsync.services;

import entities.CashCollectionPreauth;
import entities.CashCollectionRecon;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CashCollectionPreauthRepo;
import repository.CashCollectionReconDataRepo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


@Service
public class CashCollectionReconSvc {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CashCollectionReconSvc.class);

    @Autowired
    private CashCollectionReconDataRepo cashCollectionReconDataRepo;

    @Autowired private
    CashCollectionPreauthRepo cashCollectionPreauthRepo;

    public ISOMsg getCashCollectionReconData(ISOMsg isoMsg) {
        try {


            CashCollectionRecon cashCollectionRecon =
                    cashCollectionReconDataRepo.findFirstByPaymentNarrationLike(isoMsg.getString(72).trim()).orElse(null);

            if (null != cashCollectionRecon) {
                isoMsg.set(72, cashCollectionRecon.getAgentUsername().concat("#")
                        .concat(cashCollectionRecon.getPosReference().concat("#"))
                        .concat(cashCollectionRecon.getCbsPostingReference().concat("#")
                                .concat(cashCollectionRecon.getPaymentAccount().concat("#"))
                                .concat(cashCollectionRecon.getPaymentMode().concat("#"))
                                .concat(cashCollectionRecon.getPaymentNarration().concat("#"))
                                .concat(cashCollectionRecon.getRevenueItemName().concat("#"))
                                .concat(cashCollectionRecon.getZoneName().concat("#"))
                                .concat(cashCollectionRecon.getPaymentDuration().concat("#"))
                                .concat(cashCollectionRecon.getTid().concat("#"))

                        ));
                isoMsg.set(39, "00");
            }

        } catch (Exception e) {
            throw e;
        }
        return isoMsg;
    }

    public ISOMsg saveCashCollection(ISOMsg isoMsg, HashMap<String, Object> fieldDataMap) {

        isoMsg.set(39, "00");
        String[] f72arr = isoMsg.getString(72).split("#");

        try {

            String zonename = f72arr[0];
            String revenueItemName = f72arr[1];
            String paymentmode = f72arr[2];
            String paymentAccount = f72arr[3];
            String RatePayercategory = f72arr[4];
            String duration = f72arr[5];
            String paymentNarration = f72arr[6];

            String amountStr = Integer.valueOf(isoMsg.getString(4)) > 0 ?
                    String.format("%.2f", Double.valueOf(isoMsg.getString(4)) / 100) : "";
            String randomNo = 1000000000 + (int) (new Random().nextDouble() * 999999999) + "";

            cashCollectionReconDataRepo.save(
                    new CashCollectionRecon()
                            .setCbsPostingStatus("1")
                            .setAgentUsername(fieldDataMap.get("userName").toString())
                            .setAmount(amountStr)
                            .setPosReference(isoMsg.getString(37))
                            .setCbsPostingReference(randomNo)//replace with cbs ref
                            .setCreditAccount(isoMsg.getString(103))
                            .setDebitAccount(isoMsg.getString(102))
                            .setPaymentMode(paymentmode)
                            .setRevenueItemName(revenueItemName)
                            .setZoneName(zonename)
                            .setPaymentAccount(paymentAccount)
                            .setRatePayerCategoryName(RatePayercategory)
                            .setPaymentNarration(paymentNarration)
                            .setOtherData(duration)
                            .setTid(isoMsg.hasField(41) ? isoMsg.getString(41) : "")
                            .setMid(isoMsg.hasField(42) ? isoMsg.getString(42) : "")
                            .setPaymentDuration(duration)
            );

        } catch (Exception e) {
            throw e;
        }
        return isoMsg;
    }

    public ISOMsg recordHourlyParkingStartDuration(ISOMsg isoMsg) {
            try {
                String[] f72Arr = isoMsg.getString(72).split("#");

                String revenueItem = f72Arr[0];
                String zone = f72Arr[1];
                String narration = f72Arr[2];
                String uniqueCode = f72Arr[3];
                String datetime = f72Arr[4];
                //28/02/2020 15:40
                //ORACLE TIMESTAMP
                Date date = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(datetime);
                Timestamp parkingDurationStartTime = new Timestamp(date.getTime());


                cashCollectionPreauthRepo.save(
                        new CashCollectionPreauth().setCashcollectcode(uniqueCode)
                        .setStarttime(parkingDurationStartTime)
                        .setNarration(narration)
                        .setZone(zone)
                        .setRevenueItem(revenueItem)
                );
                isoMsg.set(39,"00");

            }catch (Exception e){
                e.printStackTrace();
            }
        return isoMsg;
    }

    public ISOMsg fetchPaymentDurationbyUniqueCode(ISOMsg isoMsg) {
        try {
            String de72 = isoMsg.getString(72);

            LOGGER.info("Incoming DE72 {} ", de72);
            cashCollectionPreauthRepo.findDistinctFirstByCashcollectcode(de72).ifPresent(
                    (item)->{
                        Timestamp starttime = item.getStarttime();
                        Timestamp endtime = item.getEndtime();
                        long milliseconds = endtime.getTime() - endtime.getTime();
                        int seconds = (int) milliseconds / 1000;
                        int hours = Math.round(seconds/3600);
                        BigDecimal rate = item.getDurationRate();
                        LOGGER.info("\nseconds  {} hours {}  rate {}   hours * rate.intValue() {} \n", seconds ,  hours, rate ,hours * rate.intValue() );
                        isoMsg.set(72, String.valueOf(hours * rate.intValue()));
                    }
            );
            isoMsg.set(39,"00");
        }catch (Exception e){
            //e.printStackTrace();
            LOGGER.error("fetchPaymentDurationbyUniqueCode {}  " ,e);
        }
        return isoMsg;
    }



}
