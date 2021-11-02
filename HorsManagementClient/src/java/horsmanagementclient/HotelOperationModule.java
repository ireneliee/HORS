/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.HorsManagementControllerSessionBeanRemote;
import entity.EmployeeEntity;
import entity.NormalRateEntity;
import entity.PeakRateEntity;
import entity.PromotionRateEntity;
import entity.PublishedRateEntity;
import entity.RoomAllocationExceptionEntity;
import entity.RoomEntity;
import entity.RoomRateEntity;
import entity.RoomReservationLineItemEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.DeleteRoomRateException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.NormalRateHasAlreadyExistedException;
import util.exception.PeakRateHasAlreadyExistedException;
import util.exception.PromotionRateHasAlreadyExistedException;
import util.exception.PublishedRateHasAlreadyExistedException;
import util.exception.RoomAllocationExceptionReportDoesNotExistException;
import util.exception.RoomAllocationIsDoneException;
import util.exception.RoomNotFoundException;
import util.exception.RoomNumberExistException;
import util.exception.RoomRateEntityNotFoundException;
import util.exception.RoomTypeExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomException;

public class HotelOperationModule {

    private HorsManagementControllerSessionBeanRemote horsManagementControllerSessionBeanRemote;
    private EmployeeEntity currentEmployee;
    //private final ValidatorFactory validatorFactory;
    //private final Validator validator;

    public HotelOperationModule() {
        //validatorFactory = Validation.buildDefaultValidatorFactory();
        //validator = validatorFactory.getValidator();
    }

    public HotelOperationModule(HorsManagementControllerSessionBeanRemote horsManagementControllerSessionBeanRemote,
            EmployeeEntity currentEmployee) {
        this();
        this.horsManagementControllerSessionBeanRemote = horsManagementControllerSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void operationManagerMenu() throws InvalidAccessRightException {

        if (currentEmployee.getAccessRight() != AccessRightEnum.OPERATIONMANAGER) {
            String errorMessage = "You don't have OPERATIONMANAGER rights to access the system"
                    + " hotel operation module. ";
            throw new InvalidAccessRightException(errorMessage);
        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager ***\n");
            System.out.println("1: Create new room type");
            System.out.println("2: View room type details");
            System.out.println("3: View all room types");
            System.out.println("4: Create new room");
            System.out.println("5: Update room");
            System.out.println("6: Delete room");
            System.out.println("7: View all rooms");
            System.out.println("8: View room allocation exception report");
            System.out.println("9: Allocate room now");
            System.out.println("10: Back\n");
            response = 0;

            while (response < 1 || response > 10) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    doCreateNewRoomType();

                } else if (response == 2) {

                    doViewRoomTypeDetails();

                } else if (response == 3) {

                    doViewAllRoomTypes();

                } else if (response == 4) {

                    doCreateNewRoom();

                } else if (response == 5) {

                    doUpdateRoom();

                } else if (response == 6) {

                    doDeleteRoom();

                } else if (response == 7) {

                    doRetrieveAllRooms();

                } else if (response == 8) {

                    doViewRoomAllocationExceptionReport();

                } else if (response == 9) {

                    doAllocateRoomNow();

                } else if (response == 10) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 10) {
                break;
            }
        }
    }

    public void salesManagerMenu() throws InvalidAccessRightException {

        if (currentEmployee.getAccessRight() != AccessRightEnum.SALESMANAGER) {
            String errorMessage = "You don't have SALESMANGER rights to access the system"
                    + " hotel operation module. ";
            throw new InvalidAccessRightException(errorMessage);
        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HORS Management System :: Hotel Operation :: Sales Manager ***\n");
            System.out.println("1: Create new room rate");
            System.out.println("2: View room rate details");
            System.out.println("3: View all room rate types");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    doCreateNewRoomRate();

                } else if (response == 2) {

                    doViewParticularRoomRateDetails();

                } else if (response == 3) {

                    doRetrieveAllRoomRate();

                } else if (response == 4) {

                    break;

                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }

    public void doAllocateRoomNow() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: Allocate room now ***\n");
        System.out.print("Enter the date of check-in day you want to allocate of M/d/yyyy>");
        String allocateDateInString = scanner.nextLine().trim();
        LocalDate allocateDate = dateInput(allocateDateInString);
        try {
            horsManagementControllerSessionBeanRemote.allocateRoomGivenDate(allocateDate);
            System.out.println("Rooms has been successfully allocated.");
        } catch (RoomAllocationIsDoneException ex) {
            System.out.println("An error has occured: " + ex.getMessage());
        }

    }

    public void doViewRoomAllocationExceptionReport() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: View room allocation exception report ***\n");
        System.out.print("Enter the date of the report you want to view in the form of M/d/yyyy>");
        String reportDateInString = scanner.nextLine().trim();
        LocalDate reportDate = dateInput(reportDateInString);
        RoomAllocationExceptionEntity reportRetrieved = new RoomAllocationExceptionEntity();
        try {
            reportRetrieved = horsManagementControllerSessionBeanRemote.retrieveReportByDate(reportDate);
        } catch (RoomAllocationExceptionReportDoesNotExistException ex) {
            System.out.println("An error has occured: " + ex.getMessage());
        }

        System.out.println("*** HORS Management System :: Type One Exception***\n");
        List<RoomReservationLineItemEntity> typeOneException = reportRetrieved.getTypeOneException();
        for (RoomReservationLineItemEntity roomReservation : typeOneException) {
            System.out.println("Room reservation line item: " + roomReservation.getRoomReservationLineItemId()
                    + "\n" + "Room reserved: " + roomReservation.getRoomTypeEntity().getName());
            System.out.println("Status: A room has been allocated");
        }

        System.out.println("*** HORS Management System :: Type Two Exception***\n");
        List<RoomReservationLineItemEntity> typeTwoException = reportRetrieved.getTypeTwoException();
        for (RoomReservationLineItemEntity roomReservation : typeTwoException) {
            System.out.println("Room reservation line item: " + roomReservation.getRoomReservationLineItemId()
                    + "\n" + "Room reserved: " + roomReservation.getRoomTypeEntity().getName());
            System.out.println("Status: A room has not been allocated");
        }

    }

    public void doRetrieveAllRoomRate() {
        horsManagementControllerSessionBeanRemote
                .retrieveAllRoomRate()
                .forEach(System.out::println);
    }

    public void doViewParticularRoomRateDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS Management System :: Hotel Operation :: Sales Manager :: View room rate details ***\n");

        System.out.print("Enter the id of the room rate>");
        String roomRateIdInString = scanner.nextLine().trim();
        Long roomRateId = Long.parseLong(roomRateIdInString);

        try {
            System.out.println("Now, you are viewing: ");
            RoomRateEntity currentRoomRate = horsManagementControllerSessionBeanRemote.retrieveRoomRateDetails(roomRateId);
            System.out.println(currentRoomRate);
            doUpdateOrDeleteRoomRateEntity(currentRoomRate);
        } catch (RoomRateEntityNotFoundException ex) {
            System.out.println("Error occurs during retrieval: " + ex.getMessage());
        }
    }

    public void doUpdateOrDeleteRoomRateEntity(RoomRateEntity currentRoomRate) {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** HORS Management System :: Hotel Operation :: Sales Manager :: View room rate details :: Update or Delete Room Rate ***\n");
            System.out.println("1: Update room rate");
            System.out.println("2: Delete room rate");
            System.out.println("3: Back");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = Integer.parseInt(scanner.nextLine());

                if (response == 1) {

                    if (currentRoomRate.getClass() == PromotionRateEntity.class
                            || currentRoomRate.getClass() == PeakRateEntity.class) {
                        doUpdatePromotionOrPeakRate(currentRoomRate);
                    } else if (currentRoomRate.getClass() == PublishedRateEntity.class
                            || currentRoomRate.getClass() == NormalRateEntity.class) {
                        doUpdatePublishedOrNormalRate(currentRoomRate);
                    }

                } else if (response == 2) {

                    doDeleteRoomRateEntity(currentRoomRate);
                    break;

                } else if (response == 3) {

                    break;

                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }

    }

    public void doUpdatePromotionOrPeakRate(RoomRateEntity currentRoomRate) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS Management System :: Hotel Operation :: Sales Manager :: View room rate details :: Update ***\n");
        System.out.println("Do take note that you are only allowed to change the nominal rate and the validity of the room rate");
        System.out.print("Enter the new nominal rate >");
        BigDecimal rate = new BigDecimal(Double.parseDouble(scanner.nextLine()));

        System.out.print("Enter the starting validity date in the form of M/d/yyyy>");
        String startDateInString = scanner.nextLine().trim();

        System.out.print("Enter the ending validity date in the form of M/d/yyyy>");
        String endDateInString = scanner.nextLine().trim();

        LocalDate dateToPutStart = dateInput(startDateInString);
        LocalDate dateToPutEnd = dateInput(endDateInString);

        currentRoomRate.setRate(rate);
        currentRoomRate.setStartValidityDate(dateToPutStart);
        currentRoomRate.setEndValidityDate(dateToPutEnd);

        horsManagementControllerSessionBeanRemote.updatePromotionAndPeakRate(currentRoomRate);
        System.out.println("Room rate is successfully updated");
    }

    public void doUpdatePublishedOrNormalRate(RoomRateEntity currentRoomRate) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS Management System :: Hotel Operation :: Sales Manager :: View room rate details :: Update ***\n");
        System.out.println("Do take note that you are only allowed to change the nominal rate of the room rate");
        System.out.print("Enter the new nominal rate >");
        BigDecimal rate = new BigDecimal(Double.parseDouble(scanner.nextLine()));
        currentRoomRate.setRate(rate);

        horsManagementControllerSessionBeanRemote.updatePublishedAndNormalRate(currentRoomRate);
        System.out.println("Room rate is successfully updated");
    }

    public void doDeleteRoomRateEntity(RoomRateEntity currentRoomRate) {
        System.out.println("*** HORS Management System :: Hotel Operation :: Sales Manager :: View room rate details :: Delete ***\n");
        try {
            horsManagementControllerSessionBeanRemote.deleteRoomRateEntity(currentRoomRate);
            System.out.println("Room rate is successfully deleted.");
        } catch (DeleteRoomRateException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void doCreateNewRoomRate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS Management System :: Hotel Operation :: Sales Manager :: Create a new room rate ***\n");

        while (true) {
            System.out.println("1: Published rate");
            System.out.println("2: Normal rate");
            System.out.println("3: Promotion rate");
            System.out.println("4: Peak rate");
            System.out.print(">");
            Integer roomRateType = Integer.parseInt(scanner.nextLine());

            if (roomRateType >= 1 && roomRateType <= 4) {
                // rmbr the break
                System.out.print("Enter the room type you would like to attach the new room rate to>");
                String roomTypeName = scanner.nextLine().trim();
                try {
                    RoomTypeEntity roomType = horsManagementControllerSessionBeanRemote.retrieveRoomType(roomTypeName);
                    System.out.print("Enter the rate>");
                    BigDecimal rate = new BigDecimal(Double.parseDouble(scanner.nextLine()));
                    System.out.println();
                    if (roomRateType == 1) {
                        PublishedRateEntity newPublishedRate = new PublishedRateEntity();
                        newPublishedRate.setRoomType(roomType);
                        newPublishedRate.setRate(rate);
                        try {
                            Long roomRateId = horsManagementControllerSessionBeanRemote
                                    .createNewPublishedRateEntity(newPublishedRate);
                            System.out.println("A published room rate of " + roomRateId + " has been created");
                            break;
                        } catch (PublishedRateHasAlreadyExistedException | UnknownPersistenceException ex) {
                            System.out.println("An error has occured in the creation of new room rate: " + ex.getMessage());
                            break;
                        }
                    } else if (roomRateType == 2) {
                        NormalRateEntity newNormalRate = new NormalRateEntity();
                        newNormalRate.setRoomType(roomType);
                        newNormalRate.setRate(rate);
                        try {
                            Long roomRateId = horsManagementControllerSessionBeanRemote
                                    .createNewNormalRateEntity(newNormalRate);
                            System.out.println("A published room rate of " + roomRateId + " has been created");
                            break;
                        } catch (UnknownPersistenceException | NormalRateHasAlreadyExistedException ex) {
                            System.out.println("An error has occured in the creation of new room rate: " + ex.getMessage());
                            break;
                        }
                    } else if (roomRateType == 3) {
                        PromotionRateEntity newPromotionRate = new PromotionRateEntity();
                        newPromotionRate.setRoomType(roomType);
                        newPromotionRate.setRate(rate);

                        System.out.print("Enter the starting validity date in the form of M/d/yyyy>");
                        String startDateInString = scanner.nextLine().trim();

                        System.out.print("Enter the ending validity date in the form of M/d/yyyy>");
                        String endDateInString = scanner.nextLine().trim();

                        LocalDate dateToPutStart = dateInput(startDateInString);
                        LocalDate dateToPutEnd = dateInput(endDateInString);

                        newPromotionRate.setStartValidityDate(dateToPutStart);
                        newPromotionRate.setEndValidityDate(dateToPutEnd);

                        System.out.print("Enter the name of the promotion rate>");
                        String roomRateName = scanner.nextLine();
                        newPromotionRate.setName(roomRateName);

                        try {
                            Long roomRateId = horsManagementControllerSessionBeanRemote
                                    .createNewPromotionRateEntity(newPromotionRate);
                            System.out.println("A published room rate of " + roomRateId + " has been created");
                            break;
                        } catch (UnknownPersistenceException | PromotionRateHasAlreadyExistedException ex) {
                            System.out.println("An error has occured in the creation of new room rate: " + ex.getMessage());
                        }

                    } else if (roomRateType == 4) {
                        PeakRateEntity newPeakRate = new PeakRateEntity();
                        newPeakRate.setRoomType(roomType);
                        newPeakRate.setRate(rate);
                        System.out.println("Now, enter the validity date: ");

                        System.out.print("Enter the starting validity date in the form of M/d/yyyy>");
                        String startDateInString = scanner.nextLine().trim();

                        System.out.print("Enter the ending validity date in the form of M/d/yyyy>");
                        String endDateInString = scanner.nextLine().trim();

                        LocalDate dateToPutStart = dateInput(startDateInString);
                        LocalDate dateToPutEnd = dateInput(endDateInString);

                        newPeakRate.setStartValidityDate(dateToPutStart);
                        newPeakRate.setEndValidityDate(dateToPutEnd);

                        try {
                            Long roomRateId = horsManagementControllerSessionBeanRemote
                                    .createNewPeakRateEntity(newPeakRate);
                            System.out.println("A published room rate of " + roomRateId + " has been created");
                            break;
                        } catch (UnknownPersistenceException | PeakRateHasAlreadyExistedException ex) {
                            System.out.println("An error has occured in the creation of new room rate: " + ex.getMessage());
                        }

                    }
                } catch (RoomTypeNotFoundException ex) {
                    System.out.println("New room rate creation is cancelled: " + ex.getMessage());
                }

            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

    }

    public LocalDate dateInput(String userInput) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate date = LocalDate.parse(userInput, dateFormat);

        return date;
    }

    public void doDeleteRoom() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: Delete a room ***\n");
        try {
            System.out.print("Enter the 4 digits room number that you want to update>");
            Integer roomNumber = Integer.parseInt(scanner.nextLine());
            try {
                horsManagementControllerSessionBeanRemote.deleteRoom(roomNumber);
                System.out.println("Room with room number " + roomNumber + " has been deleted");
            } catch (RoomNotFoundException ex) {
                System.out.println("Deletetion is cancelled: " + "room number is not found.");
            }
        } catch (InputMismatchException ex) {
            System.out.println("Deletion is cancelled: " + "input format is wrong.");
        }
    }

    public void doUpdateRoom() {
        Scanner scanner = new Scanner(System.in);
        RoomEntity newRoomEntity = new RoomEntity();
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: Update a room ***\n");

        try {
            System.out.print("Enter the 4 digits room number that you want to update>");
            Integer roomNumber = Integer.parseInt(scanner.nextLine());
            newRoomEntity.setRoomNumber(roomNumber);
            System.out.print("Enter the new room type: ");
            String roomTypeName = scanner.nextLine();
            try {
                RoomTypeEntity newRoomTypeEntity = horsManagementControllerSessionBeanRemote.retrieveRoomType(roomTypeName);
                newRoomEntity.setRoomType(newRoomTypeEntity);
            } catch (RoomTypeNotFoundException ex) {
                System.out.println("Error in creating a new room: " + "room type not found.");
            }

            while (true) {
                System.out.print("Select availability of the room (1: Available, 2: Unavailable)> ");
                Integer availability = scanner.nextInt();

                if (availability >= 1 && availability <= 2) {
                    newRoomEntity.setRoomStatus(RoomStatusEnum.values()[availability - 1]);
                    try {
                        horsManagementControllerSessionBeanRemote.updateRoom(newRoomEntity);
                        System.out.println("Room is successfully updated!");
                        break;
                    } catch (RoomNotFoundException | UpdateRoomException | InputDataValidationException ex) {
                        System.out.println("Update failed: " + ex.getMessage());
                    }
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
        } catch (InputMismatchException ex) {
            System.out.println("Error in creating a new room: " + "wrong input given.");
        }

    }

    public void doCreateNewRoom() {
        Scanner scanner = new Scanner(System.in);
        RoomEntity newRoomEntity = new RoomEntity();
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: Create a new room ***\n");
        System.out.print("Enter the room type of the new room>");
        String roomTypeName = scanner.nextLine();
        try {
            RoomTypeEntity roomTypeOfTheNewRoom = horsManagementControllerSessionBeanRemote.retrieveRoomType(roomTypeName);
            newRoomEntity.setRoomType(roomTypeOfTheNewRoom);
            try {
                System.out.print("Enter the 4 digits room number>");
                Integer roomNumber = scanner.nextInt();
                newRoomEntity.setRoomNumber(roomNumber);
            } catch (InputMismatchException ex) {
                System.out.println("Error in creating a new room: " + "wrong input given.");
            }

            while (true) {
                System.out.print("Select availability of the room (1: Available, 2: Unavailable)> ");
                Integer availability = scanner.nextInt();

                if (availability >= 1 && availability <= 2) {
                    newRoomEntity.setRoomStatus(RoomStatusEnum.values()[availability - 1]);
                    try {

                        horsManagementControllerSessionBeanRemote.createNewRoom(newRoomEntity);
                        System.out.println("Room with room number of " + newRoomEntity.getRoomNumber()
                                + " is successfully created");
                        break;
                    } catch (RoomNumberExistException | UnknownPersistenceException | InputDataValidationException ex) {
                        System.out.println("Error occurs in the creation of room: " + ex.getMessage());
                    }
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Error in creating a new room: " + ex.getMessage());
        }

    }

    public void doViewAllRoomTypes() {
        List<RoomTypeEntity> roomTypeList = horsManagementControllerSessionBeanRemote.retrieveAllRoomType();
        roomTypeList
                .stream()
                .forEach(System.out::println);
    }

    public void doViewRoomTypeDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: View Room Type Details ***\n");
        System.out.println("Enter the name of the room type you wish to view> ");
        String nameOfRoomType = scanner.nextLine();

        try {
            RoomTypeEntity currentRoomType = horsManagementControllerSessionBeanRemote.retrieveRoomType(nameOfRoomType);
            System.out.println(currentRoomType);
            updateOrDeleteRoomType(currentRoomType.getName());
        } catch (RoomTypeNotFoundException ex) {
            System.out.print("Error in retrieving room details: " + ex.getMessage());
        }

    }

    private void updateOrDeleteRoomType(String roomTypeName) {
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: Update or Delete Room Type ***\n");
        Scanner scanner = new Scanner(System.in);
        Integer response;
        while (true) {
            System.out.println("1: Update room type");
            System.out.println("2: Delete room type");
            System.out.println("3: Back\n");
            response = 0;

            while (response < 1 || response > 3) {

                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    doUpdateRoomType(roomTypeName);

                } else if (response == 2) {

                    doDeleteRoomType(roomTypeName);

                } else if (response == 3) {

                    break;
                }
            }

            if (response == 3) {
                break;
            }
        }

    }

    public void doRetrieveAllRooms() {
        horsManagementControllerSessionBeanRemote
                .retrieveAllRooms()
                .stream()
                .forEach(System.out::println);
    }

    private void doDeleteRoomType(String roomTypeName) {
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: Delete Room Type ***\n");
        try {
            horsManagementControllerSessionBeanRemote.deleteRoomType(roomTypeName);
            System.out.println("Room type is successfully deleted.");

        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Error during deletion: " + ex.getMessage());

        }
    }

    private void doUpdateRoomType(String roomTypeName) {

        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: Update Room Type ***\n");
        Scanner scanner = new Scanner(System.in);

        RoomTypeEntity newRoomType = new RoomTypeEntity();

        newRoomType.setName(roomTypeName);

        System.out.print("Enter the description about the room> ");
        newRoomType.setDescription(scanner.nextLine());

        System.out.print("Enter the size of the room (eg. 10x8) > ");
        newRoomType.setRoomSize(scanner.nextLine().trim());

        System.out.print("Enter the number of bed>");
        newRoomType.setBed(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter capacity of the room (number of people the room can fit) >");
        newRoomType.setCapacity(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter the amenities in the room> ");
        newRoomType.setAmenities(scanner.nextLine());
        System.out.println();
        try {
            horsManagementControllerSessionBeanRemote.updateRoomType(newRoomType);
            System.out.println("Room type is successfully updated");

        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Error in updating the room: " + ex.getMessage());
        }

    }

    public void doCreateNewRoomType() {

        Scanner scanner = new Scanner(System.in);
        RoomTypeEntity newRoomType = new RoomTypeEntity();
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: Create New Room Type ***\n");

        System.out.print("Enter the name of the room type> ");
        newRoomType.setName(scanner.nextLine().trim());

        System.out.print("Enter the description about the room> ");
        newRoomType.setDescription(scanner.nextLine());

        System.out.print("Enter the size of the room (eg. 10x8) > ");
        newRoomType.setRoomSize(scanner.nextLine().trim());

        System.out.print("Enter the number of bed>");
        newRoomType.setBed(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter capacity of the room (number of people the room can fit) >");
        newRoomType.setCapacity(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter the amenities in the room> ");
        newRoomType.setAmenities(scanner.nextLine());
        System.out.println();

        System.out.print("Enter the rank of the room (1 to 5) >");
        int rank = Integer.parseInt(scanner.nextLine());
        while (rank < 1 || rank > 5) {
            System.out.println("Rank is outside of the bound (1 - 5). Please enter another rank.");
            rank = Integer.parseInt(scanner.nextLine());
        }
        newRoomType.setRoomRanking(rank);

        try {
            System.out.println("This will take some time. Please wait for a while.");
            Long newRoomTypeId = horsManagementControllerSessionBeanRemote.createRoomType(newRoomType);
            System.out.println("Room type with roomTypeId " + newRoomTypeId + " is created. ");
            System.out.println("Please take note that the room type will only be available for 6 months. ");
            System.out.println("After which, you will have to recreate the room type.");
        } catch (RoomTypeExistException | UnknownPersistenceException ex) {
            System.out.println("Creation of room type failed: " + ex.getMessage());
        }
    }

}
