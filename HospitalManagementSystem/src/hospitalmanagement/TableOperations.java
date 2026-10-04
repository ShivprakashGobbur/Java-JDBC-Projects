package hospitalmanagement;
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
	                    "jdbc:mysql://localhost:3306/hospital_management_db",
	                    "root",
	                    "HOSPITAL_DB_PASSWORD"
	            );
	            System.out.println("Database connected successfully.");

	        } catch (SQLException e) {
	            System.out.println("Connection failed: " + e.getMessage());
	        }
	    }

	    public static void createTables() {
	        try (Statement st = con.createStatement()) {

	            st.executeUpdate(
	                "CREATE TABLE IF NOT EXISTS patients (" +
	                "patient_id INT PRIMARY KEY AUTO_INCREMENT, " +
	                "name VARCHAR(50) NOT NULL, " +
	                "age INT NOT NULL, " +
	                "gender VARCHAR(10) NOT NULL, " +
	                "phone VARCHAR(10) NOT NULL)"
	            );

	            st.executeUpdate(
	                "CREATE TABLE IF NOT EXISTS doctors (" +
	                "doctor_id INT PRIMARY KEY AUTO_INCREMENT, " +
	                "name VARCHAR(50) NOT NULL, " +
	                "specialization VARCHAR(50) NOT NULL, " +
	                "phone VARCHAR(10) NOT NULL)"
	            );

	            st.executeUpdate(
	                "CREATE TABLE IF NOT EXISTS appointments (" +
	                "appointment_id INT PRIMARY KEY AUTO_INCREMENT, " +
	                "patient_id INT NOT NULL, " +
	                "doctor_id INT NOT NULL, " +
	                "appointment_date DATE NOT NULL, " +
	                "reason VARCHAR(255) NOT NULL, " +
	                "status VARCHAR(30) DEFAULT 'Scheduled', " +
	                "FOREIGN KEY (patient_id) REFERENCES patients(patient_id), " +
	                "FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id))"
	            );

	            st.executeUpdate(
	                "CREATE TABLE IF NOT EXISTS prescriptions (" +
	                "prescription_id INT PRIMARY KEY AUTO_INCREMENT, " +
	                "appointment_id INT NOT NULL, " +
	                "medicines VARCHAR(255) NOT NULL, " +
	                "instructions VARCHAR(255), " +
	                "FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id))"
	            );

	            System.out.println("Tables created successfully.");

	        } catch (SQLException e) {
	            System.out.println("Table creation failed: " + e.getMessage());
	        }
	    }

	    public static void registerPatient(String name, int age, String gender, String phone) {
	        String sql = "INSERT INTO patients(name, age, gender, phone) VALUES(?,?,?,?)";

	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, name);
	            ps.setInt(2, age);
	            ps.setString(3, gender);
	            ps.setString(4, phone);

	            ps.executeUpdate();
	            System.out.println("Patient registered successfully.");

	        } catch (SQLException e) {
	            System.out.println("Patient registration failed: " + e.getMessage());
	        }
	    }

	    public static void viewPatients() {
	        String sql = "SELECT * FROM patients";

	        try (Statement st = con.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {

	            System.out.printf("%-12s %-20s %-8s %-12s %-15s%n",
	                    "Patient ID", "Name", "Age", "Gender", "Phone");
	            System.out.println("----------------------------------------------------------------");

	            while (rs.next()) {
	                System.out.printf("%-12d %-20s %-8d %-12s %-15s%n",
	                        rs.getInt("patient_id"),
	                        rs.getString("name"),
	                        rs.getInt("age"),
	                        rs.getString("gender"),
	                        rs.getString("phone"));
	            }

	        } catch (SQLException e) {
	            System.out.println("Unable to display patients: " + e.getMessage());
	        }
	    }

	    public static void addDoctor(String name, String specialization, String phone) {
	        String sql = "INSERT INTO doctors(name, specialization, phone) VALUES(?,?,?)";

	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, name);
	            ps.setString(2, specialization);
	            ps.setString(3, phone);

	            ps.executeUpdate();
	            System.out.println("Doctor added successfully.");

	        } catch (SQLException e) {
	            System.out.println("Doctor registration failed: " + e.getMessage());
	        }
	    }

	    public static void viewDoctors() {
	        String sql = "SELECT * FROM doctors";

	        try (Statement st = con.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {

	            System.out.printf("%-12s %-20s %-25s %-15s%n",
	                    "Doctor ID", "Name", "Specialization", "Phone");
	            System.out.println("--------------------------------------------------------------------------");

	            while (rs.next()) {
	                System.out.printf("%-12d %-20s %-25s %-15s%n",
	                        rs.getInt("doctor_id"),
	                        rs.getString("name"),
	                        rs.getString("specialization"),
	                        rs.getString("phone"));
	            }

	        } catch (SQLException e) {
	            System.out.println("Unable to display doctors: " + e.getMessage());
	        }
	    }

	    public static void bookAppointment(int patientId, int doctorId,
	                                       String date, String reason) {
	        String sql =
	            "INSERT INTO appointments(patient_id, doctor_id, appointment_date, reason) " +
	            "VALUES(?,?,?,?)";

	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setInt(1, patientId);
	            ps.setInt(2, doctorId);
	            ps.setString(3, date);
	            ps.setString(4, reason);

	            ps.executeUpdate();
	            System.out.println("Appointment booked successfully.");

	        } catch (SQLException e) {
	            System.out.println("Appointment booking failed: " + e.getMessage());
	        }
	    }

	    public static void viewAppointments() {
	        String sql =
	            "SELECT a.appointment_id, p.name AS patient_name, " +
	            "d.name AS doctor_name, a.appointment_date, a.reason, a.status " +
	            "FROM appointments a " +
	            "JOIN patients p ON a.patient_id = p.patient_id " +
	            "JOIN doctors d ON a.doctor_id = d.doctor_id";

	        try (Statement st = con.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {

	            System.out.printf("%-15s %-20s %-20s %-15s %-25s %-15s%n",
	                    "Appointment ID", "Patient", "Doctor",
	                    "Date", "Reason", "Status");
	            System.out.println("------------------------------------------------------------------------------------------------");

	            while (rs.next()) {
	                System.out.printf("%-15d %-20s %-20s %-15s %-25s %-15s%n",
	                        rs.getInt("appointment_id"),
	                        rs.getString("patient_name"),
	                        rs.getString("doctor_name"),
	                        rs.getDate("appointment_date"),
	                        rs.getString("reason"),
	                        rs.getString("status"));
	            }

	        } catch (SQLException e) {
	            System.out.println("Unable to display appointments: " + e.getMessage());
	        }
	    }

	    public static void addPrescription(int appointmentId, String medicines,
	                                       String instructions) {
	        String sql =
	            "INSERT INTO prescriptions(appointment_id, medicines, instructions) " +
	            "VALUES(?,?,?)";

	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setInt(1, appointmentId);
	            ps.setString(2, medicines);
	            ps.setString(3, instructions);

	            ps.executeUpdate();
	            System.out.println("Prescription added successfully.");

	        } catch (SQLException e) {
	            System.out.println("Prescription entry failed: " + e.getMessage());
	        }
	    }

	    public static void viewPrescriptions() {
	        String sql =
	            "SELECT pr.prescription_id, a.appointment_id, p.name AS patient_name, " +
	            "d.name AS doctor_name, pr.medicines, pr.instructions " +
	            "FROM prescriptions pr " +
	            "JOIN appointments a ON pr.appointment_id = a.appointment_id " +
	            "JOIN patients p ON a.patient_id = p.patient_id " +
	            "JOIN doctors d ON a.doctor_id = d.doctor_id";

	        try (Statement st = con.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {

	            System.out.printf("%-15s %-15s %-20s %-20s %-25s %-25s%n",
	                    "Prescription", "Appointment", "Patient",
	                    "Doctor", "Medicines", "Instructions");
	            System.out.println("----------------------------------------------------------------------------------------------------------------");

	            while (rs.next()) {
	                System.out.printf("%-15d %-15d %-20s %-20s %-25s %-25s%n",
	                        rs.getInt("prescription_id"),
	                        rs.getInt("appointment_id"),
	                        rs.getString("patient_name"),
	                        rs.getString("doctor_name"),
	                        rs.getString("medicines"),
	                        rs.getString("instructions"));
	            }

	        } catch (SQLException e) {
	            System.out.println("Unable to display prescriptions: " + e.getMessage());
	        }
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
