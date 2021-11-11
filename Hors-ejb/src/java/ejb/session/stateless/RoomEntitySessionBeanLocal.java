/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.RoomNotFoundException;
import util.exception.RoomNumberExistException;
import util.exception.RoomTypeHasBeenDisabledException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomException;

/**
 *
 * @author irene
 */
@Local
public interface RoomEntitySessionBeanLocal {

    public Long createNewRoom(RoomEntity newRoomEntity) throws RoomNumberExistException,
            UnknownPersistenceException, InputDataValidationException, RoomTypeHasBeenDisabledException;
    
    public void updateRoom(RoomEntity roomEntity) throws RoomNotFoundException, 
            UpdateRoomException, InputDataValidationException;

    public List<RoomEntity> retrieveAllRooms();

    public Boolean checkIfTheRoomIsUsed(String roomNumber);
    
    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;

    public void deleteRoom(String roomNumber) throws RoomNotFoundException;

    public void createNewRoom(RoomEntity newRoomEntity,  List<RoomEntity> listOfRoomEntities);
    
}
