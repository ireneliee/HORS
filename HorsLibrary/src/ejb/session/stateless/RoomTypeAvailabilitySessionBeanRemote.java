/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypeAvailability;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.Map;
import javax.ejb.Remote;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author irene
 */
@Remote
public interface RoomTypeAvailabilitySessionBeanRemote {
    
    public Long createRoomTypeAvailability(RoomTypeAvailability newRoomTypeAvailability) throws UnknownPersistenceException;
    
      public Map<RoomTypeEntity, Integer> searchHotelRoom(LocalDate checkIn, LocalDate checkOut);
    
}
