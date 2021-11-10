/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.GuestEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomRateEntitySessionBeanLocal;
import ejb.session.stateless.RoomReservationEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import entity.EmployeeEntity;
import entity.GuestEntity;
import entity.NormalRateEntity;
import entity.PeakRateEntity;
import entity.PromotionRateEntity;
import entity.PublishedRateEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.InputDataValidationException;
import util.exception.NormalRateHasAlreadyExistedException;
import util.exception.PeakRateHasAlreadyExistedException;
import util.exception.PromotionRateHasAlreadyExistedException;
import util.exception.PublishedRateHasAlreadyExistedException;
import util.exception.RoomNumberExistException;
import util.exception.RoomTypeExistException;
import util.exception.RoomTypeHasBeenDisabledException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 * dummy files for testing - loading data test
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private RoomRateEntitySessionBeanLocal roomRateEntitySessionBean;

    @EJB
    private RoomReservationEntitySessionBeanLocal roomReservationEntitySessionBean;

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

    @PostConstruct
    public void postConstruct() {
        if (em.find(EmployeeEntity.class, 1L) == null) {
            doDataInitialisationEmployee();
        }

        if (em.find(RoomTypeEntity.class, 1L) == null) {
            doDataInitialisationRoomTypeEntity();
        }
        
         
    }

    private void doDataInitialisationRoomTypeEntity() {
        // roomType creation
        RoomTypeEntity roomTypeOne = new RoomTypeEntity("Deluxe Room", "Ocean view with normal amenities", "10x8", 2, 2, "shower, TV, sofa", 1);

        RoomEntity roomOne = new RoomEntity("0101", RoomStatusEnum.AVAILABLE);
        RoomEntity roomTwo = new RoomEntity("0201", RoomStatusEnum.AVAILABLE);
        RoomEntity roomThree = new RoomEntity("0301", RoomStatusEnum.AVAILABLE);
        RoomEntity roomFour = new RoomEntity("0401", RoomStatusEnum.AVAILABLE);
        RoomEntity roomFive = new RoomEntity("0501", RoomStatusEnum.AVAILABLE);

        roomOne.setRoomType(roomTypeOne);
        roomTwo.setRoomType(roomTypeOne);
        roomThree.setRoomType(roomTypeOne);
        roomFour.setRoomType(roomTypeOne);
        roomFive.setRoomType(roomTypeOne);

        roomTypeOne.getRoomEntities().add(roomOne);
        roomTypeOne.getRoomEntities().add(roomTwo);
        roomTypeOne.getRoomEntities().add(roomThree);
        roomTypeOne.getRoomEntities().add(roomFour);
        roomTypeOne.getRoomEntities().add(roomFive);

        RoomTypeEntity roomTypeTwo = new RoomTypeEntity("Premier Room", "City view with luxurious amenities", "12x8", 2, 2, "bathtub, TV, shower, sofa", 2);

        RoomEntity roomSix = new RoomEntity("0102", RoomStatusEnum.AVAILABLE);
        RoomEntity roomSeven = new RoomEntity("0202", RoomStatusEnum.AVAILABLE);
        RoomEntity roomEight = new RoomEntity("0302", RoomStatusEnum.AVAILABLE);
        RoomEntity roomNine = new RoomEntity("0402", RoomStatusEnum.AVAILABLE);
        RoomEntity roomTen = new RoomEntity("0502", RoomStatusEnum.AVAILABLE);

        roomSix.setRoomType(roomTypeTwo);
        roomSeven.setRoomType(roomTypeTwo);
        roomEight.setRoomType(roomTypeTwo);
        roomNine.setRoomType(roomTypeTwo);
        roomTen.setRoomType(roomTypeTwo);

        roomTypeTwo.getRoomEntities().add(roomSix);
        roomTypeTwo.getRoomEntities().add(roomSeven);
        roomTypeTwo.getRoomEntities().add(roomEight);
        roomTypeTwo.getRoomEntities().add(roomNine);
        roomTypeTwo.getRoomEntities().add(roomTen);

        RoomTypeEntity roomTypeThree = new RoomTypeEntity("Family Room", "Room suitable for a family", "14x12", 4, 4, "personal jacuzzi and a big sofa ", 3);

        RoomEntity roomEleven = new RoomEntity("0103", RoomStatusEnum.AVAILABLE);
        RoomEntity roomTwelve = new RoomEntity("0203", RoomStatusEnum.AVAILABLE);
        RoomEntity roomThirteen = new RoomEntity("0303", RoomStatusEnum.AVAILABLE);
        RoomEntity roomFourteen = new RoomEntity("0403", RoomStatusEnum.AVAILABLE);
        RoomEntity roomFifteen = new RoomEntity("0503", RoomStatusEnum.AVAILABLE);

        roomEleven.setRoomType(roomTypeThree);
        roomTwelve.setRoomType(roomTypeThree);
        roomThirteen.setRoomType(roomTypeThree);
        roomFourteen.setRoomType(roomTypeThree);
        roomFifteen.setRoomType(roomTypeThree);

        roomTypeThree.getRoomEntities().add(roomEleven);
        roomTypeThree.getRoomEntities().add(roomTwelve);
        roomTypeThree.getRoomEntities().add(roomThirteen);
        roomTypeThree.getRoomEntities().add(roomFourteen);
        roomTypeThree.getRoomEntities().add(roomFifteen);

        RoomTypeEntity roomTypeFour = new RoomTypeEntity("Junior Suite", "Extra luxurious room with highest quality amenities imported from Italy", "18x16", 2, 2, "bathtub, sofa, personal jacuzzi and big bed", 4);

        RoomEntity roomSixteen = new RoomEntity("0104", RoomStatusEnum.AVAILABLE);
        RoomEntity roomSeventeen = new RoomEntity("0204", RoomStatusEnum.AVAILABLE);
        RoomEntity roomEighteen = new RoomEntity("0304", RoomStatusEnum.AVAILABLE);
        RoomEntity roomNineteen = new RoomEntity("0404", RoomStatusEnum.AVAILABLE);
        RoomEntity roomTwenty = new RoomEntity("0504", RoomStatusEnum.AVAILABLE);

        roomSixteen.setRoomType(roomTypeFour);
        roomSeventeen.setRoomType(roomTypeFour);
        roomEighteen.setRoomType(roomTypeFour);
        roomNineteen.setRoomType(roomTypeFour);
        roomTwenty.setRoomType(roomTypeFour);

        roomTypeFour.getRoomEntities().add(roomSixteen);
        roomTypeFour.getRoomEntities().add(roomSeventeen);
        roomTypeFour.getRoomEntities().add(roomEighteen);
        roomTypeFour.getRoomEntities().add(roomNineteen);
        roomTypeFour.getRoomEntities().add(roomTwenty);

        RoomTypeEntity roomTypeFive = new RoomTypeEntity("Grand Suite", "A room with everything you ever dream of", "12x14", 2, 2, "personal swimming pool and sauna, free breakfast, dining room, and kitchen", 5);

        RoomEntity roomTwentyOne = new RoomEntity("0105", RoomStatusEnum.AVAILABLE);
        RoomEntity roomTwentyTwo = new RoomEntity("0205", RoomStatusEnum.AVAILABLE);
        RoomEntity roomTwentyThree = new RoomEntity("0305", RoomStatusEnum.AVAILABLE);
        RoomEntity roomTwentyFour = new RoomEntity("0405", RoomStatusEnum.AVAILABLE);
        RoomEntity roomTwentyFive = new RoomEntity("0505", RoomStatusEnum.AVAILABLE);

        roomTwentyOne.setRoomType(roomTypeFive);
        roomTwentyTwo.setRoomType(roomTypeFive);
        roomTwentyThree.setRoomType(roomTypeFive);
        roomTwentyFour.setRoomType(roomTypeFive);
        roomTwentyFive.setRoomType(roomTypeFive);

        roomTypeFive.getRoomEntities().add(roomTwentyOne);
        roomTypeFive.getRoomEntities().add(roomTwentyTwo);
        roomTypeFive.getRoomEntities().add(roomTwentyThree);
        roomTypeFive.getRoomEntities().add(roomTwentyFour);
        roomTypeFive.getRoomEntities().add(roomTwentyFive);

        PublishedRateEntity rateOne = new PublishedRateEntity("DELUXE ROOM PUBLISHED RATE", new BigDecimal(100));
        NormalRateEntity rateTwo = new NormalRateEntity("DELUXE ROOM NORMAL RATE", new BigDecimal(50));
        
        PublishedRateEntity rateThree = new PublishedRateEntity("PREMIERE ROOM PUBLISHED RATE", new BigDecimal(200));
        NormalRateEntity rateFour = new NormalRateEntity("PREMIERE ROOM NORMAL RATE", new BigDecimal(100));

        PublishedRateEntity rateFive = new PublishedRateEntity("FAMILY ROOM PUBLISHED RATE", new BigDecimal(300));
        NormalRateEntity rateSix = new NormalRateEntity("FAMILY ROOM NORMAL RATE", new BigDecimal(150));

        PublishedRateEntity rateSeven = new PublishedRateEntity("JUNIOR SUITE ROOM PUBLISHED RATE", new BigDecimal(400));
        NormalRateEntity rateEight = new NormalRateEntity("JUNIOR SUITE ROOM NORMAL RATE", new BigDecimal(200));

        PublishedRateEntity rateNine = new PublishedRateEntity("GRAND SUITE ROOM PUBLISHED RATE", new BigDecimal(500));
        NormalRateEntity rateTen = new NormalRateEntity("GRAND SUITE ROOM NORMAL RATE", new BigDecimal(250));

        PeakRateEntity rateEleven = new PeakRateEntity("DELUXE ROOM PEAK RATE", LocalDate.of(2021, 12, 24),LocalDate.of(2021, 12, 27), new BigDecimal(150), roomTypeOne);
        PromotionRateEntity rateTwelve = new PromotionRateEntity("DELUXE ROOM PROMOTION RATE", LocalDate.of(2021, 12, 27),LocalDate.of(2021, 12, 29), new BigDecimal(40), roomTypeOne);
        
        PeakRateEntity rateThir = new PeakRateEntity("PREMIER ROOM PEAK RATE", LocalDate.of(2021, 12, 24),LocalDate.of(2021, 12, 27), new BigDecimal(250), roomTypeTwo);
        PromotionRateEntity rateFourt = new PromotionRateEntity("PREMIER ROOM PROMOTION RATE", LocalDate.of(2021, 12, 27),LocalDate.of(2021, 12, 29), new BigDecimal(50), roomTypeTwo);
        
        PeakRateEntity rateFift = new PeakRateEntity("FAMILY ROOM PEAK RATE", LocalDate.of(2021, 12, 24),LocalDate.of(2021, 12, 27), new BigDecimal(350), roomTypeThree);
        PromotionRateEntity rateSixt = new PromotionRateEntity("FAMILY ROOM PROMOTION RATE", LocalDate.of(2021, 12, 27),LocalDate.of(2021, 12, 29), new BigDecimal(100), roomTypeThree);
        
        PeakRateEntity rateSevent = new PeakRateEntity("JUNIOR SUITE  ROOM PEAK RATE", LocalDate.of(2021, 12, 24),LocalDate.of(2021, 12, 27), new BigDecimal(450), roomTypeFour);
        PromotionRateEntity rateEighte = new PromotionRateEntity("JUNIOR SUITE  ROOM PROMOTION RATE", LocalDate.of(2021, 12, 27),LocalDate.of(2021, 12, 29), new BigDecimal(150), roomTypeFour);
        
        PeakRateEntity rateNinet = new PeakRateEntity("GRAND SUITE PEAK RATE", LocalDate.of(2021, 12, 24),LocalDate.of(2021, 12, 27), new BigDecimal(550), roomTypeFive);
        PromotionRateEntity rateTwen = new PromotionRateEntity("GRAND SUITE PROMOTION RATE", LocalDate.of(2021, 12, 27),LocalDate.of(2021, 12, 29), new BigDecimal(200), roomTypeFive);
        
        rateOne.setRoomType(roomTypeOne);
        rateTwo.setRoomType(roomTypeOne);
        rateThree.setRoomType(roomTypeTwo);
        rateFour.setRoomType(roomTypeTwo);
        rateFive.setRoomType(roomTypeThree);
        rateSix.setRoomType(roomTypeThree);
        rateSeven.setRoomType(roomTypeFour);
        rateEight.setRoomType(roomTypeFour);
        rateNine.setRoomType(roomTypeFive);
        rateTen.setRoomType(roomTypeFive);
        
        rateEleven.setRoomType(roomTypeOne);
        rateTwelve.setRoomType(roomTypeOne);
        rateThir.setRoomType(roomTypeTwo);
        rateFourt.setRoomType(roomTypeTwo);
        rateFift.setRoomType(roomTypeThree);
        rateSixt.setRoomType(roomTypeThree);
        rateSevent.setRoomType(roomTypeFour);
        rateEighte.setRoomType(roomTypeFour);
        rateNinet.setRoomType(roomTypeFive);
        rateTwen.setRoomType(roomTypeFive);
        try {
            roomTypeEntitySessionBean.createRoomType(roomTypeOne);
            roomTypeEntitySessionBean.createRoomType(roomTypeTwo);
            roomTypeEntitySessionBean.createRoomType(roomTypeThree);
            roomTypeEntitySessionBean.createRoomType(roomTypeFour);
            roomTypeEntitySessionBean.createRoomType(roomTypeFive);
            System.out.println("done A");
            roomEntitySessionBean.createNewRoom(roomOne);
            roomEntitySessionBean.createNewRoom(roomTwo);
            roomEntitySessionBean.createNewRoom(roomThree);
            roomEntitySessionBean.createNewRoom(roomFour);
            roomEntitySessionBean.createNewRoom(roomFive);
            roomEntitySessionBean.createNewRoom(roomSix);
            roomEntitySessionBean.createNewRoom(roomSeven);
            roomEntitySessionBean.createNewRoom(roomEight);
            roomEntitySessionBean.createNewRoom(roomNine);
            roomEntitySessionBean.createNewRoom(roomTen);
            roomEntitySessionBean.createNewRoom(roomEleven);
            roomEntitySessionBean.createNewRoom(roomTwelve);
            roomEntitySessionBean.createNewRoom(roomThirteen);
            roomEntitySessionBean.createNewRoom(roomFourteen);
            roomEntitySessionBean.createNewRoom(roomFifteen);
            roomEntitySessionBean.createNewRoom(roomSixteen);
            roomEntitySessionBean.createNewRoom(roomSeventeen);
            roomEntitySessionBean.createNewRoom(roomEighteen);
            roomEntitySessionBean.createNewRoom(roomNineteen);
            roomEntitySessionBean.createNewRoom(roomTwenty);
            roomEntitySessionBean.createNewRoom(roomTwentyOne);
            roomEntitySessionBean.createNewRoom(roomTwentyTwo);
            roomEntitySessionBean.createNewRoom(roomTwentyThree);
            roomEntitySessionBean.createNewRoom(roomTwentyFour);
            roomEntitySessionBean.createNewRoom(roomTwentyFive);
            System.out.println("done B");
            roomRateEntitySessionBean.createNewPublishedRateEntity(rateOne);
            roomRateEntitySessionBean.createNewPublishedRateEntity(rateThree);
            roomRateEntitySessionBean.createNewPublishedRateEntity(rateFive);
            roomRateEntitySessionBean.createNewPublishedRateEntity(rateSeven);
            roomRateEntitySessionBean.createNewPublishedRateEntity(rateNine);
            System.out.println("done C");
            roomRateEntitySessionBean.createNewNormalRateEntity(rateTwo);
            roomRateEntitySessionBean.createNewNormalRateEntity(rateFour);
            roomRateEntitySessionBean.createNewNormalRateEntity(rateSix);
            roomRateEntitySessionBean.createNewNormalRateEntity(rateEight);
            roomRateEntitySessionBean.createNewNormalRateEntity(rateTen);
            System.out.println("done D");
            roomRateEntitySessionBean.createNewPeakRateEntity(rateEleven);
            roomRateEntitySessionBean.createNewPeakRateEntity(rateThir);
            roomRateEntitySessionBean.createNewPeakRateEntity(rateFift);
            roomRateEntitySessionBean.createNewPeakRateEntity(rateSevent);
            roomRateEntitySessionBean.createNewPeakRateEntity(rateNinet);
            
            roomRateEntitySessionBean.createNewPromotionRateEntity(rateTwelve);
            roomRateEntitySessionBean.createNewPromotionRateEntity(rateFourt);
            roomRateEntitySessionBean.createNewPromotionRateEntity(rateSixt);
            roomRateEntitySessionBean.createNewPromotionRateEntity(rateEighte);
            roomRateEntitySessionBean.createNewPromotionRateEntity(rateTwen);
            
        } catch (UnknownPersistenceException | RoomTypeExistException | PublishedRateHasAlreadyExistedException
                | RoomNumberExistException | InputDataValidationException | NormalRateHasAlreadyExistedException 
                | PeakRateHasAlreadyExistedException | PromotionRateHasAlreadyExistedException | RoomTypeHasBeenDisabledException ex) {
            System.out.println(ex.getMessage());

        }
    }

    private void doDataInitialisationEmployee() {
        try {
            EmployeeEntity employeeOne = new EmployeeEntity("Employee", "One", "sysadmin", "password",
                    AccessRightEnum.SYSTEMADMINISTRATOR);
            employeeEntitySessionBean.createNewEmployee(employeeOne);

            EmployeeEntity employeeThree = new EmployeeEntity("Employee", "Three", "opmanager", "password",
                    AccessRightEnum.OPERATIONMANAGER);
            employeeEntitySessionBean.createNewEmployee(employeeThree);

            EmployeeEntity employeeFour = new EmployeeEntity("Employee", "Four", "salesmanager", "password",
                    AccessRightEnum.SALESMANAGER);
            employeeEntitySessionBean.createNewEmployee(employeeFour);

            EmployeeEntity employeeFive = new EmployeeEntity("Employee", "Five", "guestrelo", "password",
                    AccessRightEnum.GUESTRELATIONOFFICER);
            employeeEntitySessionBean.createNewEmployee(employeeFive);
            System.out.println("employee... done!");

        } catch (UsernameExistException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
     // room reservation created. 4 deluxe room, 2 premium room, all in the same check in and check out date :)
            // reservation 1
            /*
            RoomReservationLineItemEntity roomOne = new RoomReservationLineItemEntity(roomTypeOne, new BigDecimal(100000),
                    LocalDate.of(2021, 11, 3), LocalDate.of(2021, 11, 6));

            RoomReservationLineItemEntity roomTwo = new RoomReservationLineItemEntity(roomTypeOne, new BigDecimal(100000),
                    LocalDate.of(2021, 11, 3), LocalDate.of(2021, 11, 6));

            RoomReservationEntity roomReservationOne = new RoomReservationEntity(new BigDecimal(200000), LocalDate.of(2021, 10, 10));
            roomReservationOne.getRoomReservationLineItems().add(roomOne);
            roomReservationOne.getRoomReservationLineItems().add(roomTwo);

            // reservation 2
            RoomReservationLineItemEntity roomThree = new RoomReservationLineItemEntity(roomTypeOne, new BigDecimal(100000),
                    LocalDate.of(2021, 11, 10), LocalDate.of(2021, 11, 16));

            RoomReservationLineItemEntity roomFour = new RoomReservationLineItemEntity(roomTypeOne, new BigDecimal(100000),
                    LocalDate.of(2021, 11, 10), LocalDate.of(2021, 11, 16));

            RoomReservationEntity roomReservationTwo = new RoomReservationEntity(new BigDecimal(200000), LocalDate.of(2021, 10, 10));
            roomReservationTwo.getRoomReservationLineItems().add(roomThree);
            roomReservationTwo.getRoomReservationLineItems().add(roomFour);

            // reservation 3
            RoomReservationLineItemEntity roomFive = new RoomReservationLineItemEntity(roomTypeTwo, new BigDecimal(100000),
                    LocalDate.of(2021, 11, 10), LocalDate.of(2021, 11, 16));

            RoomReservationLineItemEntity roomSix = new RoomReservationLineItemEntity(roomTypeTwo, new BigDecimal(100000),
                    LocalDate.of(2021, 11, 10), LocalDate.of(2021, 11, 16));

            RoomReservationEntity roomReservationThree = new RoomReservationEntity(new BigDecimal(200000), LocalDate.of(2021, 10, 10));
            roomReservationThree.getRoomReservationLineItems().add(roomFive);
            roomReservationThree.getRoomReservationLineItems().add(roomSix);

    }*/
