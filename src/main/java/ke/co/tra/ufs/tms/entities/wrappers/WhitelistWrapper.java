/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.entities.wrappers;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Cornelius M
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WhitelistWrapper {

    @NotNull
    private BigDecimal modelIds;
    //    @NotNull
//    @Size(min = 1, max = 50)
    private String serialNo;
    //    @NotNull
    @Size(max = 200)
    private String note;
    //    @NotNull
    private MultipartFile file;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;
    private String productNo;
    private String location;
    private String deliveredBy;
    private String receivedBy;
    private Long ufsBankId;


}
