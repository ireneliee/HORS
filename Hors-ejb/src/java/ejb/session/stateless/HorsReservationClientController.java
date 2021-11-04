/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.GuestEntity;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author irene
 */
@Stateless
public class HorsReservationClientController implements HorsReservationClientControllerRemote, HorsReservationClientControllerLocal {

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
    
    
}
