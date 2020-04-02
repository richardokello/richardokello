package ke.tra.com.tsync.services.crdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ke.tra.com.tsync.h2pkgs.models.GeneralSettingsCache;
import ke.tra.com.tsync.h2pkgs.repo.GatewaySettingsCacheRepo;
import ke.tra.com.tsync.wrappers.crdb.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CRDBPipService {

    @Value("${get_session_pip}")
    private String get_session_pip_ip;

    @Value("${get_gepg_pip}")
    private String get_gepg_pip_url;

    @Value("${get_session_pip_partner_name}")
    private String get_session_pip_partnername;

    @Value("${get_session_pip_partner_password}")
    private String get_session_pip_partner_password;

    @Autowired
    private GatewaySettingsCacheRepo gatewaySettingsCacheRepo;

    // @Autowired
    //private LoggingInterceptor loggingInterceptor;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CRDBPipService.class);

    // validate Control Number
    private String getControlNumberFromLocalDB() {
        try {
            GeneralSettingsCache generalSettingsCache = gatewaySettingsCacheRepo.findById(1l).get();
            return generalSettingsCache.getCrdbSessionKey();
        } catch (Exception e) {
            LOGGER.error("LOGGER", e);
            e.printStackTrace();
        }
        return "";
    }

    private synchronized String refreshSessionNo(){
        String sessionStr="";
        try {
            sessionStr = getSessionDetails();
            GeneralSettingsCache generalSettingsCache = gatewaySettingsCacheRepo.findById(1l).get();
            generalSettingsCache.setCrdbSessionKey(sessionStr);
            gatewaySettingsCacheRepo.save(generalSettingsCache);
        }catch (Exception e){
            LOGGER.error("refreshSessionNo {}", e);
        }
        return sessionStr;
    }

    public RestTemplate restTemplate() {
        RestTemplate myrestTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_OCTET_STREAM,
                MediaType.TEXT_HTML,
                MediaType.APPLICATION_JSON_UTF8, MediaType.ALL));
        myrestTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        // myrestTemplate.setInterceptors(Collections.singletonList(loggingInterceptor));

        return myrestTemplate;
    }

    public String getSessionDetails() {
        String result = "";
        try {
            // restTemplate.setMessageConverters(getMessageConverters());

            RestTemplate myrestTemplate = restTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));

            GetCRDBSessionWrp crdbSessionWrp = new GetCRDBSessionWrp(
                    get_session_pip_partner_password, get_session_pip_partnername, "SESSION"
            );

            LOGGER.info("crdbSessionWrp Req {} ", crdbSessionWrp);
            HttpEntity<GetCRDBSessionWrp> entity = new HttpEntity<>(crdbSessionWrp, headers);
            ResponseEntity<CRDBWrapper> crdbWrapperResponseEntity = myrestTemplate.exchange(
                    get_session_pip_ip,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<CRDBWrapper>() {
                    });
            LOGGER.info("crdbWrapperResponseEntity Res1  {} ", crdbWrapperResponseEntity);
            //CRDBWrapper crdbWrapper = new Gson().fromJson(stringResponseEntity.getBody(), CRDBWrapper.class);
            LOGGER.info("crdbWrapperResponseEntity {} ", crdbWrapperResponseEntity);
            if (crdbWrapperResponseEntity.getStatusCode().is2xxSuccessful()) {

                SessionResponse sessionResponse = new ObjectMapper().convertValue(crdbWrapperResponseEntity.getBody().getData(), SessionResponse.class);
                // crdbWrapperResponseEntity.getBody().getData();
                //SessionResponse sessionResponse = (SessionResponse) (Arrays.asList(crdbWrapperResponseEntity.getBody().getData());
                result = sessionResponse.getSessionToken();
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("getSessionDetails", e);
            LOGGER.info("getSessionDetails {}", e);
            e.printStackTrace();
            return "";
        }
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> converters =
                new ArrayList<HttpMessageConverter<?>>();
        converters.add(new MappingJackson2HttpMessageConverter());
        return converters;
    }

    //CRDB HTTP REQUEST
    private ResponseEntity<CRDBWrapper> inquireGEPGControlNumber(String paymentreference) {
        String requestID = new SimpleDateFormat("yyyyMMddHHmmssS")
                .format(new Timestamp(System.currentTimeMillis()));
        String sessionToken = getControlNumberFromLocalDB();
       // String requestIDMD5 = DigestUtils.md5Hex(requestID);
       //String sha1Str = ;
        GepgControlNumberRequest gepgControlNumberRequest = new GepgControlNumberRequest(
                paymentreference, "INQUIRE", get_session_pip_partnername, sessionToken, DigestUtils.sha1Hex(DigestUtils.md5Hex(requestID) + paymentreference), requestID

        );

        LOGGER.info("GepgControlNumberRequest Body {}", gepgControlNumberRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        HttpEntity<GepgControlNumberRequest> entity = new HttpEntity<>(gepgControlNumberRequest, headers);
        LOGGER.info("GepgControlNumberRequest entity {} url : {} ", entity, get_gepg_pip_url);
        try {

            RestTemplate restTemplate = restTemplate();
            ResponseEntity<CRDBWrapper> response = restTemplate.exchange(
                    get_gepg_pip_url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<CRDBWrapper>() {
                    }
            );

            LOGGER.info("GepgControlNumberResponse response {}", response);
            return response;
        } catch (Exception ee) {
            LOGGER.info("\n\n GepgControlNumberRequest errer \n {} \n", ee);
            //e.printStackTrace();
            return null;
        }
    }

    //CRDB postGePGControlNumberPaymentRequestHttpEntity
    private ResponseEntity<CRDBWrapper> postGePGControlNumberPaymentRequestHttpEntity(String code
            , String sessionToken, String partnerID, String checksum, String requestID
            , String paymentReference, String callbackurl
            , String owner, String customerEmail, String customerMobile
            , String serviceName, String paymentGfsCode, String currency
            , String paymentDesc, String paymentExpiry, String paymentOption
            , String amount) {
        try {
            ResponseEntity<CRDBWrapper> crdbWrapperResponseEntity = null;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
            PostGePGControlNumberPaymentRequest postGePGControlNumberPaymentRequest =
                    new PostGePGControlNumberPaymentRequest()
                            .code(code)
                            .sessionToken(sessionToken)
                            .partnerID(partnerID)
                            .checksum(checksum)
                            .requestID(requestID)
                            .paymentReference(paymentReference)
                            .callbackurl("")
                            //  .paymentType("")
                            .owner(owner)
                            .customerEmail(customerEmail)
                            .customerMobile(customerMobile)
                            .serviceName(serviceName)
                            .paymentGfsCode(paymentGfsCode)
                            .currency(currency)
                            .paymentDesc(paymentDesc)
                            .paymentExpiry(paymentExpiry)
                            .paymentOption(paymentOption)
                            .amount(amount);

            HttpEntity<PostGePGControlNumberPaymentRequest> postGePGControlNumberPaymentRequestHttpEntity =
                    new HttpEntity<>(postGePGControlNumberPaymentRequest, headers);

            LOGGER.info("\n\n PostGePGControlNumberPaymentRequest   {} \n\n\n", postGePGControlNumberPaymentRequestHttpEntity);

            RestTemplate restTemplate = restTemplate();
            crdbWrapperResponseEntity = restTemplate
                    .exchange(
                            get_gepg_pip_url,
                            HttpMethod.POST,
                            postGePGControlNumberPaymentRequestHttpEntity,
                            new ParameterizedTypeReference<CRDBWrapper>() {
                            }
                    );
            LOGGER.info("\n\n PostGePGControlNumberPaymentRequest   {} \n\n\n", crdbWrapperResponseEntity);
            PostGepgControlNumberResponse postGepgControlNumberResponse =
                    new ObjectMapper().convertValue(
                            crdbWrapperResponseEntity.getBody().getData(),
                            PostGepgControlNumberResponse.class);

            return crdbWrapperResponseEntity;
        } catch (Exception ee) {
            LOGGER.info("\n\n PostGePGControlNumberPaymentRequest   {} \n\n\n", ee);
            return null;
        }
    }

    public ISOMsg inquireGEPGControlNumber(ISOMsg isoMsg) {
        try {
            if (!isoMsg.hasField(63) || isoMsg.getString(63).isEmpty())
                isoMsg.set(39, "06");
            ResponseEntity<CRDBWrapper> inquireGEPGControlNumber = inquireGEPGControlNumber(isoMsg.getString(63));
            if (null == inquireGEPGControlNumber) {
                isoMsg.set(39, "96");
            }
            if (inquireGEPGControlNumber.getStatusCode().is2xxSuccessful()) {
                //fetch reponse details
                CRDBWrapper crdbWrapper = inquireGEPGControlNumber.getBody();
                if (crdbWrapper.getStatus() == 200) {
                    isoMsg.set(39, "00");
                    GetControlNumberDetailsResponse getControlNumberDetailsResponse =
                            new ObjectMapper().convertValue(
                                    crdbWrapper.getData(),
                                    GetControlNumberDetailsResponse.class);

                    String de63res = getControlNumberDetailsResponse.getToPOSStr();
                    isoMsg.set(63, de63res);
                }else{
                    if((crdbWrapper.getStatus() == 200){

                    }
                }
            }
        } catch (Exception ee) {
            isoMsg.set(39, "96");
            LOGGER.info("\n\n inquireGEPGControlNumber error \n {} \n", ee);
        }
        return isoMsg;
    }

    /**
     * /**
     * 0 requestID
     * 1+ "#" +owner
     * 2+ "#" +customerEmail
     * 3+ "#" +customerMobile
     * 4+ "#" +serviceName
     * 5+ "#" +paymentGfsCode
     * 6+ "#" +currency
     * 7+ "#" +paymentDesc
     * 8+ "#" +paymentExpiry
     * 9+ "#" +paymentOption
     * 10+ "#" +amount
     * 11+ "#" +paymentReference
     * 12+ "#" +partnerID
     *
     * @param isoMsg
     * @return isoMsg
     */

    public ISOMsg postGEPGControlNumber(ISOMsg isoMsg) {
        if (!isoMsg.hasField(63) || isoMsg.getString(63).isEmpty())
            isoMsg.set(39, "06");
        String[] f63split = isoMsg.getString(63).split("#");
        if (f63split.length != 14) {
            isoMsg.set(39, "06");
            isoMsg.set(72, "Invalid Data Received for Processing");
        }
        String txnReference = isoMsg.getString(37);
        String code = "PURCHASE";
        String sessionToken = getControlNumberFromLocalDB();
        ;
        String partnerID = f63split[12];
        String requestID = f63split[0];
        String paymentReference = f63split[11];
        String amount = f63split[13];
        String inquiryAmount = f63split[10];
        String checksum =
                DigestUtils.sha1Hex(amount + DigestUtils.md5Hex(requestID) + paymentReference);
        //checksum --> amount + md5 (requestID) + paymentReference)
        String callbackurl = "";
        String owner = f63split[1];
        String customerEmail = f63split[2];
        String customerMobile = f63split[3];
        String serviceName = f63split[4];
        ;
        String paymentGfsCode = f63split[5];
        String currency = f63split[6];
        String paymentDesc = f63split[7];
        String paymentExpiry = f63split[8];
        String paymentOption = f63split[9];

        //fetch posting details
        ResponseEntity<CRDBWrapper> postGePGControlNumberPaymentRequestHttpEntity =
                postGePGControlNumberPaymentRequestHttpEntity(
                        code, sessionToken, partnerID, checksum,
                        requestID, paymentReference, callbackurl, owner,
                        customerEmail, customerMobile, serviceName, paymentGfsCode,
                        currency, paymentDesc, paymentExpiry, paymentOption, amount);
        isoMsg.set(39, "96");
        if (postGePGControlNumberPaymentRequestHttpEntity != null) {
            if (postGePGControlNumberPaymentRequestHttpEntity.getStatusCode().is2xxSuccessful()) {
                CRDBWrapper crdbWrapperResponse = postGePGControlNumberPaymentRequestHttpEntity.getBody();
                if (crdbWrapperResponse.getStatus() == 200) {
                    isoMsg.set(39, "00");
                    PostGepgControlNumberResponse gepgControlNumberResponse =
                            new ObjectMapper().convertValue(crdbWrapperResponse.getData(),
                                    PostGepgControlNumberResponse.class);
                    isoMsg.set(63, gepgControlNumberResponse.toPosString());
                } else {
                    isoMsg.set(39, "96");
                    isoMsg.set(72, crdbWrapperResponse.getStatusDesc());
                }
            }
        }
        return isoMsg;
    }
}
