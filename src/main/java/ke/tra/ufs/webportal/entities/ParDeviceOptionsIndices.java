package ke.tra.ufs.webportal.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity(name = "PAR_DEVICE_OPTIONS_INDICES")
public class ParDeviceOptionsIndices implements Comparable<ParDeviceOptionsIndices> {

    @Id
    @GenericGenerator(
            name = "PAR_DEVICE_OPTIONS_INDICES_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_DEVICE_OPTIONS_INDICES_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_DEVICE_OPTIONS_INDICES_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @NotNull
    @Column(name = "OPTION_ITEM")
    private BigDecimal optionId;

    @OneToOne
    @JoinColumn(name = "OPTION_ITEM", updatable = false, insertable = false)
    private ParDeviceOptions option;

    @NotNull
    @Column(name = "OPTION_INDEX")
    private Integer optionIndex;

    @Size(max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @Override
    public int compareTo(ParDeviceOptionsIndices parDeviceOptionsIndices) {
        return this.optionId.compareTo(parDeviceOptionsIndices.getOptionId());
    }
}
