/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class GuestEntity extends UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false, length = 32)
    @Size(min = 1, max = 32)
    @NotNull
    private String firstName;
    
    @Column(nullable = false, length = 32)
    @Size(min = 1, max = 32)
    @NotNull
    private String lastName;
    
    @Column(nullable = false, unique = true)
    @Size(min = 1, max = 32)
    @NotNull
    private String username;
    
    
    @Column(nullable = false)
    @Size(min = 1, max = 32)
    @NotNull
    private String password;
    
    @Column(nullable = false, unique = true, length = 15)
    @Size(min = 1, max = 15)
    @NotNull
    private String mobileNo;
    
    @Column(nullable = false, unique = true, length = 15)
    @Size(min = 1, max = 15)
    @NotNull
    private String passportNo;

    public GuestEntity() {
    }

    public GuestEntity(String firstName, String lastName, String username, String password, String email, String mobileNo, String passportNo) {
        super(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.mobileNo = mobileNo;
        this.passportNo = passportNo;
    }

    public GuestEntity(String email) {
        super(email);

    }

    @Override
    public String toString() {
        return "entity.GuestEntity[ id=" + getUserId() + " ]";
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GuestEntity)) {
            return false;
        }
        GuestEntity other = (GuestEntity) object;
        if ((this.getUserId() == null && other.getUserId() != null) || (this.getUserId() != null
                && !this.getUserId().equals(other.getUserId()))) {
            return false;
        }
        return true;
    }

    /**
     * @return the mobileNo
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * @param mobileNo the mobileNo to set
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * @return the passportNo
     */
    public String getPassportNo() {
        return passportNo;
    }

    /**
     * @param passportNo the passportNo to set
     */
    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

}
