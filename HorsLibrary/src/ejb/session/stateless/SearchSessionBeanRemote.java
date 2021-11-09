/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import util.exception.RateNotFoundException;
import util.reservation.Pair;

/**
 *
 * @author irene
 */
@Remote
public interface SearchSessionBeanRemote {
    
    public Map<RoomTypeEntity, Integer> findAvailableRoomTypes(LocalDate checkIn, LocalDate checkOut);
    
    public BigDecimal calculatePublishedRate(LocalDate checkIn, LocalDate checkOut, RoomTypeEntity roomType) throws RateNotFoundException;
    
    public BigDecimal calculateNonPublishedRate(LocalDate checkIn, LocalDate checkOut, RoomTypeEntity roomType) throws RateNotFoundException;
    
    public List<Pair> searchRoom(int reserveType, LocalDate checkinDate, LocalDate checkoutDate, Integer numberOfRooms);
    
}
