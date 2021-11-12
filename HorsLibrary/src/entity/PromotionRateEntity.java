
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author irene
 */
@Entity
public class PromotionRateEntity extends RoomRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
   
    public PromotionRateEntity(){
        super();
        this.roomRank = 4;
    }
    
    public PromotionRateEntity (String name, LocalDate startValidityDate, LocalDate endValidityDate,
            BigDecimal rate, RoomTypeEntity roomType) {
        super(name, rate, roomType, startValidityDate, endValidityDate);
        this.roomRank = 4;
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
        if (!(object instanceof PromotionRateEntity)) {
            return false;
        }
        PromotionRateEntity other = (PromotionRateEntity) object;
        if ((this.getRoomRateId() == null && other.getRoomRateId() != null) ||
                (this.getRoomRateId() != null && !this.getRoomRateId().equals(other.getRoomRateId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Room rate name: " + this.getName() + ";\n" + "Rating at: " + this.getRate().toString() + ";\n" +
                "Room type: " + this.getRoomType().getClass().getSimpleName() + ";\n" + "Promotion rate name: " + this.getName() + ";\n" + "Start validity date: " + this.getStartValidityDate().toString() + ";\n" +
                "End validity date: " + this.getEndValidityDate().toString() + ";\n";
    }
    
  
    
}
