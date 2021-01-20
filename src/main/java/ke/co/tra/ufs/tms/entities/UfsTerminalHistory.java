package ke.co.tra.ufs.tms.entities;

import ke.axle.chassis.annotations.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "UFS_TERMINAL_HISTORY")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UfsTerminalHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_TERMINAL_HISTORY_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_TERMINAL_HISTORY_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_TERMINAL_HISTORY_SEQ")
    @Column(name = "ID")
    private Long id;

    @Size(max = 50)
    @Column(name = "SERIAL_NUMBER")
    @Filter
    private String serialNumber;

    @Size(max = 20)
    @Column(name = "ACTION")
    @Filter
    private String action;

    @Size(max = 4000)
    @Column(name = "DESCRIPTION")
    @Filter
    private String description;

    @Size(max = 3)
    @Column(name = "INTRASH",insertable = false)
    @Filter
    private String intrash;

    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsUser user;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date creationDate;

    @Size(max = 20)
    @Column(name = "ACTION_STATUS")
    @Filter
    private String actionStatus;

    @Size(max = 50)
    @Column(name = "CREATED_BY")
    @Filter
    private String createdBy;

    public UfsTerminalHistory(String serialNumber,String action,String description,Long userId,String actionStatus,String createdBy){
        this.serialNumber = serialNumber;
        this.action = action;
        this.description = description;
        this.userId = userId;
        this.actionStatus = actionStatus;
        this.createdBy = createdBy;

    }


}
