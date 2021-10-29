/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.GuestEntitySessionBeanLocal;
import entity.EmployeeEntity;
import entity.GuestEntity;
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
 * dummy files for testing - loading data
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBean;
    private GuestEntitySessionBeanLocal guestEntitySessionBean;

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        if (em.find(EmployeeEntity.class, 1L) == null && em.find(GuestEntity.class, 1L) == null) {
            doDataInitialisation();
        }
    }

    private void doDataInitialisation() {
        try {
            EmployeeEntity employeeOne = new EmployeeEntity("Employee", "One", "employeeOne", "password",
                    AccessRightEnum.SYSTEMADMINISTRATOR);
            employeeEntitySessionBean.createNewEmployee(employeeOne);

            EmployeeEntity employeeTwo = new EmployeeEntity("Employee", "Two", "employeeTwo", "password",
                    AccessRightEnum.SYSTEMADMINISTRATOR);
            employeeEntitySessionBean.createNewEmployee(employeeTwo);
            
            EmployeeEntity employeeThree = new EmployeeEntity("Employee", "Three", "employeeThree", "password",
                    AccessRightEnum.OPERATIONMANAGER);
            employeeEntitySessionBean.createNewEmployee(employeeThree);
            
            
            
            //Create guest
            GuestEntity guestOne = new GuestEntity("Guest","One", "guestOne","password", "email1", "11111","1111");
            guestEntitySessionBean.guestRegister(guestOne);
            
            GuestEntity guestTwo = new GuestEntity("Guest","Two", "guestTwo","password", "email2", "22222","2222");
            guestEntitySessionBean.guestRegister(guestOne);
            
            GuestEntity guestThree = new GuestEntity("Guest","Three", "guestThree","password", "email3", "33333","3333");
            guestEntitySessionBean.guestRegister(guestThree);
        } catch (UsernameExistException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
        
