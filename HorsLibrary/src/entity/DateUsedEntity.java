/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
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
public class DateUsedEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dateUsedId;
    
    private LocalDate dateUsed;
    
    public DateUsedEntity(){}
    
    public DateUsedEntity(LocalDate date) {
        this.dateUsed = date;
    }

    
    public Long getDateUsedId() {
        return dateUsedId;
    }

    public void setDateUsedId(Long dateUsedId) {
        this.dateUsedId = dateUsedId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dateUsedId != null ? dateUsedId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the dateUsedId fields are not set
        if (!(object instanceof DateUsedEntity)) {
            return false;
        }
        DateUsedEntity other = (DateUsedEntity) object;
        if ((this.dateUsedId == null && other.dateUsedId != null) || (this.dateUsedId != null && !this.dateUsedId.equals(other.dateUsedId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DateUsedEntity[ id=" + dateUsedId + " ]";
    }

    public LocalDate getDateUsed() {
        return dateUsed;
    }

    public void setDateUsed(LocalDate dateUsed) {
        this.dateUsed = dateUsed;
    }
    
}
