/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.session.stateful.ReserveOperationSessionBeanLocal;
import entity.GuestEntity;
import entity.PaymentEntity;
import entity.RoomReservationEntity;
import entity.RoomTypeEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
@Stateless
public class HorsReservationClientController implements HorsReservationClientControllerRemote, HorsReservationClientControllerLocal {


    @EJB
    private RoomReservationEntitySessionBeanLocal roomReservationEntitySessionBean;

    @EJB
    private ReserveOperationSessionBeanLocal reserveOperationSessionBean;

    @EJB
    private GuestEntitySessionBeanLocal guestEntitySessionBean;

    public HorsReservationClientController() {
    }

    @Override
    public GuestEntity guestLogin(String username, String password) throws InvalidLoginCredentialException {
        return guestEntitySessionBean.guestLogin(username, password);
    }
    
    @Override
    public GuestEntity retrieveGuestByUsername(String username) throws GuestNotFoundException{
        return guestEntitySessionBean.retrieveGuestByUsername(username);
    }
    
    @Override
    public Long guestRegister(GuestEntity newGuestEntity) throws UsernameExistException, UnknownPersistenceException{
        return guestEntitySessionBean.guestRegister(newGuestEntity);
    }
    
    @Override
    public List<Pair> searchRoom(int reserveType, LocalDate checkinDate, LocalDate checkoutDate, Integer numberOfRooms)  throws NoAvailableRoomOptionException{
        return reserveOperationSessionBean.searchRoom(reserveType, checkinDate, checkoutDate, numberOfRooms);
    }
    
    @Override
    public Long makeReservation(UserEntity username,List<Pair> roomResults,int response, PaymentEntity payment) throws RoomTypeNotFoundException, InvalidRoomReservationEntityException, LineItemExistException, UnknownPersistenceException {
        return reserveOperationSessionBean.makeReservation(username, roomResults, response, payment);
    }
    
    @Override
    public List<RoomReservationEntity> viewAllReservation(Long userId) throws ReservationNotFoundException, GuestNotFoundException{
        return roomReservationEntitySessionBean.viewAllMyReservation(userId);
    }
    
    public RoomReservationEntity viewReservationDetails(Long reservationId) throws ReservationNotFoundException{
        return roomReservationEntitySessionBean.viewReservationDetails(reservationId);
    }
    
    
    
    
}
