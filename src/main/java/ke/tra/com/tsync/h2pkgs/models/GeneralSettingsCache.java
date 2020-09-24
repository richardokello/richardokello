package ke.tra.com.tsync.h2pkgs.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "GENERAL_SETTINGS_CONFIG_CACHE")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralSettingsCache {

    @Id
    private long id;

    @Column(name = "crdb_session_str")
    private String crdbSessionKey;

    @Column(name = "updated")
    private boolean updated;

}
