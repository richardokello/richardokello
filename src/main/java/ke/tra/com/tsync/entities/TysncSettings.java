/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Tracom
 */
@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "XSWITCH_PARAMETER")
public class TysncSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "TABLE_INDEX")
    private String tableindex;

    @Column(name = "PARAM_NAME")
    private String paramname;

    @Column(name = "PARAM_VALUE")
    private String paramvalue;
    
    

}
