package ke.co.tra.ufs.tms.entities.wrappers;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TmsDeviceSimcardWrapper {

    private String simProvider;
    private String simPhoneNumber;
    private String simSerialNumber;




}
