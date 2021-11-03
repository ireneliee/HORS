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
import util.exception.InvalidRoomReservationEntityException;
import util.exception.WrongCheckInDate;
import util.exception.WrongCheckoutDate;


@Stateless
public class RoomReservationEntitySessionBean implements RoomReservationEntitySessionBeanRemote, RoomReservationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    public RoomReservationEntitySessionBean(){}
    
    // for one that needs user entity, does not include guest
    // payment must be added before -> then can create roomReservationEntity
    // roomReservationLineItemEntity must be added before too -> then can create room reservation entity
    @Override
    public Long createNewRoomReservationEntity(Long userId,
            RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException {
        
        if(newRoomReservationEntity == null) {
            
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
        
        if(newRoomReservationEntity == null) {
            
            throw new InvalidRoomReservationEntityException("Room reservation information not provided.");
            
        }

        
        em.persist(newRoomReservationEntity);
        
        return newRoomReservationEntity.getRoomReservationId();

    }
    
    @Override
    public List <RoomEntity> checkIn(Long roomReservationId, LocalDate date) throws InvalidRoomReservationEntityException, WrongCheckInDate {
   
        try{
            RoomReservationEntity roomReservation = em.find(RoomReservationEntity.class, roomReservationId);
            List<RoomReservationLineItemEntity> listOfRoomReservations = roomReservation.getRoomReservationLineItems();
            
            if(!listOfRoomReservations.get(0).getCheckInDate().equals(date)) {
                throw new WrongCheckInDate("You are not supposed to check in today!");
            }
            List<RoomEntity> roomsForCheckIn = new ArrayList<>();
            // checkIn = true
            listOfRoomReservations
                    .stream()
                    .forEach(x -> x.setCheckedIn(true));
            
            
            listOfRoomReservations
                    .stream()
                    .forEach(x -> roomsForCheckIn.add(x.getRoomAllocation()));
            return roomsForCheckIn;
            
           
        } catch(NoResultException ex) {
            throw new InvalidRoomReservationEntityException("Room reservation does not exist.");
        }
        
        
        
       
    }
    
    @Override
    public void checkOut(Long roomReservationId, LocalDate date) throws WrongCheckoutDate, InvalidRoomReservationEntityException {
        try{
            RoomReservationEntity roomReservation = em.find(RoomReservationEntity.class, roomReservationId);
            List<RoomReservationLineItemEntity> listOfRoomReservations = roomReservation.getRoomReservationLineItems();
            
            if(!(listOfRoomReservations.get(0).getCheckoutDate().equals(date))) {
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
   
    

    

    
}
