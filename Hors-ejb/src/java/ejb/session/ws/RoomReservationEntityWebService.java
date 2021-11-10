/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.RoomReservationEntitySessionBeanLocal;
import entity.RoomReservationEntity;
import entity.UserEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author zenyew
 */
@WebService(serviceName = "RoomReservationEntityWebService")
@Stateless()
public class RoomReservationEntityWebService {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    @EJB
    private RoomReservationEntitySessionBeanLocal roomReservationEntitySessionBean;

    
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "viewAllMyReservations")
    public List<RoomReservationEntity> viewAllMyReservations(@WebParam(name = "userId")Long userId) throws ReservationNotFoundException{
    
        List<RoomReservationEntity> myReservations = roomReservationEntitySessionBean.viewAllMyReservation(userId);
        
        for(RoomReservationEntity reservation : myReservations) {
            em.detach(reservation);
            
            reservation.setBookingAccount(null);

        }
        
        return myReservations;
        
    }
    
    
    @WebMethod(operationName = "viewReservationDetails")
    public RoomReservationEntity viewReservationDetails(@WebParam(name = "reservationId")Long reservationId) throws ReservationNotFoundException{
    
        RoomReservationEntity reservation = roomReservationEntitySessionBean.viewReservationDetails(reservationId);
        
        
        em.detach(reservation);

        reservation.setBookingAccount(null);

        return reservation;
        
    }
 
}


    

