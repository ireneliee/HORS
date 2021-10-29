/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Entity;

/**
 *
 * @author irene
 */
@Entity
public class PublishedRateEntity extends RoomRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public PublishedRateEntity(){
        super();
    }
    
    public PublishedRateEntity(String name, BigDecimal rate, RoomTypeEntity roomType) {
        super(name, rate, roomType, LocalDate.now(), LocalDate.of(2100, 01, 01));
    }
    
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (super.getRoomRateId() != null ? super.getRoomRateId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PublishedRateEntity)) {
            return false;
        }
        PublishedRateEntity other = (PublishedRateEntity) object;
        if ((super.getRoomRateId() == null && other.getRoomRateId() != null) ||
                (super.getRoomRateId()!= null && !this.getRoomRateId().equals(other.getRoomRateId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return super.toString();
    }
    
}
