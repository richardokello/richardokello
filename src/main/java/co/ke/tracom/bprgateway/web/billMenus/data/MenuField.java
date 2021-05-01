package co.ke.tracom.bprgateway.web.billMenus.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuField {
  /* Input prompt e.g Enter bill number */
  private String label;
  /* Type of input data */
  private String type;
  /* Maximum field length */
  private Integer length;
  /* Activate validation process if true */
  private boolean validated;
  /* Used to determine order of presentation */
  private Integer order;
}
