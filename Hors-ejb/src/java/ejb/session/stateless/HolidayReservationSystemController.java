/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author irene
 */
@Stateless
public class HolidayReservationSystemController implements HolidayReservationSystemControllerRemote, HolidayReservationSystemControllerLocal {

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBean;

    public HolidayReservationSystemController(){}
    
    @Override
    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException{
        return partnerEntitySessionBean.retrievePartnerByUsername(username);
    }
    
    @Override
    public PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException{
        return partnerEntitySessionBean.partnerLogin(username, password);
    }
    
    
}
