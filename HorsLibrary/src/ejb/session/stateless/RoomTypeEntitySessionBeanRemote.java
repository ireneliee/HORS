/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author irene
 */
@Remote
public interface RoomTypeEntitySessionBeanRemote {
    
     public List<RoomTypeEntity> retrieveAllRoomType();
     
       public Long createRoomType(RoomTypeEntity newRoomType) throws RoomTypeExistException, UnknownPersistenceException;
       
       public RoomTypeEntity retrieveRoomType(String name) throws RoomTypeNotFoundException;
       
       public void deleteRoomType(String name) throws RoomTypeNotFoundException;
       
       public void updateRoomType(RoomTypeEntity roomType) throws RoomTypeNotFoundException;
    
}
