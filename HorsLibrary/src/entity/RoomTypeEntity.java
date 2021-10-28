/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    
    @Column(unique = true)
    private String name;
    private String description;
    private String roomSize;
    private Integer bed;
    private Integer capacity;
    private String amenities;
    private Integer roomRanking;
    private Boolean disabled;
    
    @OneToMany(mappedBy="roomType", fetch = FetchType.EAGER)
    private List<RoomEntity> roomEntities;
    
    @OneToMany(mappedBy="roomType", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoomTypeAvailability> roomTypeAvailabilities;
   
            
    public RoomTypeEntity() {
        roomEntities = new ArrayList<>();
        roomTypeAvailabilities = new ArrayList<>();
        disabled = false;
    }

    public RoomTypeEntity(String name, String description, String size, Integer bed, Integer capacity, String amenities, Integer rank) {
        super();
        this.name = name;
        this.description = description;
        this.roomSize = size;
        this.bed = bed;
        this.capacity = capacity;
        this.amenities = amenities;
        this.roomRanking = rank;
    }
    

    
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

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
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

    public Integer getRoomRanking() {
        return roomRanking;
    }

    public void setRoomRanking(Integer roomRanking) {
        this.roomRanking = roomRanking;
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

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    
    
    @Override
    public String toString() {
        return "Name: " + this.getName() +"\n" + "Description: " + this.getDescription() + "\n" + 
                "Size: " + this.getRoomSize() + "\n" + "Bed: " + this.getBed() + "\n" + "Capacity: " + this.getCapacity() + "\n" + 
                "Amenities: " + this.getAmenities() + "\n" + "Rank: " + this.getRoomRanking() + "\n";
    }
    
}
