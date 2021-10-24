/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author irene
 */
@Entity
public class GuestEntity extends UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public GuestEntity(){
        super();
    }

   

    @Override
    public String toString() {
        return "entity.GuestEntity[ id=" + userId + " ]";
    }
    
}
