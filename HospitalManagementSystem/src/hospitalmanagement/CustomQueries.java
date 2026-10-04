package hospitalmanagement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomQueries {
	 public static boolean validateName(String name) {
	        return name != null && name.trim().matches("[a-zA-Z ]+");
	    }

	    public static boolean validateAge(int age) {
	        return age > 0 && age <= 120;
	    }

	    public static boolean validateGender(String gender) {
	        return gender != null &&
	               (gender.equalsIgnoreCase("Male") ||
	                gender.equalsIgnoreCase("Female") ||
	                gender.equalsIgnoreCase("Other"));
	    }

	    public static boolean validatePhone(String phone) {
	        return phone != null && phone.matches("[0-9]{10}");
	    }

	    public static boolean validateSpecialization(String specialization) {
	        return specialization != null && !specialization.trim().isEmpty();
	    }

	    public static boolean validateDate(String date) {
	        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
	    }

	    public static boolean validateText(String text) {
	        return text != null && !text.trim().isEmpty();
	    }

	    public static void searchPatient(int patientId) {
	        String sql = "SELECT * FROM patients WHERE patient_id = ?";

	        try (PreparedStatement ps = TableOperations.con.prepareStatement(sql)) {
	            ps.setInt(1, patientId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    System.out.println("\nPatient Details");
	                    System.out.println("Patient ID: " + rs.getInt("patient_id"));
	                    System.out.println("Name: " + rs.getString("name"));
	                    System.out.println("Age: " + rs.getInt("age"));
	                    System.out.println("Gender: " + rs.getString("gender"));
	                    System.out.println("Phone: " + rs.getString("phone"));
	                } else {
	                    System.out.println("Patient not found.");
	                }
	            }

	        } catch (SQLException e) {
	            System.out.println("Patient search failed: " + e.getMessage());
	        }
	    }

	    public static void searchAppointmentsByPatient(int patientId) {
	        String sql =
	            "SELECT a.appointment_id, p.name AS patient_name, " +
	            "d.name AS doctor_name, a.appointment_date, a.reason, a.status " +
	            "FROM appointments a " +
	            "JOIN patients p ON a.patient_id = p.patient_id " +
	            "JOIN doctors d ON a.doctor_id = d.doctor_id " +
	            "WHERE a.patient_id = ?";

	        try (PreparedStatement ps = TableOperations.con.prepareStatement(sql)) {
	            ps.setInt(1, patientId);

	            try (ResultSet rs = ps.executeQuery()) {
	                System.out.printf("%-15s %-20s %-20s %-15s %-25s %-15s%n",
	                        "Appointment ID", "Patient", "Doctor",
	                        "Date", "Reason", "Status");
	                System.out.println("------------------------------------------------------------------------------------------------");

	                boolean found = false;

	                while (rs.next()) {
	                    found = true;
	                    System.out.printf("%-15d %-20s %-20s %-15s %-25s %-15s%n",
	                            rs.getInt("appointment_id"),
	                            rs.getString("patient_name"),
	                            rs.getString("doctor_name"),
	                            rs.getDate("appointment_date"),
	                            rs.getString("reason"),
	                            rs.getString("status"));
	                }

	                if (!found) {
	                    System.out.println("No appointments found for this patient.");
	                }
	            }

	        } catch (SQLException e) {
	            System.out.println("Appointment search failed: " + e.getMessage());
	        }
	    }
}
