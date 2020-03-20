/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "PAR_CARDS")
@NamedQueries({
    @NamedQuery(name = "ParCards.findAll", query = "SELECT p FROM ParCards p")})
public class ParCards implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CA_KEY")
    private String caKey;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @Basic(optional = false)
    @Column(name = "CARD_SCHEMES")
    private String cardSchemes;
    @Column(name = "FLOOR_LIMIT")
    private Long floorLimit;
    @Column(name = "CEILING_LIMIT")
    private Long ceilingLimit;
    @Column(name = "AID")
    private String aid;
    @Column(name = "CARD_ID")
    private String cardId;
    @JoinColumn(name = "CARD_TYPE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ParCardTypes cardType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cardId")
    private Collection<ParCardAllowedTrnx> parCardAllowedTrnxCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cardId")
    private Collection<ParCardDisallowedTrnx> parCardDisallowedTrnxCollection;

    public ParCards() {
    }

    public ParCards(Long id) {
        this.id = id;
    }

    public ParCards(Long id, String name, String description, String cardSchemes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cardSchemes = cardSchemes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getCaKey() {
        return caKey;
    }

    public void setCaKey(String caKey) {
        this.caKey = caKey;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public String getCardSchemes() {
        return cardSchemes;
    }

    public void setCardSchemes(String cardSchemes) {
        this.cardSchemes = cardSchemes;
    }

    public Long getFloorLimit() {
        return floorLimit;
    }

    public void setFloorLimit(Long floorLimit) {
        this.floorLimit = floorLimit;
    }

    public Long getCeilingLimit() {
        return ceilingLimit;
    }

    public void setCeilingLimit(Long ceilingLimit) {
        this.ceilingLimit = ceilingLimit;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public ParCardTypes getCardType() {
        return cardType;
    }

    public void setCardType(ParCardTypes cardType) {
        this.cardType = cardType;
    }

    public Collection<ParCardAllowedTrnx> getParCardAllowedTrnxCollection() {
        return parCardAllowedTrnxCollection;
    }

    public void setParCardAllowedTrnxCollection(Collection<ParCardAllowedTrnx> parCardAllowedTrnxCollection) {
        this.parCardAllowedTrnxCollection = parCardAllowedTrnxCollection;
    }

    public Collection<ParCardDisallowedTrnx> getParCardDisallowedTrnxCollection() {
        return parCardDisallowedTrnxCollection;
    }

    public void setParCardDisallowedTrnxCollection(Collection<ParCardDisallowedTrnx> parCardDisallowedTrnxCollection) {
        this.parCardDisallowedTrnxCollection = parCardDisallowedTrnxCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParCards)) {
            return false;
        }
        ParCards other = (ParCards) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.ParCards[ id=" + id + " ]";
    }
    
}
