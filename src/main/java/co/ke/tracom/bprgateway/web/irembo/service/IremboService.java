package co.ke.tracom.bprgateway.web.irembo.service;

import co.ke.tracom.bprgateway.web.irembo.dto.request.BillNumberValidationRequest;
import co.ke.tracom.bprgateway.web.irembo.dto.response.IremboBillNoValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@RequiredArgsConstructor
@Service
public class IremboService {

    public IremboBillNoValidationResponse validateIremboBillNo(BillNumberValidationRequest request, String transactionRefNo) {
        HttpURLConnection httpConnection = null;
//        try {
//
//            IREMBOGATEWAYVALIDATEURL = SwitchFN.getParam_By_Name(db, "IREMBOGATEWAYVALIDATEURL");
//            IREMBOPIVOTACCESSID = SwitchFN.getParam_By_Name(db, "IREMBOPIVOTACCESSID");
//            IREMBOPIVOTSECRETKEY = SwitchFN.getParam_By_Name(db, "IREMBOPIVOTSECRETKEY");
//
//            isomsg.set(39, "908");
//            // DE 60
//            String de60 = isomsg.getString(60);
//            String customerBillNo = de60.split("#")[0].trim();
//            IremboRequest iremboRequest = new IremboRequest();
//            iremboRequest.setBillNumber(customerBillNo);
//
//            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
//            String content_type = "application/json";
//            String content_MD5 = "";
//            String uri = VALIDATEURI;
//
//            Date date2 = new Date();
//            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//            String gmtdate = sdf.format(date2);
//            String gmtdatefinal = gmtdate.substring(0, 3).toUpperCase() + gmtdate.substring(3, gmtdate.length());
//            String canonical_str = content_type + "," + content_MD5 + "," + uri + "," + gmtdatefinal;
//            String hmac = calculateRFC2104HMACBase64(canonical_str, IREMBOPIVOTSECRETKEY);
//            URL targetUrl = new URL(IREMBOGATEWAYVALIDATEURL);
//
//            httpConnection = (HttpURLConnection) targetUrl.openConnection();
//            httpConnection.setConnectTimeout(5000); // set timeout to 10 seconds
//            httpConnection.setDoOutput(true);
//            httpConnection.setDoInput(true);
//            httpConnection.setRequestMethod("POST");
//            httpConnection.setRequestProperty("Content-Type", "application/json");
//            httpConnection.setRequestProperty("Authorization", "APIAuth " + IREMBOPIVOTACCESSID + ":" + hmac);
//            httpConnection.setRequestProperty("Date", gmtdatefinal);
//            httpConnection.setUseCaches(false);
//            httpConnection.setReadTimeout(20000);
//
//            OutputStream outputStream = httpConnection.getOutputStream();
//            outputStream.write(new Gson().toJson(iremboRequest).getBytes());
//            outputStream.flush();
//
//            System.out.println("Irembo Validate Billnumber " + iremboRequest.getBillNumber() + " HTTP ResponseCode code : "
//                    + httpConnection.getResponseCode());
//            BufferedReader responseBuffer = new BufferedReader(
//                    new InputStreamReader((httpConnection.getInputStream())));
//
//            String inputLine;
//            StringBuilder stringBuilder = new StringBuilder();
//
//            while ((inputLine = responseBuffer.readLine()) != null) {
//                stringBuilder.append(inputLine);
//            }
//
//            IremboValidateRes ires = new Gson().fromJson(stringBuilder.toString(), IremboValidateRes.class);
//            if (ires.getResultCode().equals("200")) {
//
//                isomsg.set(39, "000");
//                String dname = (ires.getBillDetails().getCustomerName() == null
//                        || ires.getBillDetails().getCustomerName() == "") ? ires.getBillDetails().getRraAccountName()
//                        : ires.getBillDetails().getCustomerName();
//
//                String mobile = (ires.getBillDetails().getMobile() == null
//                        || ires.getBillDetails().getMobile().equals("")) ? "NA" : ires.getBillDetails().getMobile();
//
//                String f60res = ires.getBillDetails().getBillNumber() + "#" + ires.getMessage() + "#"
//                        + ires.getBillDetails().getBillerCode() + "#" + ires.getBillDetails().getDescription() + "#"
//                        + ires.getBillDetails().getAmount() + "#" + ires.getBillDetails().getCurrencyCode() + "#"
//                        + ires.getBillDetails().getRraAccountNumber() + "#" + ires.getBillDetails().getExpiryDate()
//                        + "#" + ires.getBillDetails().getRraAccountName() + "#" + dname + "#"
//                        + ires.getBillDetails().getCreatedAt() + "#" + ires.getBillDetails().getTransactionType() + "#"
//                        + ires.getResultCode() + "#" + mobile;
//
//                System.out.println("Field60out : " + f60res);
//                isomsg.set(60, f60res);
//
//                double d = Double.parseDouble(ires.getBillDetails().getAmount());
//                String isoamountm = String.format("%012d", (int) d);
//                isomsg.set(4, isoamountm);
//                isomsg.set(39, "000");
//
//            } else {
//                isomsg.set(60, ires.getMessage());
//            }
//
//            // Save to database
//            System.out.println("Irembo Validate Response Str : \n " + stringBuilder.toString());
//            SwitchFN.saveCardLessTransactionToAllTransactionTable(new T24TXNQueue(), isomsg, "IREMBO VALIDATION");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException | SignatureException | NoSuchAlgorithmException | InvalidKeyException e) {
//            e.printStackTrace();
//        } finally {
//            httpConnection.disconnect();
//        }
//        return isomsg;

        return null;
    }
}
