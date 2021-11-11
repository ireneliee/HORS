/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import entity.PartnerEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import entity.RoomReservationLineItemEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author zenyew
 */
@WebService(serviceName = "PartnerEntityWebService")
@Stateless()
public class PartnerEntityWebService {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBean;
    
    
    

    /**
     * This is a sample web service operation
     */
    
    /*
    public List<PartnerEntity> retrieveAllPartner(@WebParam(name = "username") String username,
                                                    @WebParam(name = "password") String password) 
    {
        
        List<PartnerEntity>  partners = partnerEntitySessionBean.retrieveAllPartner();
        
        for(PartnerEntity partner : partners) {
            em.detach(partner);
            for(RoomReservationEntity reservation : partner.getRoomReservations()) {
                em.detach(reservation);
                reservation.setBookingAccount(null);
            }
        }
        return partners;
    }
    
    public PartnerEntity retrievePartnerByUsername(String username)throws PartnerNotFoundException {
        
        PartnerEntity partner = partnerEntitySessionBean.retrievePartnerByUsername(username);
        
        em.detach(partner);
        for(RoomReservationEntity reservation : partner.getRoomReservations()) {
            em.detach(reservation);
            reservation.setBookingAccount(null);
        }
        return partner;
    }*/
    
    @WebMethod(operationName = "partnerLogin")
    public PartnerEntity partnerLogin(@WebParam(name = "username") String username,
                                        @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        
        PartnerEntity partner = partnerEntitySessionBean.partnerLogin(username, password);
        
        em.detach(partner);
        
        for(RoomReservationEntity reservation : partner.getRoomReservations()) {
            em.detach(reservation);
            reservation.setBookingAccount(null);
            for(RoomReservationLineItemEntity lineItem : reservation.getRoomReservationLineItems()) {
                em.detach(lineItem);
                em.detach(lineItem.getRoomTypeEntity());
                for(RoomEntity room : lineItem.getRoomTypeEntity().getRoomEntities()){
                    em.detach(room);
                    room.setRoomType(null);
                }
            }
        }
        return partner;
    }
    

   
}
