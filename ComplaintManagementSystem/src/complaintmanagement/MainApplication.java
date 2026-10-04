package complaintmanagement;

import java.util.Scanner;

public class MainApplication {
	 public static void main(String[] args) {
		 Scanner sc = new Scanner(System.in);
	        TableOperations.createConnection();

	        int choice;

	        do {
	            System.out.println("\n===== COMPLAINT MANAGEMENT SYSTEM =====");
	            System.out.println("1. Register User");
	            System.out.println("2. View Users");
	            System.out.println("3. Add Officer");
	            System.out.println("4. Register Complaint");
	            System.out.println("5. Assign Officer");
	            System.out.println("6. Update Complaint Status");
	            System.out.println("7. View Complaint Resolution");
	            System.out.println("8. View All Complaint Details");
	            System.out.println("9. Update User");
	            System.out.println("10. Delete User");
	            System.out.println("11. Enter Complaint Resolution");
	            System.out.println("0. Exit");
	            System.out.print("Enter your choice: ");

	            choice = sc.nextInt();
	            sc.nextLine();

	            switch (choice) {

	                case 1:
	                    System.out.print("Enter name: ");
	                    String name = sc.nextLine();

	                    System.out.print("Enter email: ");
	                    String email = sc.nextLine();

	                    System.out.print("Enter phone: ");
	                    String phone = sc.nextLine();

	                    TableOperations.registerUser(name, email, phone);
	                    break;

	                case 2:
	                    TableOperations.viewUsers();
	                    break;

	                case 3:
	                    System.out.print("Enter officer name: ");
	                    String officerName = sc.nextLine();

	                    System.out.print("Enter department: ");
	                    String department = sc.nextLine();

	                    System.out.print("Enter officer email: ");
	                    String officerEmail = sc.nextLine();

	                    TableOperations.addOfficer(
	                            officerName, department, officerEmail);
	                    break;

	                case 4:
	                    System.out.print("Enter user ID: ");
	                    int userId = sc.nextInt();
	                    sc.nextLine();

	                    System.out.print("Enter complaint description: ");
	                    String description = sc.nextLine();

	                    TableOperations.registerComplaint(userId, description);
	                    break;

	                case 5:
	                    System.out.print("Enter complaint ID: ");
	                    int complaintId = sc.nextInt();

	                    System.out.print("Enter officer ID: ");
	                    int officerId = sc.nextInt();

	                    TableOperations.assignOfficer(complaintId, officerId);
	                    break;

	                case 6:
	                	System.out.print("Enter complaint ID: ");
	                    int statusComplaintId = sc.nextInt();

	                    System.out.println(
	                            "1. Pending\n2. In Progress\n3. Resolved\n4. Rejected");
	                    System.out.print("Enter status choice: ");
	                    int statusChoice = sc.nextInt();
	                    sc.nextLine();

	                    String status = "";

	                    switch (statusChoice) {
	                        case 1:
	                            status = "Pending";
	                            break;
	                        case 2:
	                            status = "In Progress";
	                            break;
	                        case 3:
	                            status = "Resolved";
	                            break;
	                        case 4:
	                            status = "Rejected";
	                            break;
	                        default:
	                            System.out.println("Invalid status choice!");
	                            break;
	                    }

	                    if (!status.isEmpty()) {
	                        TableOperations.updateStatus(statusComplaintId, status);
	                    }
	                    break;
	                case 7:
	                    System.out.print("Enter complaint ID: ");
	                    int resolutionId = sc.nextInt();

	                    TableOperations.viewResolution(resolutionId);
	                    break;

	                case 8:
	                    CustomQueries.viewComplaintDetails();
	                    break;

	                case 9:
	                    System.out.print("Enter user ID: ");
	                    int updateId = sc.nextInt();
	                    sc.nextLine();

	                    System.out.print("Enter new name: ");
	                    String newName = sc.nextLine();

	                    System.out.print("Enter new email: ");
	                    String newEmail = sc.nextLine();

	                    System.out.print("Enter new phone: ");
	                    String newPhone = sc.nextLine();

	                    TableOperations.updateUser(
	                            updateId, newName, newEmail, newPhone);
	                    break;

	                case 10:
	                    System.out.print("Enter user ID to delete: ");
	                    int deleteId = sc.nextInt();

	                    TableOperations.deleteUser(deleteId);
	                    break;
	                    
	                case 11:
	                    System.out.print("Enter complaint ID: ");
	                    int resolutionComplaintId = sc.nextInt();
	                    sc.nextLine();

	                    System.out.print("Enter resolution details: ");
	                    String resolution = sc.nextLine();

	                    TableOperations.updateResolution(
	                            resolutionComplaintId, resolution);
	                    break;
	                    
	                case 0:
	                    System.out.println("Exiting application...");
	                    break;

	                default:
	                    System.out.println("Invalid choice. Try again.");
	            }

	        } while (choice != 0);

	        TableOperations.closeConnection();
	        sc.close();
	    }
	        
	    }

