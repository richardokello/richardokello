package co.ke.tracom.bprgateway.web.switchparameters.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "XSWITCH_PARAMETER")
public class XSwitchParameter {

  @Id
  @Column(name = "TABLE_INDEX")
  private long tableindex;

  @Column(name = "PARAM_NAME")
  private String paramName;

  @Column(name = "PARAM_VALUE")
  private String paramValue;

  @Column(name = "PARAM_MISC")
  private String paramMisc;
}
