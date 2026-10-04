package complaintmanagement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomQueries {
	public static boolean validateName(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Name cannot be empty");
            return false;
        }

        if (!name.matches("[a-zA-Z ]+")) {
            System.out.println("Name should contain only alphabets");
            return false;
        }

        return true;
    }
	public static boolean validateEmail(String email) {
	    if (email == null || email.isEmpty()) {
	        System.out.println("Email cannot be empty");
	        return false;
	    }

	    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
	        System.out.println("Invalid email format");
	        return false;
	    }

	    return true;
	}
	public static boolean validatePhone(String phone) {
	    if (phone == null || phone.isEmpty()) {
	        System.out.println("Phone number cannot be empty");
	        return false;
	    }

	    if (!phone.matches("[0-9]{10}")) {
	        System.out.println("Phone number must contain exactly 10 digits");
	        return false;
	    }

	    return true;
	}
	public static void viewComplaintDetails() {
	    String query = "SELECT c.complaint_id, u.name, " +
	                   "c.description, o.officer_name, " +
	                   "c.status, c.resolution " +
	                   "FROM complaints c " +
	                   "JOIN users u ON c.user_id = u.user_id " +
	                   "LEFT JOIN officers o " +
	                   "ON c.officer_id = o.officer_id";

	    try {
	        Statement st = TableOperations.con.createStatement();
	        ResultSet rs = st.executeQuery(query);

	        System.out.printf("%-5s %-18s %-25s %-18s %-15s %-25s%n",
	                "ID", "USER", "DESCRIPTION", "OFFICER",
	                "STATUS", "RESOLUTION");

	        System.out.println("----------------------------------------------------------------------------------------------------------");

	        while (rs.next()) {
	            System.out.printf("%-5d %-18s %-25s %-18s %-15s %-25s%n",
	                    rs.getInt("complaint_id"),
	                    rs.getString("name"),
	                    rs.getString("description"),
	                    rs.getString("officer_name") == null
	                            ? "Not Assigned"
	                            : rs.getString("officer_name"),
	                    rs.getString("status"),
	                    rs.getString("resolution") == null
	                            ? "Not Available"
	                            : rs.getString("resolution"));
	        }

	        rs.close();
	        st.close();

	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}
}
