/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.session.stateful.ReserveOperationSessionBeanLocal;
import entity.GuestEntity;
import entity.PaymentEntity;
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
    public List<Pair<RoomTypeEntity, BigDecimal>> searchRoom(int reserveType, LocalDate checkinDate, LocalDate checkoutDate, Integer numberOfRooms){
        return reserveOperationSessionBean.searchRoom(reserveType, checkinDate, checkoutDate, numberOfRooms);
    }
    
    @Override
    public Long makeReservation(UserEntity username,int response, PaymentEntity payment) throws RoomTypeNotFoundException, InvalidRoomReservationEntityException {
        return reserveOperationSessionBean.makeReservation(username, response, payment);
    }
    
    
}
