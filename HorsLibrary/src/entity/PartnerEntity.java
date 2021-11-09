/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;


/**
 *
 * @author irene
 */
@Entity
public class PartnerEntity extends UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(nullable = false, length = 32)
    private String partnerName;
    
    @Column(nullable = false, length = 32, unique = true)
    private String username;
    
    @Column(nullable = false, length = 32)
    private String password;
    
    public PartnerEntity(){
        super();
    }

    public PartnerEntity(String partnerName, String username, String password) {
        this.partnerName = partnerName;
        this.username = username;
        this.password = password;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getUserId() != null ? getUserId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PartnerEntity)) {
            return false;
        }
        PartnerEntity other = (PartnerEntity) object;
        if ((this.getUserId()== null && other.getUserId() != null) || (this.getUserId()!= null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PartnerEntity[ id=" + getUserId() + " ]";
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
