package ke.tra.ufs.webportal.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "UFS_POS_USER_ID_TRACKER")
@Data
public class UfsPosUserIdTracker implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(
            name = "UFS_POS_USER_ID_TRACKER_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_POS_USER_ID_TRACKER_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_POS_USER_ID_TRACKER_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "CURRENT_POS_USER_ID")
    private Long currentPosUserId;
    @Column(name = "IDS_REMAINING")
    private Long idsRemaining;
}
