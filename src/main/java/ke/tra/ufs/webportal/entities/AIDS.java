package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.Searchable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AIDS")
public class AIDS {
    private static final Long UUID=0l;
    @Column(name = "ID")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "UUID2")
    private Long Id;
    @Searchable
    @Filter
    @NotBlank
    @NotEmpty
    @Column(name = "PROFILENAME")
    private String profileName;
    @Searchable
    @Filter
    @NotBlank
    @NotEmpty
    @Column(name = "AIDS")
    private String AidsNo;
    @Column(name = "DEFAULTACTIONCODE")
    private String defaultActionCode;
    @Column(name = "ONLINEACTIONCODE")
    private String onlineActionCode;
    @Column(name = "DENIALACTIONCODE")
    private String denialActionCode;
}
