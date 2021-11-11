/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import entity.RoomReservationEntity;
import entity.RoomTypeEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.GuestHasNotCheckedInException;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.LineItemExistException;
import util.exception.NoMoreRoomToAccomodateException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.WrongCheckInDate;
import util.exception.WrongCheckoutDate;

/**
 *
 * @author irene
 */
@Remote
public interface RoomReservationEntitySessionBeanRemote {

    public RoomReservationEntity createNewRoomReservationEntity(Long userId, RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException;

    public Long createNewRoomReservationEntity(RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException;
    
    public List<RoomEntity> checkIn(Long roomReservationId, LocalDate date) throws InvalidRoomReservationEntityException, WrongCheckInDate, NoMoreRoomToAccomodateException;
    
    public void checkOut(Long roomReservationId, LocalDate date) throws WrongCheckoutDate, InvalidRoomReservationEntityException,
            GuestHasNotCheckedInException;
    
    public List<RoomReservationEntity> viewAllMyReservation(Long userId) throws ReservationNotFoundException;
    
    public RoomReservationEntity viewReservationDetails(Long reservationId) throws ReservationNotFoundException;
    
    public RoomReservationEntity makeReservation(Long userId, String roomTypeName, double amount, int paymentType, int numberOfRooms, LocalDate checkinDate, LocalDate checkoutDate) throws RoomTypeNotFoundException, InvalidRoomReservationEntityException, LineItemExistException, UnknownPersistenceException;

}
