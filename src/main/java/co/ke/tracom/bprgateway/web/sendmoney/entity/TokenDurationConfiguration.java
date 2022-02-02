package co.ke.tracom.bprgateway.web.sendmoney.entity;

import co.ke.tracom.bprgateway.web.sendmoney.data.TokenDuration;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity(name = "TOKEN_DURATION_CONFIGURATION")
@Table(name = "TOKEN_DURATION_CONFIGURATION", uniqueConstraints = {
        @UniqueConstraint(name = "CONFIGURATION_NAME_UNIQUE", columnNames = "NAME") })
public class TokenDurationConfiguration {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long Id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "INITIAL_DURATION_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private TokenDuration initialDuration;
    @Column(name = "INITIAL_DURATION_LENGTH")
    private int initialDurationLength;
    @Column(name = "SECOND_DURATION_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private TokenDuration secondDuration;
    @Column(name = "SECOND_DURATION_LENGTH")
    private int secondDurationLength;
}


