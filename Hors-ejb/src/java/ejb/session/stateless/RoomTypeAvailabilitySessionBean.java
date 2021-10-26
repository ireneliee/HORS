
package ejb.session.stateless;

import entity.RoomTypeAvailability;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.UnknownPersistenceException;

@Stateless
public class RoomTypeAvailabilitySessionBean implements RoomTypeAvailabilitySessionBeanRemote, RoomTypeAvailabilitySessionBeanLocal {

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    public RoomTypeAvailabilitySessionBean(){}
    
    @Override
    public Long createRoomTypeAvailability(RoomTypeAvailability newRoomTypeAvailability)
            throws UnknownPersistenceException{
           try
        {
            em.persist(newRoomTypeAvailability);
            em.flush();

            return newRoomTypeAvailability.getRoomTypeAvailabilityId();
        }
        catch(PersistenceException ex)
        {
                throw new UnknownPersistenceException(ex.getMessage());
        }
    }
    
    @Override
    public Map<RoomTypeEntity, Integer> searchHotelRoom(LocalDate checkIn, LocalDate checkOut) {
        Map<RoomTypeEntity, Integer> searchHotelResult = new HashMap<>();
        
        List<RoomTypeEntity> allRoomTypes = roomTypeEntitySessionBeanLocal.retrieveAllRoomType();
        allRoomTypes
                .stream()
                .forEach(x -> searchHotelResult.put(x, 0));
        
        allRoomTypes
                .stream()
                .forEach(x -> 
                        updateSearchHotelResultWithNumberOfAvailableRoom(checkIn, checkOut, x, searchHotelResult));
        
 
       return searchHotelResult;
    }
    
    // query might be problematic need to be checked
    private void updateSearchHotelResultWithNumberOfAvailableRoom(LocalDate checkIn, LocalDate checkOut, 
            RoomTypeEntity roomType, Map<RoomTypeEntity, Integer> searchHotelResult) {
        
        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {

            String databaseQueryString = "SELECT s FROM RoomTypeAvailability s WHERE s.date = :inDate AND "
                    + "s.roomType = :inRoomType";

            Query query = em.createQuery(databaseQueryString);

            query.setParameter("inDate", date);
            query.setParameter("inRoomType", roomType);

            RoomTypeAvailability availableRoomType = (RoomTypeAvailability) query.getSingleResult();

            Integer numberOfRoomsForTheRoomType = availableRoomType.getNoOfAvailableRoom();

            Integer minNumberOfRooms = Math.min(searchHotelResult.get(roomType), numberOfRoomsForTheRoomType);

            searchHotelResult.replace(roomType, minNumberOfRooms);
            }
    }
    
    

    

}
