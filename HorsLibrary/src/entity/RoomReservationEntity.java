/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author irene
 */
@Entity
public class RoomReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomReservationId;
    
    @OneToMany
    private List<RoomReservationLineItemEntity> roomReservationLineItems;
    private BigDecimal totalAmount;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn (nullable = false)
    private UserEntity bookingAccount;
    
    private LocalDate reservationDate;
    private PaymentEntity payment;
    
    public RoomReservationEntity(){
        roomReservationLineItems = new ArrayList<>();
    }

    public RoomReservationEntity(BigDecimal totalAmount, LocalDate reservationDate) {
        this();
        this.totalAmount = totalAmount;
        this.reservationDate = reservationDate;
    }
    
    
    public Long getRoomReservationId() {
        return roomReservationId;
    }

    public void setRoomReservationId(Long roomReservationId) {
        this.roomReservationId = roomReservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomReservationId != null ? roomReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomReservationId fields are not set
        if (!(object instanceof RoomReservationEntity)) {
            return false;
        }
        RoomReservationEntity other = (RoomReservationEntity) object;
        if ((this.roomReservationId == null && other.roomReservationId != null) || (this.roomReservationId != null && !this.roomReservationId.equals(other.roomReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomReservationEntity[ id=" + roomReservationId + " ]";
    }

    public List<RoomReservationLineItemEntity> getRoomReservationLineItems() {
        return roomReservationLineItems;
    }

    public void setRoomReservationLineItems(List<RoomReservationLineItemEntity> roomReservationLineItems) {
        this.roomReservationLineItems = roomReservationLineItems;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

    public UserEntity getBookingAccount() {
        return bookingAccount;
    }

    public void setBookingAccount(UserEntity bookingAccount) {
        this.bookingAccount = bookingAccount;
    }
    
}
