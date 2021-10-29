/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.NormalRateEntity;
import entity.PeakRateEntity;
import entity.PromotionRateEntity;
import entity.PublishedRateEntity;
import entity.RoomRateEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteRoomRateException;
import util.exception.NormalRateHasAlreadyExistedException;
import util.exception.PeakRateHasAlreadyExistedException;
import util.exception.PromotionRateHasAlreadyExistedException;
import util.exception.PublishedRateHasAlreadyExistedException;
import util.exception.RoomRateEntityNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author irene
 */
@Local
public interface RoomRateEntitySessionBeanLocal {

    public Long createNewPublishedRateEntity(PublishedRateEntity newPublishedRateEntity) throws PublishedRateHasAlreadyExistedException, UnknownPersistenceException;

    public Long createNewNormalRateEntity(NormalRateEntity newNormalRateEntity) throws NormalRateHasAlreadyExistedException, UnknownPersistenceException;

    public Long createNewPeakRateEntity(PeakRateEntity newPeakRateEntity) throws PeakRateHasAlreadyExistedException, UnknownPersistenceException;

    public Long createNewPromotionRateEntity(PromotionRateEntity newPromotionRateEntity) throws PromotionRateHasAlreadyExistedException, UnknownPersistenceException;

    public RoomRateEntity retrieveRoomRateDetails(Long roomRateId) throws RoomRateEntityNotFoundException;

    public void updatePublishedAndNormalRate(RoomRateEntity roomRate);
    
    public void updatePromotionAndPeakRate(RoomRateEntity roomRate);
    
    public List<RoomRateEntity> retrieveAllRoomRate();
    
    public void deleteRoomRateEntity(RoomRateEntity roomRate) throws DeleteRoomRateException;

   
    
}
