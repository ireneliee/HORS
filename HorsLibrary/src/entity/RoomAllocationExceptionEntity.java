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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

/**
 *
 * @author irene
 */
@Entity
public class RoomAllocationExceptionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomAllocationExceptionId;
    
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<RoomReservationLineItemEntity> typeOneException;
    
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<RoomReservationLineItemEntity> typeTwoException;
    
    private LocalDate dateOfAllocation;

    public RoomAllocationExceptionEntity() {
        typeOneException = new ArrayList<>();
        typeTwoException = new ArrayList<>();
    }
    
    public RoomAllocationExceptionEntity(LocalDate dateOfAllocation) {
        this();
        this.dateOfAllocation = dateOfAllocation;
    }
    
    
    public Long getRoomAllocationExceptionId() {
        return roomAllocationExceptionId;
    }

    public void setRoomAllocationExceptionId(Long roomAllocationExceptionId) {
        this.roomAllocationExceptionId = roomAllocationExceptionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomAllocationExceptionId != null ? roomAllocationExceptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomAllocationExceptionId fields are not set
        if (!(object instanceof RoomAllocationExceptionEntity)) {
            return false;
        }
        RoomAllocationExceptionEntity other = (RoomAllocationExceptionEntity) object;
        if ((this.roomAllocationExceptionId == null && other.roomAllocationExceptionId != null) || (this.roomAllocationExceptionId != null && !this.roomAllocationExceptionId.equals(other.roomAllocationExceptionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomAllocationInADayEntity[ id=" + roomAllocationExceptionId + " ]";
    }

    public List<RoomReservationLineItemEntity> getTypeOneException() {
        return typeOneException;
    }

    public void setTypeOneException(List<RoomReservationLineItemEntity> typeOneException) {
        this.typeOneException = typeOneException;
    }

    public List<RoomReservationLineItemEntity> getTypeTwoException() {
        return typeTwoException;
    }

    public void setTypeTwoException(List<RoomReservationLineItemEntity> typeTwoException) {
        this.typeTwoException = typeTwoException;
    }

    public LocalDate getDateOfAllocation() {
        return dateOfAllocation;
    }

    public void setDateOfAllocation(LocalDate dateOfAllocation) {
        this.dateOfAllocation = dateOfAllocation;
    }
    
    
}
