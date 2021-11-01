/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomReservationEntity;
import javax.ejb.Local;
import util.exception.InvalidRoomReservationEntityException;

/**
 *
 * @author irene
 */
@Local
public interface RoomReservationEntitySessionBeanLocal {

    public Long createNewRoomReservationEntity(Long userId, RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException;

    public Long createNewRoomReservationEntity(RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException;
    
}
