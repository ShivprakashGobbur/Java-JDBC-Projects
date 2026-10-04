package vehicleservicecenter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomQueries {
	 public static boolean validateName(String name) {
	        return name != null && name.trim().matches("[a-zA-Z ]+");
	    }

	    public static boolean validateEmail(String email) {
	        return email != null &&
	               email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
	    }

	    public static boolean validatePhone(String phone) {
	        return phone != null && phone.matches("[0-9]{10}");
	    }

	    public static boolean validateVehicleNumber(String number) {
	        return number != null && !number.trim().isEmpty();
	    }

	    public static boolean validateCost(double cost) {
	        return cost >= 0;
	    }

	    public static void searchVehicle(String vehicleNumber) {
	        String sql =
	            "SELECT c.name, c.phone, v.vehicle_number, v.model " +
	            "FROM vehicles v JOIN customers c ON v.customer_id = c.customer_id " +
	            "WHERE v.vehicle_number = ?";

	        try (PreparedStatement ps = TableOperations.con.prepareStatement(sql)) {
	            ps.setString(1, vehicleNumber);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    System.out.println("\nVehicle Details");
	                    System.out.println("Customer Name: " + rs.getString("name"));
	                    System.out.println("Phone: " + rs.getString("phone"));
	                    System.out.println("Vehicle Number: " + rs.getString("vehicle_number"));
	                    System.out.println("Model: " + rs.getString("model"));
	                } else {
	                    System.out.println("Vehicle not found.");
	                }
	            }

	        } catch (SQLException e) {
	            System.out.println("Vehicle search failed: " + e.getMessage());
	        }
	    }

	    public static void displayServiceHistory(int vehicleId) {
	        String sql =
	            "SELECT s.service_id, c.name, v.vehicle_number, v.model, " +
	            "s.service_date, s.service_type, s.cost, s.status, s.notes " +
	            "FROM service_records s " +
	            "JOIN vehicles v ON s.vehicle_id = v.vehicle_id " +
	            "JOIN customers c ON v.customer_id = c.customer_id " +
	            "WHERE v.vehicle_id = ?";

	        try (PreparedStatement ps = TableOperations.con.prepareStatement(sql)) {
	            ps.setInt(1, vehicleId);

	            try (ResultSet rs = ps.executeQuery()) {
	                System.out.printf(
	                    "%-10s %-18s %-16s %-15s %-12s %-20s %-10s %-15s%n",
	                    "Service ID", "Customer", "Vehicle No.", "Model",
	                    "Date", "Service Type", "Cost", "Status"
	                );

	                System.out.println("----------------------------------------------------------------------------------------------------------");

	                boolean found = false;

	                while (rs.next()) {
	                    found = true;

	                    System.out.printf(
	                        "%-10d %-18s %-16s %-15s %-12s %-20s %-10.2f %-15s%n",
	                        rs.getInt("service_id"),
	                        rs.getString("name"),
	                        rs.getString("vehicle_number"),
	                        rs.getString("model"),
	                        rs.getDate("service_date"),
	                        rs.getString("service_type"),
	                        rs.getDouble("cost"),
	                        rs.getString("status")
	                    );

	                    System.out.println("Notes: " + rs.getString("notes"));
	                }

	                if (!found) {
	                    System.out.println("No service history found for this vehicle.");
	                }
	            }

	        } catch (SQLException e) {
	            System.out.println("Unable to display service history: " + e.getMessage());
	        }
	    }
}
