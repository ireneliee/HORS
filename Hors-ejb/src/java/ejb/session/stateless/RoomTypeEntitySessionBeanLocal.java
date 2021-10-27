/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomTypeExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author irene
 */
@Local
public interface RoomTypeEntitySessionBeanLocal {
    
    public List<RoomTypeEntity> retrieveAllRoomType();

    public Long createRoomType(RoomTypeEntity newRoomType) throws RoomTypeExistException, UnknownPersistenceException;
    
}
