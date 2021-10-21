/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.enumeration.RoomStatusEnum;

/**
 *
 * @author irene
 */
@Entity
public class RoomEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RoomEntityId;
    private Integer roomNumber;
    private RoomStatusEnum roomStatus;
    //private RoomTypeEntity roomType;

    public RoomEntity() {
    }

    public RoomEntity(Integer roomNumber, RoomStatusEnum roomStatus) {
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
        return "entity.RoomEntity[ id=" + RoomEntityId + " ]";
    }
    
}
