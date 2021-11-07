/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import entity.RoomReservationEntity;
import entity.RoomReservationLineItemEntity;
import entity.UserEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.GuestHasNotCheckedInException;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.NoMoreRoomToAccomodateException;
import util.exception.WrongCheckInDate;
import util.exception.WrongCheckoutDate;

@Stateless
public class RoomReservationEntitySessionBean implements RoomReservationEntitySessionBeanRemote, RoomReservationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    public RoomReservationEntitySessionBean() {
    }

    // for one that needs user entity, does not include guest
    // payment must be added before -> then can create roomReservationEntity
    // roomReservationLineItemEntity must be added before too -> then can create room reservation entity
    @Override
    public Long createNewRoomReservationEntity(Long userId,
            RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException {

        if (newRoomReservationEntity == null) {

            throw new InvalidRoomReservationEntityException("Room reservation information not provided.");

        }
        UserEntity user = em.find(UserEntity.class, userId);

        user.getRoomReservations().add(newRoomReservationEntity);

        newRoomReservationEntity.setBookingAccount(user);

        em.persist(newRoomReservationEntity);

        return newRoomReservationEntity.getRoomReservationId();

    }

    @Override
    public Long createNewRoomReservationEntity(RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException {

        if (newRoomReservationEntity == null) {

            throw new InvalidRoomReservationEntityException("Room reservation information not provided.");

        }

        em.persist(newRoomReservationEntity);

        return newRoomReservationEntity.getRoomReservationId();

    }

    @Override
    public List<RoomEntity> checkIn(Long roomReservationId, LocalDate date) throws InvalidRoomReservationEntityException, WrongCheckInDate,
            NoMoreRoomToAccomodateException  {

        try {
            RoomReservationEntity roomReservation = em.find(RoomReservationEntity.class, roomReservationId);
            List<RoomReservationLineItemEntity> listOfRoomReservations = roomReservation.getRoomReservationLineItems();

            if (!listOfRoomReservations.get(0).getCheckInDate().equals(date)) {
                throw new WrongCheckInDate("You are not supposed to check in today!");
            }
            List<RoomEntity> roomsForCheckIn = new ArrayList<>();
            // checkIn = true
            for (RoomReservationLineItemEntity x : listOfRoomReservations) {
                   if(x.getRoomAllocation() != null) {
                       roomsForCheckIn.add(x.getRoomAllocation());
                       x.setCheckedIn(true);
                   } else {
                       String roomNumber = "";
                       for(int i = 0; i < roomsForCheckIn.size(); i++) {
                           roomNumber = roomNumber + roomsForCheckIn.get(i) + "\n";
                       }
                       throw new NoMoreRoomToAccomodateException("Currently, the hotel only has " + roomsForCheckIn.size() + " room(s)" + 
                               " to accomodate for the guest's reservation. The room(s) are \n" + roomNumber + "\nPlease handle this manually");
                   }
            }

            return roomsForCheckIn;

        } catch (NoResultException ex) {
            throw new InvalidRoomReservationEntityException("Room reservation does not exist.");
        }

    }

    @Override
    public void checkOut(Long roomReservationId, LocalDate date) throws WrongCheckoutDate, InvalidRoomReservationEntityException,
            GuestHasNotCheckedInException{
        try {
            RoomReservationEntity roomReservation = em.find(RoomReservationEntity.class, roomReservationId);
            List<RoomReservationLineItemEntity> listOfRoomReservations = roomReservation.getRoomReservationLineItems();
            // check if the guest has checked in
            if(!listOfRoomReservations.get(0).getCheckedIn()) {
                throw new GuestHasNotCheckedInException("Guest has not checked in!'");
            }
            if (!(listOfRoomReservations.get(0).getCheckoutDate().equals(date))) {
                System.out.println(listOfRoomReservations.get(0).getCheckoutDate());
                throw new WrongCheckoutDate("Checkout date need to be rechecked!");
            }

            // checkIn = true
            listOfRoomReservations
                    .stream()
                    .forEach(x -> x.setCheckedOut(true));

        } catch (NoResultException ex) {
            throw new InvalidRoomReservationEntityException("Room reservation does not exist.");
        }
    }
    
    public List<RoomReservationEntity> viewAllMyReservation(String username){
        
         String databaseQueryString = "SELECT rr FROM RoomReservationEntity rr WHERE rr.bookingAccount.name = :iName";
            Query query = em.createQuery(databaseQueryString);
            query.setParameter("iName", username);
            
            List<RoomReservationEntity> reservations = query.getResultList();
        
        return reservations;
    } 

}
