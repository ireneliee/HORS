/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author irene
 */
@Local
public interface PartnerEntitySessionBeanLocal {

    public Long createNewPartner(PartnerEntity newPartnerEntity) throws UsernameExistException, UnknownPersistenceException;

    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException;

    public PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException;

    public List<PartnerEntity> retrieveAllPartner();
    
}
