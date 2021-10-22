/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author irene
 */
@Entity
public class RoomAllocationInADayEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany
    private List<RoomReservationLineItemEntity> roomReservationAllocated;
    
    @OneToMany
    private List<RoomReservationLineItemEntity> roomReservationNotAllocated;
    
    private LocalDate dateOfAllocation;

    public RoomAllocationInADayEntity() {
        roomReservationAllocated = new ArrayList<>();
        roomReservationNotAllocated = new ArrayList<>();
    }
    
    public RoomAllocationInADayEntity(LocalDate dateOfAllocation) {
        this();
        this.dateOfAllocation = dateOfAllocation;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof RoomAllocationInADayEntity)) {
            return false;
        }
        RoomAllocationInADayEntity other = (RoomAllocationInADayEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomAllocationInADayEntity[ id=" + id + " ]";
    }

    public List<RoomReservationLineItemEntity> getRoomReservationAllocated() {
        return roomReservationAllocated;
    }

    public void setRoomReservationAllocated(List<RoomReservationLineItemEntity> roomReservationAllocated) {
        this.roomReservationAllocated = roomReservationAllocated;
    }

    public List<RoomReservationLineItemEntity> getRoomReservationNotAllocated() {
        return roomReservationNotAllocated;
    }

    public void setRoomReservationNotAllocated(List<RoomReservationLineItemEntity> roomReservationNotAllocated) {
        this.roomReservationNotAllocated = roomReservationNotAllocated;
    }

    public LocalDate getDateOfAllocation() {
        return dateOfAllocation;
    }

    public void setDateOfAllocation(LocalDate dateOfAllocation) {
        this.dateOfAllocation = dateOfAllocation;
    }
    
}
