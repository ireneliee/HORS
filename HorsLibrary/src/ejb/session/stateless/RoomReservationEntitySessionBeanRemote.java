/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import entity.RoomReservationEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.WrongCheckInDate;
import util.exception.WrongCheckoutDate;

/**
 *
 * @author irene
 */
@Remote
public interface RoomReservationEntitySessionBeanRemote {

    public Long createNewRoomReservationEntity(Long userId, RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException;

    public Long createNewRoomReservationEntity(RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException;
    
    public List<RoomEntity> checkIn(Long roomReservationId, LocalDate date) throws InvalidRoomReservationEntityException, WrongCheckInDate;
    
    public void checkOut(Long roomReservationId, LocalDate date) throws WrongCheckoutDate, InvalidRoomReservationEntityException;

}
