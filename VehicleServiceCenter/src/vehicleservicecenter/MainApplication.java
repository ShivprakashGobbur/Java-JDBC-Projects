package vehicleservicecenter;

import java.util.Scanner;

public class MainApplication {
	public static void main(String[] args) {


        Scanner sc = new Scanner(System.in);

        TableOperations.createConnection();

        if (TableOperations.con == null) {
            System.out.println("Cannot start application without database connection.");
            sc.close();
            return;
        }

        TableOperations.createTables();

        int choice;

        do {
            System.out.println("\n===== VEHICLE SERVICE CENTER MANAGEMENT =====");
            System.out.println("1. Register Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Register Vehicle");
            System.out.println("4. View Vehicles");
            System.out.println("5. Book Service");
            System.out.println("6. Update Service Status");
            System.out.println("7. View Service History");
            System.out.println("8. Search Vehicle");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter customer name: ");
                    String name = sc.nextLine();

                    if (!CustomQueries.validateName(name)) {
                        System.out.println("Invalid name. Use alphabets and spaces only.");
                        break;
                    }

                    System.out.print("Enter email: ");
                    String email = sc.nextLine();

                    if (!CustomQueries.validateEmail(email)) {
                        System.out.println("Invalid email.");
                        break;
                    }

                    System.out.print("Enter phone number: ");
                    String phone = sc.nextLine();

                    if (!CustomQueries.validatePhone(phone)) {
                        System.out.println("Phone number must contain exactly 10 digits.");
                        break;
                    }

                    TableOperations.registerCustomer(name, email, phone);
                    break;

                case 2:
                    TableOperations.viewCustomers();
                    break;

                case 3:
                    System.out.print("Enter customer ID: ");
                    int customerId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter vehicle number: ");
                    String vehicleNumber = sc.nextLine();

                    if (!CustomQueries.validateVehicleNumber(vehicleNumber)) {
                        System.out.println("Vehicle number cannot be empty.");
                        break;
                    }

                    System.out.print("Enter vehicle model: ");
                    String model = sc.nextLine();

                    if (model.trim().isEmpty()) {
                        System.out.println("Vehicle model cannot be empty.");
                        break;
                    }

                    TableOperations.registerVehicle(customerId, vehicleNumber, model);
                    break;

                case 4:
                    TableOperations.viewVehicles();
                    break;

                case 5:
                    System.out.print("Enter vehicle ID: ");
                    int vehicleId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter service type: ");
                    String serviceType = sc.nextLine();

                    if (serviceType.trim().isEmpty()) {
                        System.out.println("Service type cannot be empty.");
                        break;
                    }

                    System.out.print("Enter service cost: ");
                    double cost = sc.nextDouble();
                    sc.nextLine();

                    if (!CustomQueries.validateCost(cost)) {
                        System.out.println("Cost cannot be negative.");
                        break;
                    }

                    System.out.print("Enter notes: ");
                    String notes = sc.nextLine();

                    TableOperations.bookService(vehicleId, serviceType, cost, notes);
                    break;

                case 6:
                    System.out.print("Enter service ID: ");
                    int serviceId = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Available statuses:");
                    System.out.println("1. Booked");
                    System.out.println("2. In Progress");
                    System.out.println("3. Completed");
                    System.out.println("4. Cancelled");
                    System.out.print("Choose status: ");

                    int statusChoice = sc.nextInt();
                    sc.nextLine();

                    String status = null;

                    switch (statusChoice) {
                        case 1:
                            status = "Booked";
                            break;

                        case 2:
                            status = "In Progress";
                            break;

                        case 3:
                            status = "Completed";
                            break;

                        case 4:
                            status = "Cancelled";
                            break;

                        default:
                            System.out.println("Invalid status choice.");
                    }

                    if (status != null) {
                        TableOperations.updateServiceStatus(serviceId, status);
                    }
                    break;

                case 7:
                    System.out.print("Enter vehicle ID: ");
                    int historyVehicleId = sc.nextInt();
                    sc.nextLine();

                    TableOperations.viewServiceHistory(historyVehicleId);
                    break;

                case 8:
                    System.out.print("Enter vehicle number to search: ");
                    String searchNumber = sc.nextLine();

                    CustomQueries.searchVehicle(searchNumber);
                    break;

                case 0:
                    System.out.println("Exiting application...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 0);

        TableOperations.closeConnection();
        sc.close();
    }
}
