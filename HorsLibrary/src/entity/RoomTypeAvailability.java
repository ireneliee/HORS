/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private Date date;
    private Integer noOfAvailableRoom;
    @ManyToOne
    private RoomTypeEntity roomType;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getNoOfAvailableRoom() {
        return noOfAvailableRoom;
    }

    public void setNoOfAvailableRoom(Integer noOfAvailableRoom) {
        this.noOfAvailableRoom = noOfAvailableRoom;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }
    
}
