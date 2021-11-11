/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.RoomStatusEnum;


@Entity
public class RoomEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RoomEntityId;
    
    
    @NotNull
    @Size(min = 4, max = 4)
    @Column(nullable = false, unique = true, length = 4)
    private String roomNumber;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private RoomStatusEnum roomStatus;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomTypeEntity roomType;


    public RoomEntity() {

    }
    
    public RoomEntity(String roomNumber, RoomStatusEnum roomStatus) {
        this.roomNumber = roomNumber;
        this.roomStatus = roomStatus;
    }

    public RoomEntity(String roomNumber, RoomStatusEnum roomStatus, RoomTypeEntity roomType) {
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.roomStatus = roomStatus;
        
    }
    
    
    public Long getRoomEntityId() {
        return RoomEntityId;
    }

    public void setRoomEntityId(Long RoomEntityId) {
        this.RoomEntityId = RoomEntityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (RoomEntityId != null ? RoomEntityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the RoomEntityId fields are not set
        if (!(object instanceof RoomEntity)) {
            return false;
        }
        RoomEntity other = (RoomEntity) object;
        if ((this.RoomEntityId == null && other.RoomEntityId != null) || (this.RoomEntityId != null && !this.RoomEntityId.equals(other.RoomEntityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Room number: " + this.getRoomNumber() + "\n" + "Room Type: " + this.getRoomType() + "\n" +
                "Availability: " + this.getRoomStatus().toString() + "\n";
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomStatusEnum getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatusEnum roomStatus) {
        this.roomStatus = roomStatus;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    
}
