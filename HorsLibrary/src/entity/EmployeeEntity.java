/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import static jdk.nashorn.internal.runtime.Debug.id;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author irene
 */
@Entity
public class EmployeeEntity extends UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String password;
    AccessRightEnum accessRight;
    
    public EmployeeEntity() {
        super();
    }

    public EmployeeEntity(String firstName, String lastName, String username, 
            String password, AccessRightEnum accessRight) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.accessRight = accessRight;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.userId != null ? this.userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeEntity)) {
            return false;
        }
        EmployeeEntity other = (EmployeeEntity) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EmployeeEntity[ id=" + userId + " ]";
    }
    
}
