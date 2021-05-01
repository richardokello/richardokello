package co.ke.tracom.bprgateway.pos;

import co.ke.tracom.bprgateway.web.util.data.Field47Data;
import org.junit.Test;

public class ISOMsgProcessor {

  @Test
  public void checkField47Content() {
    String ISOMsgField47 = "2508Bpr P1.4301007886988093506398833370113824180222233221053607111028";

    int i = 0;
    String Tag = null;
    String TagLength = null;
    String TagValue = null;
    boolean more = true;
    int skip = 0;

    Field47Data field47Data = new Field47Data();

    while (more) {
      try {

        Tag = ISOMsgField47.substring(skip, skip + 2);
        TagLength = ISOMsgField47.substring(skip + 2, skip + 4);

        if (Integer.parseInt(TagLength) > 0) {
          TagValue = ISOMsgField47.substring(skip + 4, skip + 4 + Integer.parseInt(TagLength));
        } else TagValue = null;

        switch (Integer.parseInt(Tag)) {
          case 99:
            // HostServer = TagValue;
            break;
          case 1:
            // sourcechannel = TagValue;
            break;
          case 2:
            System.out.println("CardHolderName = " + TagValue);
            break;
          case 3:
            // ReceiptNumber = TagValue;
            break;
          case 4:
            System.out.println("BIN = " + TagValue);
            break;
          case 6:
            // Route = TagValue;
            break;
          case 7:
            // TripFrom = TagValue;
            break;
          case 8:
            // TripTo = TagValue;
            break;
          case 9:
            // Driver_Name = TagValue;
            break;
          case 10:
            // Driver_id = TagValue;
            break;
          case 11:
            // Conductor_Name = TagValue;
            break;
          case 12: // =====operator_ID=====
            System.out.println("userID = " + TagValue);
            field47Data.setUserID(TagValue);
            break;
          case 13:
            break;
          case 14: // Response Last Name
            break;
          case 15: // Response role
          case 16: // Response Offpeak fare
            System.out.println("password = " + TagValue);
            break;
          case 17: // Role
            System.out.println("ROLE_ = " + TagValue);
            break;
          case 18: // Response Userdefined fare
            break;
          case 19:
            System.out.println("sacconame = " + TagValue);
            break;
          case 24:
            System.out.println("Counter = " + TagValue);
            break;
          case 25:
            System.out.println("appVersion = " + TagValue);
            field47Data.setAppVersion(TagValue);
            break;
          case 29:
            System.out.println("usergender = " + TagValue);
            break;
          case 30:
            System.out.println("userName = " + TagValue);
            field47Data.setUserName(TagValue);
            break;
          case 31:
            System.out.println("fullName = " + TagValue);
            break;
          case 32:
            System.out.println("eMail = " + TagValue);
            break;
          case 33:
            System.out.println("phoneNo = " + TagValue);
            break;
          case 34:
            System.out.println("identification = " + TagValue);
            break;
          case 35:
            System.out.println("userpassword = " + TagValue);
            field47Data.setUserpassword(TagValue);
            break;
          case 36:
            System.out.println("terminalpin = " + TagValue);
            field47Data.setTerminalpin(TagValue);
            break;
          case 37:
            System.out.println("userworkgroup = " + TagValue);
            field47Data.setUserworkgroup(TagValue);
            break;
          case 38:
            System.out.println("terminalSerialNo = " + TagValue);
            field47Data.setTerminalSerialNo(TagValue);
            break;
          default:
            break;
        }
        i++;
        skip = skip + Integer.parseInt(TagLength) + 4;
        if (((skip) == ISOMsgField47.length()) || (i > 50)) {
          more = false;
        }
      } catch (Exception e) {
        break;
      }
    }
    String toString = field47Data.toString();

    System.out.println("toString = " + toString);
  }
}
