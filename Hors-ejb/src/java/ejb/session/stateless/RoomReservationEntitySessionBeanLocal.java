/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import entity.RoomReservationEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.GuestHasNotCheckedInException;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.NoMoreRoomToAccomodateException;
import util.exception.ReservationNotFoundException;
import util.exception.WrongCheckInDate;
import util.exception.WrongCheckoutDate;

/**
 *
 * @author irene
 */
@Local
public interface RoomReservationEntitySessionBeanLocal {

    public Long createNewRoomReservationEntity(Long userId, RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException;

    public Long createNewRoomReservationEntity(RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException;

    public List<RoomEntity> checkIn(Long roomReservationId, LocalDate date) throws InvalidRoomReservationEntityException, WrongCheckInDate, NoMoreRoomToAccomodateException;

    public void checkOut(Long roomReservationId, LocalDate date) throws WrongCheckoutDate, InvalidRoomReservationEntityException,
            GuestHasNotCheckedInException;

    public List<RoomReservationEntity> viewAllMyReservation(Long userId);

    public RoomReservationEntity viewReservationDetails(Long reservationId) throws ReservationNotFoundException;
    
}
