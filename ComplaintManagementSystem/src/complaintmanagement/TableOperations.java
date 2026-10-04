package complaintmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TableOperations {
	static String url = "jdbc:mysql://localhost:3306/complaint_db";
    static String username = "root";
    static String password = "Shiv@8350";
    
    static Connection con;

    public static void createConnection() {
        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
    
 // Create all tables
    public static void createTables() {

        if (con == null) {
            System.out.println("Database is not connected!");
            return;
        }

        try {
            Statement stmt = con.createStatement();

            // Create users table
            String userQuery = "CREATE TABLE IF NOT EXISTS users ("
                    + "user_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "name VARCHAR(50) NOT NULL, "
                    + "email VARCHAR(100) UNIQUE NOT NULL, "
                    + "phone VARCHAR(10) NOT NULL"
                    + ")";

            stmt.executeUpdate(userQuery);

            // Create officers table
            String officerQuery = "CREATE TABLE IF NOT EXISTS officers ("
                    + "officer_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "officer_name VARCHAR(50) NOT NULL, "
                    + "department VARCHAR(50) NOT NULL, "
                    + "email VARCHAR(100) UNIQUE NOT NULL"
                    + ")";

            stmt.executeUpdate(officerQuery);

            // Create complaints table
            String complaintQuery = "CREATE TABLE IF NOT EXISTS complaints ("
                    + "complaint_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "user_id INT NOT NULL, "
                    + "officer_id INT NULL, "
                    + "description VARCHAR(255) NOT NULL, "
                    + "status VARCHAR(20) DEFAULT 'Pending', "
                    + "resolution VARCHAR(255) NULL, "
                    + "FOREIGN KEY (user_id) REFERENCES users(user_id), "
                    + "FOREIGN KEY (officer_id) REFERENCES officers(officer_id), "
                    + "CHECK (status IN ('Pending', 'In Progress', 'Resolved', 'Rejected'))"
                    + ")";

            stmt.executeUpdate(complaintQuery);

            System.out.println("Users table created successfully!");
            System.out.println("Officers table created successfully!");
            System.out.println("Complaints table created successfully!");

            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error creating tables!");
            e.printStackTrace();
        }
    }
    
    public static void registerUser(String name, String email, String phone) {

        if (!CustomQueries.validateName(name) ||
            !CustomQueries.validateEmail(email) ||
            !CustomQueries.validatePhone(phone)) {
            return;
        }

        String query = "INSERT INTO users (name, email, phone) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("User registered successfully!");
            }

            ps.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void viewUsers() {
        String query = "SELECT * FROM users";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.printf("%-10s %-20s %-30s %-15s%n",
                    "ID", "NAME", "EMAIL", "PHONE");

            System.out.println("--------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-10d %-20s %-30s %-15s%n",
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"));
            }

            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void updateUser(int userId, String name, String email, String phone) {
        String query = "UPDATE users SET name = ?, email = ?, phone = ? WHERE user_id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, userId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("User ID not found.");
            }

            ps.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("User ID not found.");
            }

            ps.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void registerComplaint(int userId, String description) {
        String query = "INSERT INTO complaints (user_id, description) VALUES (?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, userId);
            ps.setString(2, description);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Complaint registered successfully!");
            }

            ps.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void addOfficer(String name, String department, String email) {
        String query = "INSERT INTO officers (officer_name, department, email) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, department);
            ps.setString(3, email);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Officer added successfully!");
            }

            ps.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void assignOfficer(int complaintId, int officerId) {
        String query = "UPDATE complaints SET officer_id = ? WHERE complaint_id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, officerId);
            ps.setInt(2, complaintId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Officer assigned successfully!");
            } else {
                System.out.println("Complaint ID not found.");
            }

            ps.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void updateStatus(int complaintId, String status) {
        String query = "UPDATE complaints SET status = ? WHERE complaint_id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, status);
            ps.setInt(2, complaintId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Complaint status updated successfully!");
            } else {
                System.out.println("Complaint ID not found.");
            }

            ps.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void viewResolution(int complaintId) {
        String query = "SELECT complaint_id, status, resolution " +
                       "FROM complaints WHERE complaint_id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, complaintId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Complaint ID: " +
                        rs.getInt("complaint_id"));
                System.out.println("Status: " +
                        rs.getString("status"));

                String resolution = rs.getString("resolution");

                if (resolution == null || resolution.isEmpty()) {
                    System.out.println("Resolution: Not available yet");
                } else {
                    System.out.println("Resolution: " + resolution);
                }
            } else {
                System.out.println("Complaint ID not found.");
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static void updateResolution(int complaintId, String resolution) {
        String query = "UPDATE complaints SET resolution = ? WHERE complaint_id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, resolution);
            ps.setInt(2, complaintId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Resolution updated successfully!");
            } else {
                System.out.println("Complaint ID not found.");
            }

            ps.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    // Close database connection
    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Database connection closed successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
