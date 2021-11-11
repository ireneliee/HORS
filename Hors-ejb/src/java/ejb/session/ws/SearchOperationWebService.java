/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import datamodel.ws.PairRemote;
import ejb.session.stateful.ReserveOperationSessionBeanLocal;
import ejb.session.stateless.RoomReservationEntitySessionBeanLocal;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import entity.RoomReservationLineItemEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.LineItemExistException;
import util.exception.NoAvailableRoomOptionException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.reservation.Pair;

/**
 *
 * @author zenyew
 */
@WebService(serviceName = "SearchOperationWebService")
@Stateless()
public class SearchOperationWebService {

    @EJB
    private RoomReservationEntitySessionBeanLocal roomReservationEntitySessionBean;

    @EJB
    private ReserveOperationSessionBeanLocal reserveOperationSessionBean;
    
    

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;
    
    


    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "searchRoom")
    public List<PairRemote> searchRoom(@WebParam(name = "cinYear") int cinYear,
                                    @WebParam(name = "cinMonth") int cinMonth,
                                    @WebParam(name = "cinDay") int cinDay,
                                    @WebParam(name = "coutYear") int coutYear,
                                    @WebParam(name = "coutMonth") int coutMonth,
                                    @WebParam(name = "coutDay") int coutDay,
                                    @WebParam(name = "numberOfRooms") int numberOfRooms) throws NoAvailableRoomOptionException{
        LocalDate checkinDate = LocalDate.of(cinYear, cinMonth, cinDay);
        LocalDate checkoutDate = LocalDate.of(coutYear, coutMonth, coutDay);
        List<Pair> roomResults = reserveOperationSessionBean.searchRoom(4, checkinDate, checkoutDate, numberOfRooms);
        List<PairRemote> roomPass = new ArrayList<>();
        
        for(Pair pair :  roomResults) {
            RoomTypeEntity roomType = pair.getRoomType();
            em.detach(roomType);
            for(RoomEntity room : roomType.getRoomEntities()) {
                em.detach(room);
                room.setRoomType(null);
            }
            roomPass.add(new PairRemote(pair.getRoomType(), pair.getPrice()));
        }
        
        return roomPass;
    }
    
    @WebMethod(operationName = "makeReservation")
    public Long makeReservation(@WebParam(name = "cinYear") int cinYear,
                                    @WebParam(name = "cinMonth") int cinMonth,
                                    @WebParam(name = "cinDay") int cinDay,
                                    @WebParam(name = "coutYear") int coutYear,
                                    @WebParam(name = "coutMonth") int coutMonth,
                                    @WebParam(name = "coutDay") int coutDay,
                                    @WebParam(name = "numberOfRooms") int numberOfRooms,
                                    @WebParam(name = "userId") Long userId,
                                    @WebParam(name = "roomTypeName") String roomTypeName,
                                    @WebParam(name = "totalAmount") double totalAmount,
                                    @WebParam(name = "paymentType") int paymentType) throws RoomTypeNotFoundException, InvalidRoomReservationEntityException,
                                             LineItemExistException, UnknownPersistenceException {
        
        
        LocalDate checkinDate = LocalDate.of(cinYear, cinMonth, cinDay);
        LocalDate checkoutDate = LocalDate.of(coutYear, coutMonth, coutDay);
        RoomReservationEntity newReservation = roomReservationEntitySessionBean.makeReservation(userId, roomTypeName, totalAmount, paymentType, numberOfRooms, checkinDate, checkoutDate);
        
        em.detach(newReservation);
        em.detach(newReservation.getBookingAccount());
        newReservation.setBookingAccount(null);
        for(RoomReservationLineItemEntity lineItem : newReservation.getRoomReservationLineItems()) {
                em.detach(lineItem);
                em.detach(lineItem.getRoomTypeEntity());
                for(RoomEntity room : lineItem.getRoomTypeEntity().getRoomEntities()){
                    em.detach(room);
                    room.setRoomType(null);
                }
            }
        
        return newReservation.getRoomReservationId();
        
    }
    
}
        
        

    