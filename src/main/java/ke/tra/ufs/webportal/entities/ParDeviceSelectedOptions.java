package ke.tra.ufs.webportal.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@Entity(name = "PAR_DEVICE_SELECTED_OPTIONS")
public class ParDeviceSelectedOptions {

    @Id
    @GenericGenerator(
            name = "PAR_DEVICE_SELECTED_OPTIONS_S",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_DEVICE_SELECTED_OPTIONS_S"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_DEVICE_SELECTED_OPTIONS_S")
    @Column(name = "ID")
    private BigDecimal id;

    @NotNull
    @Column(name = "DEVICE_ID")
    private BigDecimal deviceId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "DEVICE_ID", insertable = false, updatable = false)
    private TmsDevice tmsDevice;

    @NotNull
    @Column(name = "PAR_ID")
    private BigDecimal deviceOptionId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "PAR_ID", insertable = false, updatable = false)
    private ParDeviceOptions deviceOptions;

    @Size(max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;
}
