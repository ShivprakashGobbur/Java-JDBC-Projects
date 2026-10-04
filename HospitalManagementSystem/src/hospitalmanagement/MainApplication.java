package hospitalmanagement;

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
	            System.out.println("\n===== HOSPITAL MANAGEMENT SYSTEM =====");
	            System.out.println("1. Register Patient");
	            System.out.println("2. View Patients");
	            System.out.println("3. Add Doctor");
	            System.out.println("4. View Doctors");
	            System.out.println("5. Book Appointment");
	            System.out.println("6. View Appointments");
	            System.out.println("7. Add Prescription");
	            System.out.println("8. View Prescriptions");
	            System.out.println("9. Search Patient");
	            System.out.println("10. Search Appointments by Patient");
	            System.out.println("0. Exit");
	            System.out.print("Enter your choice: ");

	            choice = sc.nextInt();
	            sc.nextLine();

	            switch (choice) {

	                case 1:
	                    System.out.print("Enter patient name: ");
	                    String name = sc.nextLine();

	                    if (!CustomQueries.validateName(name)) {
	                        System.out.println("Invalid name.");
	                        break;
	                    }

	                    System.out.print("Enter age: ");
	                    int age = sc.nextInt();
	                    sc.nextLine();

	                    if (!CustomQueries.validateAge(age)) {
	                        System.out.println("Invalid age.");
	                        break;
	                    }

	                    System.out.print("Enter gender (Male/Female/Other): ");
	                    String gender = sc.nextLine();

	                    if (!CustomQueries.validateGender(gender)) {
	                        System.out.println("Invalid gender.");
	                        break;
	                    }

	                    System.out.print("Enter phone number: ");
	                    String phone = sc.nextLine();

	                    if (!CustomQueries.validatePhone(phone)) {
	                        System.out.println("Phone must contain exactly 10 digits.");
	                        break;
	                    }

	                    TableOperations.registerPatient(name, age, gender, phone);
	                    break;

	                case 2:
	                    TableOperations.viewPatients();
	                    break;

	                case 3:
	                    System.out.print("Enter doctor name: ");
	                    String doctorName = sc.nextLine();

	                    if (!CustomQueries.validateName(doctorName)) {
	                        System.out.println("Invalid doctor name.");
	                        break;
	                    }

	                    System.out.print("Enter specialization: ");
	                    String specialization = sc.nextLine();

	                    if (!CustomQueries.validateSpecialization(specialization)) {
	                        System.out.println("Specialization cannot be empty.");
	                        break;
	                    }

	                    System.out.print("Enter phone number: ");
	                    String doctorPhone = sc.nextLine();

	                    if (!CustomQueries.validatePhone(doctorPhone)) {
	                        System.out.println("Phone must contain exactly 10 digits.");
	                        break;
	                    }

	                    TableOperations.addDoctor(doctorName, specialization, doctorPhone);
	                    break;

	                case 4:
	                    TableOperations.viewDoctors();
	                    break;

	                case 5:
	                    System.out.print("Enter patient ID: ");
	                    int patientId = sc.nextInt();

	                    System.out.print("Enter doctor ID: ");
	                    int doctorId = sc.nextInt();
	                    sc.nextLine();

	                    System.out.print("Enter appointment date (YYYY-MM-DD): ");
	                    String date = sc.nextLine();

	                    if (!CustomQueries.validateDate(date)) {
	                        System.out.println("Enter the date in YYYY-MM-DD format.");
	                        break;
	                    }

	                    System.out.print("Enter reason for appointment: ");
	                    String reason = sc.nextLine();

	                    if (!CustomQueries.validateText(reason)) {
	                        System.out.println("Reason cannot be empty.");
	                        break;
	                    }

	                    TableOperations.bookAppointment(patientId, doctorId, date, reason);
	                    break;

	                case 6:
	                    TableOperations.viewAppointments();
	                    break;

	                case 7:
	                    System.out.print("Enter appointment ID: ");
	                    int appointmentId = sc.nextInt();
	                    sc.nextLine();

	                    System.out.print("Enter medicines: ");
	                    String medicines = sc.nextLine();

	                    if (!CustomQueries.validateText(medicines)) {
	                        System.out.println("Medicines cannot be empty.");
	                        break;
	                    }

	                    System.out.print("Enter instructions: ");
	                    String instructions = sc.nextLine();

	                    TableOperations.addPrescription(
	                            appointmentId, medicines, instructions);
	                    break;

	                case 8:
	                    TableOperations.viewPrescriptions();
	                    break;

	                case 9:
	                    System.out.print("Enter patient ID to search: ");
	                    int searchPatientId = sc.nextInt();
	                    sc.nextLine();

	                    CustomQueries.searchPatient(searchPatientId);
	                    break;

	                case 10:
	                    System.out.print("Enter patient ID: ");
	                    int searchAppointmentId = sc.nextInt();
	                    sc.nextLine();

	                    CustomQueries.searchAppointmentsByPatient(searchAppointmentId);
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
