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
import javax.ejb.Remote;
import util.exception.NormalRateHasAlreadyExistedException;
import util.exception.PeakRateHasAlreadyExistedException;
import util.exception.PromotionRateHasAlreadyExistedException;
import util.exception.PublishedRateHasAlreadyExistedException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author irene
 */
@Remote
public interface RoomRateEntitySessionBeanRemote {
    
     public Long createNewPublishedRateEntity(PublishedRateEntity newPublishedRateEntity) throws PublishedRateHasAlreadyExistedException, 
             UnknownPersistenceException;
     
     public Long createNewNormalRateEntity(NormalRateEntity newNormalRateEntity) throws NormalRateHasAlreadyExistedException, UnknownPersistenceException;
     
     public Long createNewPeakRateEntity(PeakRateEntity newPeakRateEntity) throws PeakRateHasAlreadyExistedException, UnknownPersistenceException;
     
      public Long createNewPromotionRateEntity(PromotionRateEntity newPromotionRateEntity) throws PromotionRateHasAlreadyExistedException, UnknownPersistenceException;
    
}
