/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemjavaseclient;


import java.time.DateTimeException;
import java.util.List;
import java.util.Scanner;
import ws.client.InvalidLoginCredentialException;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.PartnerEntity;
import ws.client.roomReservationEntityWebService.ReservationNotFoundException_Exception;
import ws.client.searchOperation.InvalidRoomReservationEntityException_Exception;
import ws.client.searchOperation.LineItemExistException_Exception;
import ws.client.searchOperation.NoAvailableRoomOptionException_Exception;
import ws.client.searchOperation.PairRemote;
import ws.client.searchOperation.RoomTypeNotFoundException_Exception;
import ws.client.searchOperation.UnknownPersistenceException_Exception;

/**
 *
 * @author zenyew
 */
public class MainApp {

    private PartnerEntity currentPartnerEntity;

    public void runApp() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome Hors Reservation System (v4.1) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Search Hotel");
            System.out.println("3: Exit\n");

            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        menuMain();
                    } catch (InvalidLoginCredentialException_Exception ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    doSearchHotelBeforeLogin();
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

    private void doLogin() throws InvalidLoginCredentialException_Exception {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** HORS Reservation System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentPartnerEntity = partnerLogin(username, password);
        } else {
            throw (new InvalidLoginCredentialException_Exception("Missing login credential!", new InvalidLoginCredentialException()));
        }
    }

    public void doSearchHotelBeforeLogin() {
        Scanner scanner = new Scanner(System.in);
        Integer cinDay = 0;
        Integer cinMonth = 0;
        Integer cinYear = 0;
        Integer coutDay = 0;
        Integer coutMonth = 0;
        Integer coutYear = 0;
        Integer numberOfRooms = 0;
        Integer i = 0;

        System.out.println("*** HORS Reservation  :: Search Room ***\n");
        System.out.print("Enter Day of Check In> ");
        cinDay = scanner.nextInt();
        System.out.print("Enter Month of Check In> ");
        cinMonth = scanner.nextInt();
        System.out.print("Enter Year of Check In> ");
        cinYear = scanner.nextInt();
        System.out.print("Enter Day of Check Out> ");
        coutDay = scanner.nextInt();
        System.out.print("Enter Month of Check Out> ");
        coutMonth = scanner.nextInt();
        System.out.print("Enter Year of Check Out> ");
        coutYear = scanner.nextInt();
        System.out.print("Enter number of room(s)> ");
        numberOfRooms = scanner.nextInt();
        scanner.nextLine();

        try {

            if (cinDay > coutDay && cinMonth >= cinMonth && cinYear >= coutYear
                    || cinMonth > coutYear || cinYear > coutYear) {
                throw new DateTimeException("Invalid Date Input");
            }

            List<PairRemote> availableRooms = searchRoom(cinYear, cinMonth, cinDay, coutYear, coutMonth, coutDay, numberOfRooms);
            System.out.printf("\n%3s%10s%10s", "No", "Room Type", "Total Price");

            for (PairRemote pair : availableRooms) {
                i++;
                System.out.printf("\n%3s%10s%10s", i, pair.getRoomType().getName(), pair.getPrice());

            }

            System.out.println("");
            System.out.println("------------------------");

        } catch (java.time.DateTimeException ex) {
            System.out.println("Invalid Date input");

        } catch (NoAvailableRoomOptionException_Exception ex) {
            System.out.println("No room available");
        }

    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Hors Reservation System ***\n");
            System.out.println("You are login as " + currentPartnerEntity.getPartnerName());
            System.out.println("1: Search Hotel Room");
            System.out.println("2: View My Reservation Details");
            System.out.println("3: View All My Reservation");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doSearchHotel();
                } else if (response == 2) {
                    doViewMyReservationDetails();
                } else if (response == 3) {
                    doViewAllMyReservations();
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

    public void doSearchHotel() {
        Scanner scanner = new Scanner(System.in);
        Integer cinDay = 0;
        Integer cinMonth = 0;
        Integer cinYear = 0;
        Integer coutDay = 0;
        Integer coutMonth = 0;
        Integer coutYear = 0;
        Integer numberOfRooms = 0;
        Integer i = 0;
        String confirmReserve = "";
        Integer option = 0;
        Integer payment = 0;

        System.out.println("*** HORS Reservation  :: Search Room ***\n");
        System.out.print("Enter Day of Check In> ");
        cinDay = scanner.nextInt();
        System.out.print("Enter Month of Check In> ");
        cinMonth = scanner.nextInt();
        System.out.print("Enter Year of Check In> ");
        cinYear = scanner.nextInt();
        System.out.print("Enter Day of Check Out> ");
        coutDay = scanner.nextInt();
        System.out.print("Enter Month of Check Out> ");
        coutMonth = scanner.nextInt();
        System.out.print("Enter Year of Check Out> ");
        coutYear = scanner.nextInt();
        System.out.print("Enter number of room(s)> ");
        numberOfRooms = scanner.nextInt();
        scanner.nextLine();

        try {

            if (cinDay > coutDay && cinMonth >= cinMonth && cinYear >= coutYear
                    || cinMonth > coutYear || cinYear > coutYear) {
                throw new DateTimeException("Invalid Date Input");
            }

            List<PairRemote> availableRooms = searchRoom(cinYear, cinMonth, cinDay, coutYear, coutMonth, coutDay, numberOfRooms);
            System.out.printf("\n%3s%10s%10s", "No", "Room Type", "Total Price");

            for (PairRemote pair : availableRooms) {
                i++;
                System.out.printf("\n%3s%10s%10s", i, pair.getRoomType().getName(), pair.getPrice());

            }

            System.out.println("");
            System.out.println("------------------------");
            System.out.print("Reserve Room(s)? (Enter 'Y' to reserve)> ");
            confirmReserve = scanner.nextLine().trim();

            if (confirmReserve.equals("Y")) {
                while (true) {

                    System.out.print("Select the option>");
                    option = scanner.nextInt();
                    String roomTypeName = availableRooms.get(option - 1).getRoomType().getName();
                    Double totalAmount = availableRooms.get(option - 1).getPrice().doubleValue();
                    if (option >= 1 || option <= availableRooms.size()) {

                        while (true) {
                            System.out.println("The fee is $" + availableRooms.get(option - 1).getPrice() + ". Please choose the payment option");
                            System.out.println("1: AMEX");
                            System.out.println("2: MasterCard");
                            System.out.println("3: Visa");
                            payment = scanner.nextInt();

                            if (payment >= 1 && payment <= 3) {
                                break;
                            } else {
                                System.out.println("Invalid option, please try again!\n");
                            }
                        }

                        makeReservation(cinYear, cinMonth, cinDay, coutYear, coutMonth, coutDay, numberOfRooms, currentPartnerEntity.getUserId(), roomTypeName, totalAmount, payment);
                        System.out.println("Reservation is successfully created!");
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!");
                    }
                }

            }

        } catch (java.time.DateTimeException ex) {
            System.out.println("Invalid Date input");

        } catch (NoAvailableRoomOptionException_Exception ex) {
            System.out.println("No room available");
        } catch (RoomTypeNotFoundException_Exception ex) {
            System.out.println("Room type not available!");
        } catch (InvalidRoomReservationEntityException_Exception ex) {
            System.out.println("Invalid Reservation!");
        } catch (LineItemExistException_Exception ex) {
            System.out.println("Invalid Reservation");
        } catch (UnknownPersistenceException_Exception ex) {
            System.out.println("Reservation failed");
        }

    }

    public void doViewMyReservationDetails() {

        Scanner scanner = new Scanner(System.in);
        Long reservationId = 0L;

        System.out.println("*** HORS Reservation  :: View My Reservation Details ***\n");
        System.out.print("Enter Reservation Id> ");
        reservationId = scanner.nextLong();

        try {
            String roomReservation = viewReservationDetails(reservationId);
            System.out.println(roomReservation);
            /*
            System.out.println("Reservation Id :" + roomReservation.getRoomReservationId());
            System.out.println("Booking Account Id:" + currentPartnerEntity.getUserId());
            System.out.println("Booking Account name:" + currentPartnerEntity.getPartnerName());
            System.out.println("Reservation Date :" + roomReservation.getReservationDate().toString());
           
            System.out.println("Number of Rooms :" + roomReservation.getRoomReservationLineItems().size());
            System.out.println("Check-in Date :" + roomReservation.getRoomReservationLineItems().get(0).getCheckInDate());
            System.out.println("Check-out Date :" + roomReservation.getRoomReservationLineItems().get(0).getCheckoutDate());
             
            System.out.println("Total Amount :" + roomReservation.getTotalAmount());
             */

        } catch (ReservationNotFoundException_Exception ex) {
            System.out.println("Invalid Reservation Id");
        }
    }

    private void doViewAllMyReservations() {

        try {
            
            String reservations = viewAllMyReservations(currentPartnerEntity.getUserId());

            
            System.out.println(reservations);
        } /*catch (GuestNotFoundException_Exception ex) {
            System.out.println("Guest not found");
        }*/ catch (ReservationNotFoundException_Exception ex) {
            System.out.println("There are no reservation");

        }
        /*
        catch(DatatypeConfigurationException ex) {
            System.out.println("Invalid Data Type");
        }
         */

    }

    private static PartnerEntity partnerLogin(java.lang.String username, java.lang.String password) throws ws.client.InvalidLoginCredentialException_Exception {
        ws.client.PartnerEntityWebService_Service service = new ws.client.PartnerEntityWebService_Service();
        ws.client.PartnerEntityWebService port = service.getPartnerEntityWebServicePort();
        return port.partnerLogin(username, password);
    }

    private static String viewAllMyReservations(java.lang.Long userId) throws ws.client.roomReservationEntityWebService.ReservationNotFoundException_Exception {
        ws.client.roomReservationEntityWebService.RoomReservationEntityWebService_Service service = new ws.client.roomReservationEntityWebService.RoomReservationEntityWebService_Service();
        ws.client.roomReservationEntityWebService.RoomReservationEntityWebService port = service.getRoomReservationEntityWebServicePort();
        return port.viewAllMyReservations(userId);
    }

    private static String viewReservationDetails(java.lang.Long reservationId) throws ws.client.roomReservationEntityWebService.ReservationNotFoundException_Exception {
        ws.client.roomReservationEntityWebService.RoomReservationEntityWebService_Service service = new ws.client.roomReservationEntityWebService.RoomReservationEntityWebService_Service();
        ws.client.roomReservationEntityWebService.RoomReservationEntityWebService port = service.getRoomReservationEntityWebServicePort();
        return port.viewReservationDetails(reservationId);
    }

    private static java.util.List<PairRemote> searchRoom(java.lang.Integer cinYear,
            java.lang.Integer cinMonth,
            java.lang.Integer cinDay,
            java.lang.Integer coutYear,
            java.lang.Integer coutMonth,
            java.lang.Integer coutDay,
            java.lang.Integer numberOfRooms) throws ws.client.searchOperation.NoAvailableRoomOptionException_Exception {

        ws.client.searchOperation.SearchOperationWebService_Service service = new ws.client.searchOperation.SearchOperationWebService_Service();
        ws.client.searchOperation.SearchOperationWebService port = service.getSearchOperationWebServicePort();
        return port.searchRoom(cinYear, cinMonth, cinDay, coutYear, coutMonth, coutDay, numberOfRooms);
    }

    private static java.lang.Long makeReservation(java.lang.Integer cinYear,
            java.lang.Integer cinMonth,
            java.lang.Integer cinDay,
            java.lang.Integer coutYear,
            java.lang.Integer coutMonth,
            java.lang.Integer coutDay,
            java.lang.Integer numberOfRooms,
            java.lang.Long userId,
            java.lang.String roomTypeName,
            java.lang.Double totalAmount,
            java.lang.Integer paymentType) throws ws.client.searchOperation.RoomTypeNotFoundException_Exception,
            ws.client.searchOperation.InvalidRoomReservationEntityException_Exception,
            ws.client.searchOperation.LineItemExistException_Exception,
            ws.client.searchOperation.UnknownPersistenceException_Exception {

        ws.client.searchOperation.SearchOperationWebService_Service service = new ws.client.searchOperation.SearchOperationWebService_Service();
        ws.client.searchOperation.SearchOperationWebService port = service.getSearchOperationWebServicePort();
        return port.makeReservation(cinYear, cinMonth, cinDay, coutYear, coutMonth, coutDay, numberOfRooms, userId, roomTypeName, totalAmount, paymentType);
    }

}
