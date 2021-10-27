/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.HorsManagementControllerSessionBeanRemote;
import entity.EmployeeEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.InvalidAccessRightException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;

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
                    + " administration module. ";
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
            System.out.println("5: Update a room");

            System.out.println("9: Back\n");
            response = 0;

            while (response < 1 || response > 9) {
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

                } else if (response == 9) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 9) {
                break;
            }
        }
    }

    public void salesManagerMenu() {
    }
    
    public void doDeleteRoom(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: Delete a room ***\n");
         try {
            System.out.print("Enter the 4 digits room number that you want to update>");
            Integer roomNumber = scanner.nextInt();
            try{
            horsManagementControllerSessionBeanRemote.deleteRoom(roomNumber);
            System.out.println("Room with room number " + roomNumber + " has been deleted");
            } catch (RoomNotFoundException ex) {
                System.out.println("Deletetion is cancelled: " + "room number is not found.");
            }
         } catch(InputMismatchException ex) {
             System.out.println("Deletion is cancelled: " + "input format is wrong.");
         }
    }

    public void doUpdateRoom() {
        Scanner scanner = new Scanner(System.in);
        RoomEntity newRoomEntity = new RoomEntity();
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: Update a room ***\n");

        try {
            System.out.print("Enter the 4 digits room number that you want to update>");
            Integer roomNumber = scanner.nextInt();
            newRoomEntity.setRoomNumber(roomNumber);
            String roomTypeName = scanner.nextLine();
            try {
                RoomTypeEntity roomTypeOfTheNewRoom = horsManagementControllerSessionBeanRemote.retrieveRoomType(roomTypeName);
                newRoomEntity.setRoomType(roomTypeOfTheNewRoom);
            } catch (RoomTypeNotFoundException ex) {
                System.out.println("Error in creating a new room: " + "room type not found.");
            }

            while (true) {
                System.out.print("Select availability of the room (1: Available, 2: Unavailable)> ");
                Integer availability = scanner.nextInt();

                if (availability >= 1 && availability <= 2) {
                    newRoomEntity.setRoomStatus(RoomStatusEnum.values()[availability - 1]);
                    break;
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
                    break;
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
        System.out.println("Enter the name of the room type you wish to delete> ");
        String nameOfRoomType = scanner.nextLine();

        try {
            RoomTypeEntity currentRoomType = horsManagementControllerSessionBeanRemote.retrieveRoomType(nameOfRoomType);
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
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: Create New Room Type ***\n");

        System.out.print("Enter the name of the room you wish to change type> ");
        newRoomType.setName(roomTypeName);

        System.out.print("Enter the description about the room> ");
        newRoomType.setDescription(scanner.nextLine());

        System.out.print("Enter the size of the room (eg. 10x8) > ");
        newRoomType.setSize(scanner.nextLine().trim());

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
        newRoomType.setSize(scanner.nextLine().trim());

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
        newRoomType.setRank(rank);

        try {
            Long newRoomTypeId = horsManagementControllerSessionBeanRemote.createRoomType(newRoomType);
            System.out.println("Room type with roomTypeId " + newRoomTypeId + " is created. ");
        } catch (RoomTypeExistException | UnknownPersistenceException ex) {
            System.out.println("Creation of room type failed: " + ex.getMessage());
        }
    }

}
