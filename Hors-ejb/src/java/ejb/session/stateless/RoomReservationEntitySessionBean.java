/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomReservationEntity;
import entity.UserEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidRoomReservationEntityException;


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
    
    public Long createNewRoomReservationEntity(RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException {
        
        if(newRoomReservationEntity == null) {
            
            throw new InvalidRoomReservationEntityException("Room reservation information not provided.");
            
        }

        
        em.persist(newRoomReservationEntity);
        
        return newRoomReservationEntity.getRoomReservationId();

    }

    
}
