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
import static jdk.nashorn.internal.runtime.Debug.id;

/**
 *
 * @author irene
 */
@Entity
public class NormalRateEntity extends RoomRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public NormalRateEntity(){
        super();
    }
    
    public NormalRateEntity(String name, BigDecimal rate, RoomTypeEntity roomType) {
        super(name, rate, roomType, LocalDate.now(), LocalDate.of(2100, 01, 01));
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getRoomRateId() != null ? this.getRoomRateId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NormalRateEntity)) {
            return false;
        }
        NormalRateEntity other = (NormalRateEntity) object;
        if ((this.getRoomRateId() == null && other.getRoomRateId() != null) ||
                (this.getRoomRateId() != null && !this.getRoomRateId().equals(other.getRoomRateId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return super.toString();
    }
    
}
