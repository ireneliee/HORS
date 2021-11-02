/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomAllocationExceptionEntity;
import java.time.LocalDate;
import javax.ejb.Remote;
import util.exception.RoomAllocationExceptionReportDoesNotExistException;
import util.exception.RoomAllocationIsDoneException;

/**
 *
 * @author irene
 */
@Remote
public interface RoomAllocationSessionBeanRemote {
    
    public void allocateRoomGivenDate(LocalDate checkInDate) throws RoomAllocationIsDoneException;
   
    
    public RoomAllocationExceptionEntity retrieveReportByDate(LocalDate reportDate) throws RoomAllocationExceptionReportDoesNotExistException;
    
}
