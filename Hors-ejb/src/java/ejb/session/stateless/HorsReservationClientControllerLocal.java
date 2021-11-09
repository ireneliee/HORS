/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.GuestEntity;
import entity.PaymentEntity;
import entity.RoomReservationEntity;
import entity.RoomTypeEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.LineItemExistException;
import util.exception.NoAvailableRoomOptionException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;
import util.reservation.Pair;

/**
 *
 * @author irene
 */
@Local
public interface HorsReservationClientControllerLocal {

    public GuestEntity guestLogin(String username, String password) throws InvalidLoginCredentialException;

    public GuestEntity retrieveGuestByUsername(String username) throws GuestNotFoundException;

    public Long guestRegister(GuestEntity newGuestEntity) throws UsernameExistException, UnknownPersistenceException;

    public List<Pair> searchRoom(int reserveType, LocalDate checkinDate, LocalDate checkoutDate, Integer numberOfRooms) throws NoAvailableRoomOptionException;
    
    public Long makeReservation(UserEntity username,List<Pair> roomResults, int response, PaymentEntity payment) throws RoomTypeNotFoundException, InvalidRoomReservationEntityException, LineItemExistException, UnknownPersistenceException;

    public List<RoomReservationEntity> viewAllReservation(Long userId) throws ReservationNotFoundException, GuestNotFoundException;

    public RoomReservationEntity viewReservationDetails(Long reservationId) throws ReservationNotFoundException;
}
