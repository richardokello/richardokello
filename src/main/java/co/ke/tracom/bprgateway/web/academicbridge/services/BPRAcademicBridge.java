/*
package co.ke.tracom.bprgateway.web.academicbridge.services;


import com.academicbridge.ABPostPaymentPJ;
import com.academicbridge.AcademicBridgeValidationResWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.hibernate.Transaction;
import org.jpos.ee.DB;
import transactions.AcBridgePayments;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class BPRAcademicBridge {

	// TODO move these to a constants file
	// Get Url Constants
	String AC_BRIDGE_VALIDATEURL = SwitchFN.getParam("AC_BRIDGE_VALIDATEURL");
	String AC_BRIDGE_APIKEY = SwitchFN.getParam("AC_BRIDGE_APIKEY");
	String AC_BRIDGE_APISECRET = SwitchFN.getParam("AC_BRIDGE_APISECRET");

*
	 * String AC_BRIDGE_VALIDATEURL =
	 * "https://academicbridge.xyz/api/payments/online/students/info/"; String
	 * AC_BRIDGE_APIKEY = "zzN2uJwtAcCuw2Pn"; String AC_BRIDGE_APISECRET =
	 * "QRD3QpML52nGdZjA ";
	 *


	// TODO get from database
**
	 * String AC_BRIDGE_SAVEPAYMENT =
	 * "https://academicbridge.xyz/api/payments/online/students/save/"; String
	 * ACBRIDGEPOSTPAYMENTAPIKEY = "zzN2uJwtAcCuw2Pn"; String
	 * ACBRIDGEPOSTPAYMENTAPISECRET = "QRD3QpML52nGdZjA";
	 **


	
	String AC_BRIDGE_SAVEPAYMENT = SwitchFN.getParam("AC_BRIDGE_SAVEPAYMENT");
	String ACBRIDGEPOSTPAYMENTAPIKEY = SwitchFN.getParam("ACBRIDGEPOSTPAYMENTAPIKEY");
	String ACBRIDGEPOSTPAYMENTAPISECRET = SwitchFN.getParam("ACBRIDGEPOSTPAYMENTAPISECRET");

	String strURL = SwitchFN.getParam("ACCOUNT_TO_DEPOSIT_WSDL");
	String username = SwitchFN.getParam("ACCOUNT_TO_DEPOSIT_USERNAME");
	String password = SwitchFN.getParam("ACCOUNT_TO_DEPOSIT_PASSWORD");

*
	 * @param studentreg
	 * @return


	public AcademicBridgeValidationResWrapper getStudentInfoAB(String studentreg) {
		AcademicBridgeValidationResWrapper acValidateJson = new AcademicBridgeValidationResWrapper();
		int responseCode = 0;
		StringBuilder responseBuffer = new StringBuilder();
		// student reg not null empty or less than 10
		if (studentreg == null || studentreg.isEmpty() || studentreg.length() < 10) {
			System.out.println("Invalid Student registration received");
		} else {
			try {
				// keep it simple using stream readers
				String httpsURL = AC_BRIDGE_VALIDATEURL + studentreg + "?API_KEY=" + AC_BRIDGE_APIKEY + "&API_SECRET="
						+ AC_BRIDGE_APISECRET;
				URL myUrl = new URL(httpsURL);
				HttpsURLConnection conn = (HttpsURLConnection) myUrl.openConnection();
				InputStream is = conn.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String inputLine;
				responseCode = conn.getResponseCode();

				//	if (responseCode == 200) {

				while ((inputLine = br.readLine()) != null) {
					responseBuffer.append(inputLine);
				}
				br.close();
				
				JsonObject jsonObject = (JsonObject) new JsonParser().parse(responseBuffer.toString());
				Gson gson = new Gson();
				acValidateJson = gson.fromJson(jsonObject, AcademicBridgeValidationResWrapper.class);

					//	} else {
					// no connection or http failure
					//	}

			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return acValidateJson; // in outer method check for nulls first and
								// return appropraite response to the caller
	}

*
	 *
	 * @param regno
	 * @param refno
	 * @param amount
	 * @param sendername
	 * @param senderphone
	 * @return
	 * 


	public ABPostPaymentPJ postBPRPaymentstoAcademicBridge(String regno, String refno, String amount, String sendername,
			String senderphone) {
		AcBridgePayments abp = new AcBridgePayments();
		ABPostPaymentPJ abpost = new ABPostPaymentPJ();

		abp.setRefno(refno);
		abp.setRegno(regno);
		abp.setSendername(sendername);
		abp.setSenderphone(senderphone);

		// save request before saving and update after request

		String posturl = AC_BRIDGE_SAVEPAYMENT + regno + "?API_KEY=" + ACBRIDGEPOSTPAYMENTAPIKEY + "&API_SECRET="
				+ ACBRIDGEPOSTPAYMENTAPISECRET + "&reference_number=" + refno + "&paid_amount=" + amount
				+ "&sender_name=" + sendername + "&sender_phone_number=" + senderphone;

		int responseCode = 0;
		StringBuilder responseBuffer = new StringBuilder();

		// student reg not null empty or less than 10
		if (regno == null || regno.isEmpty() || regno.length() < 10) {
			System.out.println("Invalid Student registration received. Reg no less than 10 chars");
		} else {
			try {
				// keep it simple using stream readers

				URL myUrl = new URL(posturl);
				HttpsURLConnection conn = (HttpsURLConnection) myUrl.openConnection();
				InputStream is = conn.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);

				String inputLine;
				responseCode = conn.getResponseCode();
				abp.setHttprescode(responseCode);
				System.out.println("~~~~~~~~~~~~~~~~ : Http resp code :\n" + responseCode + "~~~~~~~~~~~~~~~");
				if (responseCode == 200) {

					while ((inputLine = br.readLine()) != null) {
						responseBuffer.append(inputLine);
					}

					System.out.println(
							"~~~~~~~~~~~~~~~~ : Api Response :\n" + responseBuffer.toString() + "~~~~~~~~~~~~~~~");
					br.close();

					JsonObject jsonObject = (JsonObject) new JsonParser().parse(responseBuffer.toString());
					Gson gson = new Gson();
					abpost = gson.fromJson(jsonObject, ABPostPaymentPJ.class);

					int posterror_code = abpost.getErrorCode();
					String posterror_msg = abpost.getErrorMsg();
					String pstatus = abpost.getSuccess().toString();

					abp.setAmount(amount);
					abp.setPaymentresponsecode(posterror_code + "");
					abp.setPaymentresponsedesc(posterror_msg);
					abp.setPaymentstatus(pstatus);

				} else {
					System.out.println("responseCode ");
				}
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				// e.getClass().getCanonicalName() ..get class name
				ex.printStackTrace();
			}
		}

		// record payment ..in db add timestamp as usual
		recordPayment(abp);

		System.out.println("~~~~~~~ : Record Saved to database  \n" + abp.toString() + " \n~~~~~~~~~");
		System.out.println("~~~~End PostAcademic Bridge Payments ~~~~~");

		return abpost;
	}

	private static void recordPayment(AcBridgePayments abp) {
		// TODO Auto-generated method stub
		DB db = new DB();
		db.open();
		try {
			Transaction txn = db.session().beginTransaction();
			db.session().save(abp);
			txn.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}

		db.close();
	}

}
*/
