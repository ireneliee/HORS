/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author irene
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class RoomRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long roomRateId;
    
    @Column(nullable = false, length = 32)
    protected String name;
    
    @Column(nullable = false, precision = 11, scale = 2)
    protected BigDecimal rate;
    
    @Column(nullable = false, length = 32)
    protected RoomTypeEntity roomType;
    
    @Column(nullable = false)
    protected LocalDate startValidityDate;
    
    @Column(nullable = false)
    protected LocalDate endValidityDate;
    
    @Column(nullable = false)
    protected Boolean disabled;
    
    @Column(nullable = false)
    protected Integer roomRank;
    
    public RoomRateEntity(){
        disabled = false;
    }

    public RoomRateEntity(String name, BigDecimal rate, RoomTypeEntity roomType, LocalDate startValidityDate, LocalDate endValidityDate) {
        this();
        this.name = name;
        this.rate = rate;
        this.roomType = roomType;
        this.startValidityDate = startValidityDate;
        this.endValidityDate = endValidityDate;
    }
    
    public Long getRoomRateId() {
        return roomRateId;
    }

    public void setRoomRateId(Long roomRateId) {
        this.roomRateId = roomRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRateId != null ? roomRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomRateId fields are not set
        if (!(object instanceof RoomRateEntity)) {
            return false;
        }
        RoomRateEntity other = (RoomRateEntity) object;
        if ((this.roomRateId == null && other.roomRateId != null) || (this.roomRateId != null && !this.roomRateId.equals(other.roomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Room rate name: " + this.getName() + ";\n" + "Rating at: " + this.getRate().toString() + ";\n" +
                "Room type: " + this.getRoomType().getClass().getSimpleName() + ";\n";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    public LocalDate getEndValidityDate() {
        return endValidityDate;
    }

    public void setEndValidityDate(LocalDate endValidityDate) {
        this.endValidityDate = endValidityDate;
    }

    public LocalDate getStartValidityDate() {
        return startValidityDate;
    }

    public void setStartValidityDate(LocalDate startValidityDate) {
        this.startValidityDate = startValidityDate;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getRoomRank() {
        return roomRank;
    }

    public void setRoomRank(Integer roomRank) {
        this.roomRank = roomRank;
    }
    
    
    
}
