/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;



@Entity
public class GuestEntity extends UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public GuestEntity(){
        super();
    }
    
    public GuestEntity(String email) {
        super(email);
        
    }

    @Override
    public String toString() {
        return "entity.GuestEntity[ id=" + getUserId() + " ]";
    }

   
    
}
