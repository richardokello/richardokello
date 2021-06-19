package co.ke.tracom.bprgateway.core.tracomchannels.tcp;

import co.ke.tracom.bprgateway.web.switchparameters.entities.XSwitchParameter;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class T24TCPClient {
    private final String T24_TCP_IP_PARAM_NAME = "T24_IP";
    private final String T24_TCP_PORT_PARAM_NAME = "T24_PORT";
    private final String T24_USERNAME_PARAM_NAME = "T24USER";
    private final String T24_PASSWORD_PARAM_NAME = "T24PASS";

    private final XSwitchParameterRepository switchParameterRepository;
    private final UtilityService utilityService;


    public String sendTransactionToT24(String T24OFSMessage)
            throws NoSuchFieldException, IOException {
        log.info("Masked t24 Request to CBS ~~ %s", T24OFSMessage);

        Optional<XSwitchParameter> optionalT24IP =
                switchParameterRepository.findByParamName(T24_TCP_IP_PARAM_NAME);
        Optional<XSwitchParameter> optionalT24Port =
                switchParameterRepository.findByParamName(T24_TCP_PORT_PARAM_NAME);

        if (optionalT24IP.isPresent() && optionalT24Port.isPresent()) {
            try {
                TelnetClient telnetClient = new TelnetClient();
                telnetClient.connect(
                        optionalT24IP.get().getParamValue(),
                        Integer.parseInt(optionalT24Port.get().getParamValue()));

                Optional<XSwitchParameter> optionalT24Pass =
                        switchParameterRepository.findByParamName(T24_PASSWORD_PARAM_NAME);

                Optional<XSwitchParameter> optionalT24Username =
                        switchParameterRepository.findByParamName(T24_USERNAME_PARAM_NAME);

                String t24usn = utilityService.decryptSensitiveData( optionalT24Username.get().getParamValue());
                String t24pwd = utilityService.decryptSensitiveData( optionalT24Pass.get().getParamValue());

                String[] messageSplit = T24OFSMessage.split(",");
                String plainTextMessage = messageSplit[2];
                String[] APIUserCredentials = plainTextMessage.split("/");

                String requestWithMaskedCredentials =
                        T24OFSMessage.replaceAll(APIUserCredentials[0], t24usn)
                                .replaceAll(APIUserCredentials[1], t24pwd);

                String minus4 = requestWithMaskedCredentials.substring(4);
                String T24MessageWithPlainTextCredentials = String.format("%04d", minus4.length()) + minus4;
                log.info("T24MessageWithPlainTextCredentials: "+T24MessageWithPlainTextCredentials);
                if (send(telnetClient, T24MessageWithPlainTextCredentials)) {
                    return receive(telnetClient);
                } else {
                    throw new IOException("Unable to send message to T24.");
                }

            } catch (IOException e) {
                throw new IOException(
                        "Gateway unable to initiate communication with T24 Gateway ["
                                + optionalT24IP.get().getParamValue()
                                + ":"
                                + Integer.parseInt(optionalT24Port.get().getParamValue() + "]"));
            }
        } else {
            throw new NoSuchFieldException("Missing T24 Configuration [IP/PORT] ");
        }
    }

    private static boolean send(TelnetClient client, String payload) {
        try {
            payload += "\r\n";
            client.getOutputStream().write(payload.getBytes());
            client.getOutputStream().flush();
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred: OFS Request" + payload);
            e.printStackTrace();
        }
        return false;
    }

    private static String receive(TelnetClient client) {
        StringBuffer strBuffer;
        try {

            strBuffer = new StringBuffer();
            byte[] buf = new byte[4096];
            int len = 0, datalen = -1;

            while ((len = client.getInputStream().read(buf)) != 0) {
                strBuffer.append(new String(buf, 0, len));
                // if length of received data is greater than four ... store the
                // data length if its not set yet
                Thread.sleep(20L);
                if (datalen == -1 && strBuffer.toString().length() > 4) {
                    datalen = Integer.parseInt(strBuffer.toString().substring(0, 4));
                }
                if (client.getInputStream().available() == 0) {
                    break;
                }
                if (strBuffer.length() == datalen) {
                    break;
                }
            }
            return strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
