/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomAllocationExceptionEntity;
import entity.RoomReservationEntity;
import java.time.LocalDate;
import javax.ejb.Local;
import util.exception.RoomAllocationExceptionReportDoesNotExistException;
import util.exception.RoomAllocationIsDoneException;

/**
 *
 * @author irene
 */
@Local
public interface RoomAllocationSessionBeanLocal {

    public void allocateRoomGivenDate(LocalDate checkInDate) throws RoomAllocationIsDoneException;

    public RoomAllocationExceptionEntity retrieveReportByDate(LocalDate reportDate) throws RoomAllocationExceptionReportDoesNotExistException;

    public void allocateRoomNow(RoomReservationEntity roomReservation);
    
}
