/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author irene
 */
@Entity
public class RoomReservationLineItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomReservationLineItemId;
    
    @OneToOne (optional = false)
    private RoomTypeEntity roomTypeEntity;
    
    @OneToMany
    private List<RoomRateEntity> roomRatesPerNight;
    
    private BigDecimal subTotal;
    private LocalDate checkInDate;
    private LocalDate checkoutDate;
    
    @OneToOne
    private RoomEntity roomAllocation;
    private Boolean checkedIn;
    private Boolean checkedOut;
    
    public RoomReservationLineItemEntity(){
        this.roomRatesPerNight = new ArrayList<>();
        this.checkedIn = false;
        this.checkedOut = false;
    }

    public RoomReservationLineItemEntity(RoomTypeEntity roomTypeEntity, BigDecimal subTotal, 
            LocalDate checkInDate, LocalDate checkoutDate) {
        this();
        this.roomTypeEntity = roomTypeEntity;
        this.subTotal = subTotal;
        this.checkInDate = checkInDate;
        this.checkoutDate = checkoutDate;
    }
    
    
    public Long getRoomReservationLineItemId() {
        return roomReservationLineItemId;
    }

    public void setRoomReservationLineItemId(Long roomReservationLineItemId) {
        this.roomReservationLineItemId = roomReservationLineItemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomReservationLineItemId != null ? roomReservationLineItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomReservationLineItemId fields are not set
        if (!(object instanceof RoomReservationLineItemEntity)) {
            return false;
        }
        RoomReservationLineItemEntity other = (RoomReservationLineItemEntity) object;
        if ((this.roomReservationLineItemId == null && other.roomReservationLineItemId != null) || (this.roomReservationLineItemId != null && !this.roomReservationLineItemId.equals(other.roomReservationLineItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomReservationLineItem[ id=" + roomReservationLineItemId + " ]";
    }

    public List<RoomRateEntity> getRoomRatesPerNight() {
        return roomRatesPerNight;
    }

    public void setRoomRatesPerNight(List<RoomRateEntity> roomRatesPerNight) {
        this.roomRatesPerNight = roomRatesPerNight;
    }

    public RoomTypeEntity getRoomTypeEntity() {
        return roomTypeEntity;
    }

    public void setRoomTypeEntity(RoomTypeEntity roomTypeEntity) {
        this.roomTypeEntity = roomTypeEntity;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public RoomEntity getRoomAllocation() {
        return roomAllocation;
    }

    public void setRoomAllocation(RoomEntity roomAllocation) {
        this.roomAllocation = roomAllocation;
    }
    
}
