/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
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
public class RoomTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RoomTypeId;
    private String name;
    private String description;
    private String size;
    private Integer bed;
    private Integer capacity;
    private String amenities;
    private Integer rank;
    
    @OneToMany(mappedBy="roomType")
    private List<RoomEntity> roomEntities;
    
    @OneToMany(mappedBy = "roomType")
    private List<RoomTypeAvailability> roomTypeAvailabilities;
            

    public Long getRoomTypeId() {
        return RoomTypeId;
    }

    public void setRoomTypeId(Long RoomTypeId) {
        this.RoomTypeId = RoomTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (RoomTypeId != null ? RoomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the RoomTypeId fields are not set
        if (!(object instanceof RoomTypeEntity)) {
            return false;
        }
        RoomTypeEntity other = (RoomTypeEntity) object;
        if ((this.RoomTypeId == null && other.RoomTypeId != null) || (this.RoomTypeId != null && !this.RoomTypeId.equals(other.RoomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomTypeEntity[ id=" + RoomTypeId + " ]";
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getBed() {
        return bed;
    }

    public void setBed(Integer bed) {
        this.bed = bed;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public List<RoomEntity> getRoomEntities() {
        return roomEntities;
    }

    public void setRoomEntities(List<RoomEntity> roomEntities) {
        this.roomEntities = roomEntities;
    }

    public List<RoomTypeAvailability> getRoomTypeAvailabilities() {
        return roomTypeAvailabilities;
    }

    public void setRoomTypeAvailabilities(List<RoomTypeAvailability> roomTypeAvailabilities) {
        this.roomTypeAvailabilities = roomTypeAvailabilities;
    }
    
}
