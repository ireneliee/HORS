/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import entity.EmployeeEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
dummy files for testing - loading data
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBean;

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

   @PostConstruct
   public void postConstruct() {
       if(em.find(EmployeeEntity.class, 1L) == null) {
           doDataInitialisation();
       }
   }
   
   private void doDataInitialisation(){
       try{
       EmployeeEntity employeeOne = new EmployeeEntity("Employee", "One", "employeeOne", "password",
       AccessRightEnum.SYSTEMADMINISTRATOR);
       employeeEntitySessionBean.createNewEmployee(employeeOne);
       
       EmployeeEntity employeeTwo = new EmployeeEntity("Employee", "Two", "employeeTwo", "password",
       AccessRightEnum.SYSTEMADMINISTRATOR);
       employeeEntitySessionBean.createNewEmployee(employeeTwo);
       } catch (UsernameExistException | UnknownPersistenceException ex) {
           System.out.println(ex.getMessage());
       }
       
   }

    
}
