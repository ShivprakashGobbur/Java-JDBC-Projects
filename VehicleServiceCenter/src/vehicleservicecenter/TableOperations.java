package vehicleservicecenter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class TableOperations {
	public static Connection con;

    public static void createConnection() {
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vehicle_service_db",
                    "root",
                    "Shiv@8350"
            );

            System.out.println("Database connected successfully.");

        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    public static void createTables() {
        try {
            Statement st = con.createStatement();

            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS customers (" +
                "customer_id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50) NOT NULL," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "phone VARCHAR(10) NOT NULL)"
            );

            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS vehicles (" +
                "vehicle_id INT PRIMARY KEY AUTO_INCREMENT," +
                "customer_id INT NOT NULL," +
                "vehicle_number VARCHAR(20) UNIQUE NOT NULL," +
                "model VARCHAR(50) NOT NULL," +
                "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))"
            );

            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS service_records (" +
                "service_id INT PRIMARY KEY AUTO_INCREMENT," +
                "vehicle_id INT NOT NULL," +
                "service_date DATE DEFAULT (CURRENT_DATE)," +
                "service_type VARCHAR(100) NOT NULL," +
                "cost DECIMAL(10,2) DEFAULT 0," +
                "status VARCHAR(30) DEFAULT 'Booked'," +
                "notes VARCHAR(255)," +
                "FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id))"
            );

            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static void registerCustomer(String name, String email, String phone) {
        String sql = "INSERT INTO customers(name, email, phone) VALUES(?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            ps.executeUpdate();
            System.out.println("Customer registered successfully.");

        } catch (SQLException e) {
            System.out.println("Customer registration failed: " + e.getMessage());
        }
    }

    public static void viewCustomers() {
        String sql = "SELECT * FROM customers";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.printf("%-12s %-20s %-30s %-15s%n",
                    "Customer ID", "Name", "Email", "Phone");
            System.out.println("--------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-12d %-20s %-30s %-15s%n",
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"));
            }

        } catch (SQLException e) {
            System.out.println("Unable to display customers: " + e.getMessage());
        }
    }

    public static void registerVehicle(int customerId, String vehicleNumber, String model) {
        String sql = "INSERT INTO vehicles(customer_id, vehicle_number, model) VALUES(?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setString(2, vehicleNumber);
            ps.setString(3, model);

            ps.executeUpdate();
            System.out.println("Vehicle registered successfully.");

        } catch (SQLException e) {
            System.out.println("Vehicle registration failed: " + e.getMessage());
        }
    }

    public static void viewVehicles() {
        String sql =
            "SELECT v.vehicle_id, c.name, v.vehicle_number, v.model " +
            "FROM vehicles v JOIN customers c ON v.customer_id = c.customer_id";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.printf("%-12s %-20s %-20s %-20s%n",
                    "Vehicle ID", "Customer", "Vehicle Number", "Model");
            System.out.println("------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-12d %-20s %-20s %-20s%n",
                        rs.getInt("vehicle_id"),
                        rs.getString("name"),
                        rs.getString("vehicle_number"),
                        rs.getString("model"));
            }

        } catch (SQLException e) {
            System.out.println("Unable to display vehicles: " + e.getMessage());
        }
    }

    public static void bookService(int vehicleId, String serviceType, double cost, String notes) {
        String sql =
            "INSERT INTO service_records(vehicle_id, service_type, cost, notes) " +
            "VALUES(?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.setString(2, serviceType);
            ps.setDouble(3, cost);
            ps.setString(4, notes);

            ps.executeUpdate();
            System.out.println("Service booked successfully.");

        } catch (SQLException e) {
            System.out.println("Service booking failed: " + e.getMessage());
        }
    }

    public static void updateServiceStatus(int serviceId, String status) {
        String sql = "UPDATE service_records SET status = ? WHERE service_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, serviceId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Service status updated successfully.");
            } else {
                System.out.println("Service record not found.");
            }

        } catch (SQLException e) {
            System.out.println("Status update failed: " + e.getMessage());
        }
    }

    public static void viewServiceHistory(int vehicleId) {
        CustomQueries.displayServiceHistory(vehicleId);
    }

    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Unable to close connection: " + e.getMessage());
        }
    }
}
