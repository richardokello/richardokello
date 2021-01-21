/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.wrappers;

/**
 *
 * @author emuraya
 */
public class TaxGroupsWrapper {

    private String groupA;
    private String groupB;
    private String groupC;
    private String groupD;
    private String groupE;

    public TaxGroupsWrapper() {
    }

    public TaxGroupsWrapper(String groupA, String groupB, String groupC, String groupD, String groupE) {
        this.groupA = groupA;
        this.groupB = groupB;
        this.groupC = groupC;
        this.groupD = groupD;
        this.groupE = groupE;
    }

    public String getGroupA() {
        return groupA;
    }

    public void setGroupA(String groupA) {
        this.groupA = groupA;
    }

    public String getGroupB() {
        return groupB;
    }

    public void setGroupB(String groupB) {
        this.groupB = groupB;
    }

    public String getGroupC() {
        return groupC;
    }

    public void setGroupC(String groupC) {
        this.groupC = groupC;
    }

    public String getGroupD() {
        return groupD;
    }

    public void setGroupD(String groupD) {
        this.groupD = groupD;
    }

    public String getGroupE() {
        return groupE;
    }

    public void setGroupE(String groupE) {
        this.groupE = groupE;
    }

    
}
