/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import entity.RoomReservationLineItemEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RoomStatusEnum;
import util.exception.RoomAllocationIsDoneException;

/**
 *
 * @author irene
 */
@Stateless
public class RoomAllocationSessionBean implements RoomAllocationSessionBeanRemote, RoomAllocationSessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    public RoomAllocationSessionBean() {
    }

    public void allocateRoomToday(LocalDate checkInDate) throws RoomAllocationIsDoneException{
        if(roomAllocationIsDone(checkInDate)) {
            throw new RoomAllocationIsDoneException("Room allocation for " + checkInDate.toString() + 
                    " has already been done.");
        }
        // room allocation must be done from level 1 to level 5
        
        

    }
    
    private Boolean roomAllocationIsDone(LocalDate checkInDate) {
        String databaseQueryInString = "SELECT rr FROM RoomReservationLineItemEntity rr " + 
                "WHERE rr.checkInDate = :iCheckInDate";
        Query databaseQuery = em.createQuery(databaseQueryInString);
        databaseQuery.setParameter("iCheckInDate", checkInDate);
        List<RoomReservationLineItemEntity> listOfRoomReservations = databaseQuery.getResultList();
        for(RoomReservationLineItemEntity roomReservation: listOfRoomReservations) {
            if(!(roomReservation.getRoomAllocation() == null)) {
                return true;
            }
        }
        
        return false;
    }
    
    
    
    private List<RoomEntity> retrieveListOfAvailableRoomsByType(RoomTypeEntity roomType) {
        String databaseQueryInString = "SELECT r FROM RoomEntity r WHERE r.roomType = :iRoomType "
                + "AND r.roomStatus = :iRoomStatus";
        Query databaseQuery = em.createQuery(databaseQueryInString);
        databaseQuery.setParameter("iRoomType", roomType);
        databaseQuery.setParameter("iRoomStatus", RoomStatusEnum.AVAILABLE);
        return databaseQuery.getResultList();
    }

    // retrieve room reservation that has been allocated that room
    // check if the checkIndate is before, and checkout date is after
    private List<RoomEntity> retrieveListOfAvailableRoom(RoomTypeEntity roomType,
            LocalDate checkInDate) {
        List<RoomEntity> listOfAvailableRoomThatDay = new ArrayList<>();
        List<RoomEntity> listOfAvailableRoom = retrieveListOfAvailableRoomsByType(roomType);
        for (RoomEntity room : listOfAvailableRoom) {
            // finding all the  reservations that are using a certain room
            String databaseQueryInString = "SELECT r FROM RoomReservationLineItemEntity r WHERE r.roomAllocation = :iRoomEntity";
            Query databaseQuery = em.createQuery(databaseQueryInString);
            databaseQuery.setParameter("iRoomEntity", room);

            //checking if there is any reservation using that room on the check in day
            List<RoomReservationLineItemEntity> listOfReservationUsingThatRoom = databaseQuery.getResultList();
            Boolean theRoomIsNotAvailable = listOfReservationUsingThatRoom
                                                                                            .stream()
                                                                                            .filter(x -> reservationIsUsingTheRoomOnThatDate(x,
                                                                                                    checkInDate))
                                                                                            .findAny()
                                                                                            .isPresent();
            if(!theRoomIsNotAvailable) listOfAvailableRoomThatDay.add(room);
        }
        return listOfAvailableRoomThatDay;
    }

    private Boolean reservationIsUsingTheRoomOnThatDate(RoomReservationLineItemEntity roomReservation,
            LocalDate checkInDate) {
        LocalDate checkInDateOfReservation = roomReservation.getCheckInDate();
        LocalDate checkOutDateOfReservation = roomReservation.getCheckoutDate();
        return (checkInDateOfReservation.isBefore(checkInDate) || checkInDateOfReservation.isEqual(checkInDate))
                && checkOutDateOfReservation.isAfter(checkInDate);
    }

}
