package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Entity(name = "PAR_DEVICE_OPTIONS")
public class ParDeviceOptions {
//
    @Id
    @GenericGenerator(
            name = "PAR_DEVICE_OPTIONS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_DEVICE_OPTIONS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_DEVICE_OPTIONS_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @ModifiableField
    @NotNull
    @Column(name = "NAME")
    private String name;

    @ModifiableField
    @Column(name = "DESCRIPTION")
    private String description;

    @ModifiableField
    @Column(name = "IS_ALLOWED")
    private Short isAllowed;

    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;


    @Searchable
    @Filter
    @Column(name = "ACTION", insertable = false)
    private String action;

    @Searchable
    @Filter
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;

    @Size(max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @JsonIgnore
    @OneToMany(mappedBy = "deviceOptions",fetch = FetchType.LAZY)
    private List<ParDeviceSelectedOptions> selectedOptions;


    @OneToOne(mappedBy = "option")
    @JsonIgnore
    private ParDeviceOptionsIndices optionsIndex;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(Short isAllowed) {
        this.isAllowed = isAllowed;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public List<ParDeviceSelectedOptions> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<ParDeviceSelectedOptions> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public ParDeviceOptionsIndices getOptionsIndex() {
        return optionsIndex;
    }

    public void setOptionsIndex(ParDeviceOptionsIndices optionsIndex) {
        this.optionsIndex = optionsIndex;
    }
}
