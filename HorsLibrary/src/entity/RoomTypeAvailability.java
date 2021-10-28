/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author irene
 */
@Entity
public class RoomTypeAvailability implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeAvailabilityId;
    private LocalDate dateOfAvailability;
    private Integer noOfAvailableRoom;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomTypeEntity roomType;

    
    public RoomTypeAvailability(){}
    
    public RoomTypeAvailability(LocalDate dateOfAvailability, Integer noOfAvailableRoom, RoomTypeEntity roomType) {
        this.dateOfAvailability = dateOfAvailability;
        this.noOfAvailableRoom = noOfAvailableRoom;
        this.roomType = roomType;
    }
    public Long getRoomTypeAvailabilityId() {
        return roomTypeAvailabilityId;
    }

    public void setRoomTypeAvailabilityId(Long roomTypeAvailabilityId) {
        this.roomTypeAvailabilityId = roomTypeAvailabilityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeAvailabilityId != null ? roomTypeAvailabilityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeAvailabilityId fields are not set
        if (!(object instanceof RoomTypeAvailability)) {
            return false;
        }
        RoomTypeAvailability other = (RoomTypeAvailability) object;
        if ((this.roomTypeAvailabilityId == null && other.roomTypeAvailabilityId != null) || (this.roomTypeAvailabilityId != null && !this.roomTypeAvailabilityId.equals(other.roomTypeAvailabilityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomTypeAvailability[ id=" + roomTypeAvailabilityId + " ]";
    }

    public Integer getNoOfAvailableRoom() {
        return noOfAvailableRoom;
    }

    public void setNoOfAvailableRoom(Integer noOfAvailableRoom) {
        this.noOfAvailableRoom = noOfAvailableRoom;
    }
    
    public void incrementNoOfAvailableRoomByOne() {
        this.noOfAvailableRoom = this.noOfAvailableRoom + 1;
    }
    
    public void decreaseNoOfAvailableRoomByOne() {
        this.noOfAvailableRoom = this.noOfAvailableRoom - 1;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    public LocalDate getDateOfAvailability() {
        return dateOfAvailability;
    }

    public void setDateOfAvailability(LocalDate dateOfAvailability) {
        this.dateOfAvailability = dateOfAvailability;
    }
    
}
