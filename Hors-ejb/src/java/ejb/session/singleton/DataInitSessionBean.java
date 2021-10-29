/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.GuestEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import entity.EmployeeEntity;
import entity.GuestEntity;
import entity.RoomTypeEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.exception.RoomTypeExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 * dummy files for testing - loading data
 * test
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBean;

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBean;

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBean;
    
    @EJB
    private GuestEntitySessionBeanLocal guestEntitySessionBean;

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;
    
    /*
    private RoomTypeEntity roomTypeOne;
    private RoomTypeEntity roomTypeTwo;
    private RoomTypeEntity roomTypeThree;
    private RoomTypeEntity roomTypeFour;
    private RoomTypeEntity roomTypeFive;
*/
    

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void postConstruct() {
        if (em.find(EmployeeEntity.class, 1L) == null) {
            doDataInitialisationEmployee();
        }

        if (em.find(RoomTypeEntity.class, 1L) == null) {
            doDataInitialisationRoomTypeEntity();
        }
               /*
        if(em.find(RoomEntity.class, 1L) == null) {
            doDataInitialisationRoomEntity();
        }
        */
        if (em.find(GuestEntity.class, 1L) == null) {
            doDataInitialisationGuestEntity();
        }       
        
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void doDataInitialisationRoomTypeEntity() {
        try {
            System.out.println("Reach A");
            RoomTypeEntity roomTypeOne = new RoomTypeEntity("Deluxe", "Basic room", "10x8", 2, 2, "wifi", 1);
            System.out.println("Reach B");
            roomTypeEntitySessionBean.createRoomType(roomTypeOne);
            System.out.println("Room type one");

            RoomTypeEntity roomTypeTwo = new RoomTypeEntity("Premium", "Better room", "12x8", 2, 2, "bathtub", 2);
            roomTypeEntitySessionBean.createRoomType(roomTypeTwo);
            System.out.println("Room type two");

            RoomTypeEntity roomTypeThree = new RoomTypeEntity("Premium plus", "Much better room", "12x10", 2, 3, "bathtub + sofa", 3);
            roomTypeEntitySessionBean.createRoomType(roomTypeThree);
            System.out.println("Room type three");

            RoomTypeEntity roomTypeFour = new RoomTypeEntity("Queen", "Much better room", "12x10", 3, 3, "bathtub + sofa", 4);
            roomTypeEntitySessionBean.createRoomType(roomTypeFour);
            System.out.println("Room type four");

           RoomTypeEntity roomTypeFive = new RoomTypeEntity("King", "Much better room", "12x14", 2, 3, "bathtub + sofa", 5);
            roomTypeEntitySessionBean.createRoomType(roomTypeFive);
            System.out.println("Room type five");

           

        } catch (RoomTypeExistException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }

    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void doDataInitialisationEmployee() {
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

            EmployeeEntity employeeFour = new EmployeeEntity("Employee", "Four", "employeeFour", "password",
                    AccessRightEnum.SALESMANAGER);
            employeeEntitySessionBean.createNewEmployee(employeeFour);

        } catch (UsernameExistException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /*
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void doDataInitialisationRoomEntity() {
         try {
                RoomEntity roomOne = new RoomEntity(2015, RoomStatusEnum.AVAILABLE, roomTypeOne);
                roomEntitySessionBean.createNewRoom(roomOne);
                System.out.println("Room one");
                
                RoomEntity roomTwo = new RoomEntity(3015, RoomStatusEnum.AVAILABLE, roomTypeTwo);
                roomEntitySessionBean.createNewRoom(roomTwo);
                System.out.println("Room two");
                
                RoomEntity roomThree = new RoomEntity(4015, RoomStatusEnum.AVAILABLE, roomTypeThree);
                roomEntitySessionBean.createNewRoom(roomThree);
                System.out.println("Room three");
                
                RoomEntity roomFour = new RoomEntity(5015, RoomStatusEnum.AVAILABLE, roomTypeFour);
                roomEntitySessionBean.createNewRoom(roomFour);
                System.out.println("Room four");
                
                RoomEntity roomFive = new RoomEntity(6015, RoomStatusEnum.NOTAVAILABLE, roomTypeFive);
                roomEntitySessionBean.createNewRoom(roomFive);
                System.out.println("Room five");
                
            } catch (RoomNumberExistException | UnknownPersistenceException | InputDataValidationException ex) {
                System.out.print(ex.getMessage());
            }
    }
*/
    
       
            
        @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void doDataInitialisationGuestEntity() {
            try {   
            
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
