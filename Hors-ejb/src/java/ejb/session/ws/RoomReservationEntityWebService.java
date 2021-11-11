/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.RoomReservationEntitySessionBeanLocal;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import entity.RoomReservationLineItemEntity;
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
    public String viewAllMyReservations(@WebParam(name = "userId") Long userId) throws ReservationNotFoundException {
        List<RoomReservationEntity> myReservations = roomReservationEntitySessionBean.viewAllMyReservation(userId);
        String result = "";

        for (RoomReservationEntity reservation : myReservations) {
            em.detach(reservation);

            reservation.setBookingAccount(null);
            for (RoomReservationLineItemEntity lineItem : reservation.getRoomReservationLineItems()) {
                em.detach(lineItem);
                em.detach(lineItem.getRoomTypeEntity());
                for (RoomEntity room : lineItem.getRoomTypeEntity().getRoomEntities()) {
                    em.detach(room);
                    room.setRoomType(null);
                }
            }

            if (!myReservations.isEmpty()) {
                for (RoomReservationEntity roomReservation : myReservations) {
                    result = result + roomReservation.toString() + "\n"
                            + "--------------------------------------------------------------------------------------------------" + "\n";

                }

            } else {
                
                result = "No reservation has been made.";
            }
        }
        return result;

    }

    @WebMethod(operationName = "viewReservationDetails")
    public String viewReservationDetails(@WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException {

        RoomReservationEntity reservation = roomReservationEntitySessionBean.viewReservationDetails(reservationId);
        String result = "";

        em.detach(reservation);

        reservation.setBookingAccount(null);
        for (RoomReservationLineItemEntity lineItem : reservation.getRoomReservationLineItems()) {
            em.detach(lineItem);
            em.detach(lineItem.getRoomTypeEntity());
            for (RoomEntity room : lineItem.getRoomTypeEntity().getRoomEntities()) {
                em.detach(room);
                room.setRoomType(null);
            }
        }

        if (reservation != null) {
            List<RoomReservationLineItemEntity> listOfRooms = reservation.getRoomReservationLineItems();
            for(RoomReservationLineItemEntity room: listOfRooms) {
                result = result + room.toString() + "\n" + "***" + "\n";
            }
        }

        return result;

    }

}
