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
public class PeakRateEntity extends RoomRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public PeakRateEntity(){
        super();
        this.roomRank = 2;
    }
    
    public PeakRateEntity(String name, LocalDate startValidityDate, LocalDate endValidityDate,
            BigDecimal rate, RoomTypeEntity roomType) {
        super(name, rate, roomType, startValidityDate, endValidityDate);
        this.roomRank = 2;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getRoomRateId()!= null ? this.getRoomRateId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeakRateEntity)) {
            return false;
        }
        PeakRateEntity other = (PeakRateEntity) object;
        if ((this.getRoomRateId() == null && other.getRoomRateId() != null) ||
                (this.getRoomRateId() != null && !this.getRoomRateId().equals(other.getRoomRateId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        
        return super.toString() + "Start validity date: " + this.getStartValidityDate().toString() + ";\n" +
                "End validity date: " + this.getEndValidityDate().toString() + ";\n";
    }
    
}
