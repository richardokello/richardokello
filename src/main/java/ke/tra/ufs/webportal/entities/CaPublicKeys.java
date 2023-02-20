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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CA_PUBLIC_KEYS")
public class CaPublicKeys {
    private static final long UUID=0L;
    @Column(name = "ID")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "UUID2")
    private Long Id;
    @Column(name = "ISSUER")
    @Searchable
    @Filter
    private String issuer;
    @Column(name = "EXPONENT")
    @Searchable
    private String exponent;
    @Column(name = "RID_INDEX")
    @Filter
    @Searchable
    private String ridIndex;
    @Column(name = "RIDLIST")
    @Searchable
    @Filter
    private String ridList;
    @Column(name = "MODULUS")
    @Searchable
    private String modulus;
    @Column(name = "KEYLENGTH")
    private String keyLength;
    @Column(name = "SHA1")
    private String SHA1;
    @Column(name = "KEYTYPE")
    private String keyType;
    @Column(name = "EXPIRES_AT")
    private String expiry;
}
