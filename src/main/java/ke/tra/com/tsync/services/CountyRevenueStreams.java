package ke.tra.com.tsync.services;

import ke.tra.com.tsync.wrappers.DataWrapper;
import ke.tra.com.tsync.wrappers.TrcmGeneralRestWrapper;
import ke.tra.com.tsync.wrappers.countydevicesresponse.CountyDeviceResponseWrapper;
import ke.tra.com.tsync.wrappers.countyrevenues.CountyRevenueResWrap;
import ke.tra.com.tsync.wrappers.revenueItemsByDevice.Child;
import ke.tra.com.tsync.wrappers.revenueItemsByDevice.RevenueItemsByDeviceRes;
import ke.tra.com.tsync.wrappers.revenueStream.RevenueStreamItem;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CountyRevenueStreams {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserManagementService.class);

    @Value("${revenue_streams_url}")
    private String revenueStreamsUrl;

    @Value("${get_revenue_streams_by_id}")
    private String getRevenueByIDUrl;


    @Value("${countydevicebyid}")
    private String countyDeviceById;

    @Value("${revenue_streams_by_device}")
    private String revenueStreamsByDeviceSerialUrl;

    @Autowired
    private RestTemplate myRestTemplate;

    @Autowired
    private RevenueChargesSvc revenueChargesSvc;

    public ISOMsg countyDeviceResponseWrapperbyId(ISOMsg isoMsg, Integer id) {
        try {
            CountyDeviceResponseWrapper countyDeviceResponseWrapper = myRestTemplate.getForEntity(countyDeviceById, CountyDeviceResponseWrapper.class, id).getBody();
            isoMsg.set(
                    72, countyDeviceResponseWrapper.getData().getCountyIds().getName() + "#" + countyDeviceResponseWrapper.getData().getCountyIds().getCountyCode() + "#");
        } catch (Exception e) {
            isoMsg.set(39, "06");
            logger.error("countybydev", e);
        }
        return isoMsg;
    }

    public CountyRevenueResWrap getRevenueStream(String deviceid) {
        CountyRevenueResWrap revenueStreamsWrp = new CountyRevenueResWrap();
        //String url = revenueStreamsUrl+"/serialNo="+hierachyId;
        logger.info("RevenueStreamsUrl :  ", revenueStreamsUrl);
        try {
            ResponseEntity<CountyRevenueResWrap> revenueItemsDataWrpResponseEntity = myRestTemplate.getForEntity(revenueStreamsUrl, CountyRevenueResWrap.class, "serialNo", deviceid);
            logger.info("Revenue Streams httpresponsecode : {} ", revenueItemsDataWrpResponseEntity.getStatusCodeValue());
            logger.info("Revenue Streams Response Body : \n{} ", revenueItemsDataWrpResponseEntity.getBody().getData());//
            revenueStreamsWrp = revenueItemsDataWrpResponseEntity.getBody();
        } catch (Exception e) {
            logger.info("Revenue Streams Fetch Error-getRevenueStream", e);
            if (e instanceof ResourceAccessException) {
                revenueStreamsWrp.setCode(BigInteger.valueOf(503));
                revenueStreamsWrp.setMessage("REMOTE SYSTEM UNAVAILABLE-RI");
            } else {
                revenueStreamsWrp.setCode(BigInteger.valueOf(503));
                revenueStreamsWrp.setMessage("REMOTE SYSTEM ERROR-RI");
            }
        }
        return revenueStreamsWrp;
    }

    public HashMap<String, Object> getRevenueStreamByCode(String revenuecode) {
        HashMap<String, Object> object = new HashMap<>();
        logger.info("RevenueStreamsUrl :  ", getRevenueByIDUrl);

        String url = getRevenueByIDUrl + "/code/" + revenuecode;
        System.out.println("url " + url);
        try {
            ResponseEntity<TrcmGeneralRestWrapper> revenueItemsDataWrpResponseEntity = myRestTemplate
                    .getForEntity(
                            url,
                            TrcmGeneralRestWrapper.class
                            // "serialNo",
                            // revenuecode
                    );
            logger.info("getRevenueStreamByCode Streams httpresponsecode : {} ", revenueItemsDataWrpResponseEntity.getStatusCodeValue());
            logger.info("getRevenueStreamByCode Streams Response Body : \n{} ", revenueItemsDataWrpResponseEntity.getBody());//
            object = (HashMap) revenueItemsDataWrpResponseEntity.getBody().getObject();
            System.out.println("object " + object.get("id"));
            System.out.println("object " + object.get("name"));
            System.out.println("object " + object.get("text"));

            // HashMap map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        } catch (Exception e) {
            logger.info("getRevenueStreamByCode Streams Fetch Error-getRevenueStream", e);
            if (e instanceof ResourceAccessException) {
                // ChildWrapper.setCode(BigInteger.valueOf(503));
                // ChildWrapper.setMessage("REMOTE SYSTEM UNAVAILABLE-RI");
            } else {
                //  ChildWrapper.setCode(BigInteger.valueOf(503));
                //  ChildWrapper.setMessage("REMOTE SYSTEM ERROR-RI");
            }
        }
        return object;
    }

    public RevenueStreamItem getRevenueStreamItemByID(String itemId) {
        RevenueStreamItem revenueStreamsWrp = new RevenueStreamItem();
        String url = getRevenueByIDUrl + "/" + itemId;
        logger.info("RevenueStreamsUrl :  ", getRevenueByIDUrl);
        try {
            ResponseEntity<RevenueStreamItem> revenueItemsDataWrpResponseEntity = myRestTemplate.getForEntity(url, RevenueStreamItem.class);
            logger.info("Revenue Streams httpresponsecode : {} ", revenueItemsDataWrpResponseEntity.getStatusCodeValue());
            logger.info("Revenue Streams Response Body : \n{} ", revenueItemsDataWrpResponseEntity.getBody().getData());//
            revenueStreamsWrp = revenueItemsDataWrpResponseEntity.getBody();
        } catch (Exception e) {
            logger.info("Revenue Streams Fetch Error-getRevenueStreamItem", e);
            if (e instanceof ResourceAccessException) {
                revenueStreamsWrp.setCode("503");
                revenueStreamsWrp.setMessage("REMOTE SYSTEM UNAVAILABLE-RI");
            } else {
                revenueStreamsWrp.setCode("503");
                revenueStreamsWrp.setMessage("REMOTE SYSTEM ERROR-RI");
            }
        }
        return revenueStreamsWrp;
    }

    public RevenueItemsByDeviceRes revenueItemsByDeviceRes(String deviceSerialNo) {
        RevenueItemsByDeviceRes revenueItemsByDeviceRes = new RevenueItemsByDeviceRes();
        // http://192.168.1.175:8006/api/v1/county-devices/stream?serialNo=1519WL81344394
        String url = revenueStreamsByDeviceSerialUrl + "?serialNo=" + deviceSerialNo;
        logger.info("RevenueStreamsUrl : {}  ", url);
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("serialNo", deviceSerialNo);
            ResponseEntity<RevenueItemsByDeviceRes> revenueItemsByDeviceResResponseEntity = myRestTemplate.getForEntity(url, RevenueItemsByDeviceRes.class);
            logger.info("Revenue revenueItemsByDeviceResResponseEntity httpresponsecode : {} ", revenueItemsByDeviceResResponseEntity.getStatusCodeValue());
            logger.info("Revenue revenueItemsByDeviceResResponseEntity Response Body : \n{} ", revenueItemsByDeviceResResponseEntity.getBody().getData());//
            revenueItemsByDeviceRes = revenueItemsByDeviceResResponseEntity.getBody();
        } catch (Exception ex) {
            logger.info("Revenue Streams by DEVICE SERIAL ID  Fetch Error-GETSTREAM {}", ex);
            if (ex instanceof ResourceAccessException) {
                revenueItemsByDeviceRes.setCode(503);
                revenueItemsByDeviceRes.setMessage("REMOTE SYSTEM UNAVAILABLE-RI");
            } else {
                revenueItemsByDeviceRes.setCode(503);
                revenueItemsByDeviceRes.setMessage("REMOTE SYSTEM ERROR-RI");
            }
        }
        return revenueItemsByDeviceRes;
    }

    public CountyRevenueResWrap getRevenueStreams() {

        CountyRevenueResWrap revenueStreamsWrp = new CountyRevenueResWrap();
        try {
            ResponseEntity<CountyRevenueResWrap> revenueStreamsWrpResponseEntity = myRestTemplate.getForEntity(revenueStreamsUrl, CountyRevenueResWrap.class);
            logger.info("Revenue Streams httpresponsecode :  ", revenueStreamsWrpResponseEntity.getStatusCodeValue());
            logger.info("Revenue Streams Response Body :  ", revenueStreamsWrpResponseEntity.getBody().toString());//
            revenueStreamsWrp = revenueStreamsWrpResponseEntity.getBody();
        } catch (RestClientException e) {
            logger.info("Revenue Streams Fetch Error", e);
            if (e instanceof ResourceAccessException) {
                //assert revenueStreamsWrp != null;
                revenueStreamsWrp.setCode(BigInteger.valueOf(503));
                revenueStreamsWrp.setMessage("REMOTE SYSTEM UNAVAILABLE");
            } else {
                revenueStreamsWrp.setCode(BigInteger.valueOf(503));
                revenueStreamsWrp.setMessage("REMOTE SYSTEM ERROR");
            }
        } catch (Exception e) {
            revenueStreamsWrp.setCode(BigInteger.valueOf(503));
            revenueStreamsWrp.setMessage("REMOTE SYSTEM ERROR");
        }

        return revenueStreamsWrp;
    }

    public ISOMsg getRevenueStreamItems(ISOMsg isoMsg) {

        String itemID;
        if (isoMsg.hasField(72))
            itemID = isoMsg.getString(72);
        else {
            isoMsg.set(39, "96");
            return isoMsg;
        }

        RevenueStreamItem revenueStreamItem = getRevenueStreamItemByID(itemID);
        if (!revenueStreamItem.getCode().equalsIgnoreCase("200")) {
            isoMsg.set(39, "06");
            isoMsg.set(72, revenueStreamItem.getMessage());
            return isoMsg;
        }

        isoMsg.set(39, "00");
        final String[] f72Response = {""};
        final String[] children = {""};
        DataWrapper data = revenueStreamItem.getData();


        if (data.getHasChildren()) {
            data.getChildren().forEach((child) -> {
            /*
             id*action*actionstatus*name*isparent*untiqueid*parentID*TEXT*NICKNAME*CHILDREN # ... another child #... and so on
             */
                children[0] +=
                        child.getId() + "*"
                                + child.getAction() + "*"
                                + child.getActionStatus() + "*"
                                + child.getIntrash() + "*"
                                + child.getName() + "*"
                                + child.getIsParent() + "*"
                                + child.getUniqueId() + "*"
                                + child.getParentIds() + "*"
                                + child.getText() + "*"
                                + child.getNickname() + "*"
                                + child.getHasChildren() + "*"
                                + "#";
            });
        } else
            children[0] = " ";

        f72Response[0] = data.getId() + "|"
                + data.getAction() + "|"
                + data.getActionStatus() + "|"
                + data.getIntrash() + "|"
                + data.getName() + "|"
                + data.getIsParent() + "|"
                + data.getUniqueId() + "|"
                + data.getParentIds() + "|"
                + data.getText() + "|"
                + data.getNickname() + "|"
                + data.getHasChildren() + "|"
                + children[0] + "|";

        isoMsg.set(72, itemID + "|" + f72Response[0]);
        logger.info(f72Response[0]);


        // id|id|action|actionstatus|name|isparent|untiqueid|parentID|TEXT|NICKNAME|HASCHILDREN|CHILDREN |

        return isoMsg;
    }

    public ISOMsg getRevenueStreamByID(ISOMsg isoMsg) {
        try {
            String revenueid = isoMsg.getString(72);

            CountyRevenueResWrap revenueItemsDataWrp = getRevenueStream(revenueid);
            logger.info("revenueStreamsWrp", revenueItemsDataWrp.toString());

            if (revenueItemsDataWrp.getCode().toString().equalsIgnoreCase("503")) {
                isoMsg.set(39, "06");
                isoMsg.set(72, revenueItemsDataWrp.getMessage());
                return isoMsg;
            }

            final String[] f72Response = {""};
            revenueItemsDataWrp.getData().getContent().forEach(item -> {
                f72Response[0] +=
                        item.getId() + "|"
                                + item.getName() + "|"
                                + item.getUniqueId() + "|"
                                + item.getLevels() + "|"
                                + item.getAccIds().getAccNumber() + "|"
                                + item.getAccIds().getBranchIds().getCode() + "|"
                                + item.getAccIds().getBranchIds().getName() + "|"
                                + item.getAccIds().getBranchIds().getGeographicalRegionId().getCode() + "|"
                                + item.getAccIds().getBranchIds().getGeographicalRegionId().getRegionName() + "|#";
                logger.info(f72Response[0]);

                //REVENUE STREAM ID
                //REVENUE STREAM NAME
                //REVEMUE UNIQUE ID
                //REVEMUE LEVEL
                //REVENUE ACCOUNT NUMBER
                //REVENUE ACCOUNT BRANCHCODE
                //REVENUE ACCOUNT BRANCHNAME
                //GEOGRAPHICAL REGION CODE
                //GEOGRAPHICAL REGION NAME
            });
            //  f72Response[0] = f72Response[0] + "#";
            logger.info("FINAL f72response data items", f72Response[0]);
            isoMsg.set(72, f72Response[0]);
        } catch (Exception we) {
            isoMsg.unset(72);
            if (we instanceof ResourceAccessException) {
                isoMsg.set(72, "REMOTE SYSTEM COULD NOT BE REACHED. TRY LATER");
            }
            isoMsg.set(39, "06");

            logger.error("setRevenueStreams", we);
        }
        return isoMsg;
    }

    public String getChildData(int level, String symbol, List<Child> children) {
        String returnStr = "";
        for (Child child : children) {

            //id*name*haschildren*childrendata*>

            returnStr += child.getId() + symbol + child.getName() + symbol + child.getHasChildren() + symbol;

            if (child.getHasChildren()) {
                for (Child child1 : child.getChildren()) {
                    String symbol1 = "<";
                    returnStr += child1.getId() + symbol1 + child1.getName() + symbol1 + child1.getHasChildren() + symbol1;
                    if (child1.getHasChildren()) {
                        for (Child child2 : child1.getChildren()) {
                            String symbol2 = "@";
                            returnStr += child2.getId() + symbol2 + child2.getName() + symbol2 + child2.getHasChildren() + symbol2 + "^";


                            //id@name@haschildren@^

                        }
                        returnStr += symbol1;
                    } else {
                        returnStr += symbol1;
                    }
                    returnStr += "~";
                }
                // returnStr  +=symbol;
                returnStr += "*>";
            } else {
                // returnStr +=symbol;
                returnStr += "*>";
            }


        }
        // returnStr +=  symbol;
        return returnStr;
    }

    public ISOMsg RevenueItemsByDeviceRes(ISOMsg isoMsg, HashMap<String, Object> fieldDataMap) {
        try {
            String serialNumber = fieldDataMap.get("serialNumber").toString();
            RevenueItemsByDeviceRes revenueItemsByDeviceRes = revenueItemsByDeviceRes(serialNumber);
            if (!revenueItemsByDeviceRes.getCode().toString().equalsIgnoreCase("200")) {
               // isoMsg.set(39, "06");
                isoMsg.set(72, revenueItemsByDeviceRes.getMessage());
                return isoMsg;
            }
            isoMsg.set(39, "00");
            //        ID|NAME|UNIQUEID|TEXT|NICKNAME|INTRASH|HASCHILDREM|SIZEOFCHILDREN|CHILREDNDATA
            final String[] f72Response = {""};
            revenueItemsByDeviceRes.getData().getStreams().forEach(
                    item -> {
                        // if(item.getIntrash().equalsIgnoreCase("NO") && item.getActionStatus().equalsIgnoreCase("Approved")){
                        // SEND TO pos
                        String nickname = null == item.getNickname() ?
                                " " : item.getNickname();

                        f72Response[0] +=
                                item.getId().toString().concat("|")
                                        .concat(item.getName().concat("|"))
                                        .concat(item.getUniqueId().concat("|"))
                                        .concat(item.getText().concat("|"))
                                        .concat(nickname.concat("|"))
                                        .concat(item.getIntrash().concat("|"))
                                        .concat(item.getHasChildren() + "|")
                                        .concat(item.getChildren().size() + "|")
                        .concat("#");
                       // if (!item.getHasChildren()) {
                            //logger.info("levelo {}", );
                          //  f72Response[0] += " |";
                       // } else {
                        //    String levelo = getChildData(1, "*", item.getChildren());
                        //    logger.info("levelo {}", levelo);
                       //     f72Response[0] += levelo + "|";
                       // }
                    }
            );

            logger.info("FINAL f72response data items {}", f72Response[0]);
            //isoMsg.set(39, "00");
            isoMsg.set(72, f72Response[0]);

        } catch (Exception we) {
            isoMsg.unset(72);

            if (we instanceof ResourceAccessException) {
                isoMsg.set(72, "REMOTE SYSTEM COULD NOT BE REACHED. TRY LATER");
            }

            isoMsg.set(39, "06");
            logger.info("RevenueItemsByDeviceRes  {}", we);
            logger.error("RevenueItemsByDeviceRes  {}", we);

        }
        return isoMsg;
    }

    public ISOMsg setRevenueStreams(ISOMsg isoMsg) {
        try {
            CountyRevenueResWrap revenueStreamsWrp = getRevenueStreams();
            // final String[] f62response = {""};
            final String[] f72response = {""};

            if (revenueStreamsWrp.getCode().toString().equalsIgnoreCase("503")) {
                isoMsg.set(39, "06");
                isoMsg.set(72, revenueStreamsWrp.getMessage());
                return isoMsg;
            }

            //ACCOUNTNUMBER|REVENUESTREAMID|REVENUESTREAMNAME|COUNTYCODE|COUTYNAME|#
            revenueStreamsWrp.getData().getContent().forEach(item -> {
                f72response[0] += item.getAccIds().getAccNumber() + "|";
                f72response[0] += item.getId() + "|" + item.getName() + "|";
                f72response[0] += item.getCountyIds().getCountyCode() + "|" + item.getCountyIds().getName() + "#";
                logger.info(item.getAccIds().getAccNumber() + "|" + item.getId() + "|" + item.getName() + "|" + item.getCountyIds().getCountyCode() + "|" + item.getCountyIds().getName() + "#");
            });

            f72response[0] = f72response[0];
            logger.info("FINAL f72response", f72response[0]);
            //  logger.info("f72response", f72response[0]);
            //  isoMsg.set(62, f62response[0]);
            isoMsg.set(72, f72response[0]);

        } catch (Exception we) {
            logger.error("setRevenueStreams", we);
        }
        return isoMsg;
    }

    public ISOMsg getAmountByZoneAndStreamItemId(ISOMsg isoMsg) {
        String zoneItem;

        if (isoMsg.hasField(72)) {
            zoneItem = isoMsg.getString(72);
            logger.info("DE 72", zoneItem);
            String[] zoneItemSplit = zoneItem.split("\\|");
            if (zoneItemSplit[0].isEmpty() || zoneItemSplit[1].isEmpty()) {
                isoMsg.set(39, "96");
                logger.info("getAmountByZoneAndStreamItemId", isoMsg, "missing zone or stream id info on DE72");
                //  isoMsg.set(72, revenueItemsDataWrp.getMessage());
            } else {
                //Fetch the Amount
                //isoMsg.set(39, "00");
                isoMsg.set(4, revenueChargesSvc.fetchAmountByZoneStreamID(zoneItemSplit[0], zoneItemSplit[1]));
                isoMsg.set(39, "00");
            }
            return isoMsg;
        }
        return isoMsg;
    }


    public ISOMsg getStreamByUniqueID(ISOMsg isoMsg) {

        if (isoMsg.hasField(72)) {
            HashMap<String, Object> streamdetails = getRevenueStreamByCode(isoMsg.getString(72));
            // System.out.println("Stream details for code " + isoMsg.getString(72) + "\n"+
            //   streamdetails);
            if (streamdetails != null) {
                //ID|NAME|TEXT|HASCHILDREN

                isoMsg.set(72,
                        streamdetails.get("id") + "|"
                                + streamdetails.get("name") + "|"
                                + streamdetails.get("text") + "|"
                                + streamdetails.get("nickname") + "|"
                                + streamdetails.get("hasChildren") + "|"
                );
                isoMsg.set(39, "00");
            } else {
                isoMsg.set(96, "Remote System Could not Verify Code");
            }
        } else {
            isoMsg.set(96, "KINDLY INPUT THE ITEM CODE");
        }
        return isoMsg;
    }

    public ISOMsg getRevenueStreamAccountByID(ISOMsg isoMsg) {
        String streamId;
        //http://192.168.1.175:8006/api/v1/revenue_stream/payment-account/87
        try {
            if (isoMsg.hasField(72)) {
                streamId = isoMsg.getString(72).trim();
                String accUrl = revenueStreamsUrl + "/payment-account/"+streamId;
                System.out.println("revenueStreamsUrl " + accUrl);
                        TrcmGeneralRestWrapper object = myRestTemplate
                             .getForObject( accUrl, TrcmGeneralRestWrapper.class
                        );

                        if(object.getCode().equalsIgnoreCase("200")){
                            String accountno = (String) object.getObject();
                            isoMsg.set(72,accountno);
                            isoMsg.set(39, "00");
                        }
            }

        } catch (RestClientException e) {
            isoMsg.set(72,"Remote System Error");
            e.printStackTrace();

        } catch (Exception e) {
            isoMsg.set(72,"System Error");
            e.printStackTrace();
            isoMsg.set(39, "06");
        }
        return isoMsg;
    }

}
