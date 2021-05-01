package co.ke.tracom.bprgateway.web.bankbranches.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.StringJoiner;

@Entity
@Table(name = "BPR_BRANCHES")
public class BPRBranches {
  @Id
  @Column(name = "ID")
  private String id;

  @Column(name = "MNEMONIC")
  private String mnemonic;

  @Column(name = "SUBDIVISIONCODE")
  private String subDivisionCode;

  @Column(name = "COMPANYNAME")
  private String companyName;

  public String getId() {
    return id;
  }

  public BPRBranches setId(String id) {
    this.id = id;
    return this;
  }

  public String getMnemonic() {
    return mnemonic;
  }

  public BPRBranches setMnemonic(String mnemonic) {
    this.mnemonic = mnemonic;
    return this;
  }

  public String getSubDivisionCode() {
    return subDivisionCode;
  }

  public BPRBranches setSubDivisionCode(String subDivisionCode) {
    this.subDivisionCode = subDivisionCode;
    return this;
  }

  public String getCompanyName() {
    return companyName;
  }

  public BPRBranches setCompanyName(String companyName) {
    this.companyName = companyName;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", BPRBranches.class.getSimpleName() + "[", "]")
        .add("id='" + id + "'")
        .add("mnemonic='" + mnemonic + "'")
        .add("subDivisionCode='" + subDivisionCode + "'")
        .add("companyName='" + companyName + "'")
        .toString();
  }
}
