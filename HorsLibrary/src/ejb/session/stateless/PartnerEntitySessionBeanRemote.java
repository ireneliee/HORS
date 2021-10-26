/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author irene
 */
@Remote
public interface PartnerEntitySessionBeanRemote {
    
    public Long createNewPartner(PartnerEntity newPartnerEntity) throws UsernameExistException, UnknownPersistenceException;
    
     public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException;
     
      public PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException;
    
}
