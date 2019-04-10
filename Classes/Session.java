import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mediator class for storing Session information
 * 
 * @author Mckenzie Moize
 */
class Session {
    /**
     * ID number of the current session
    */
    private Integer SESSION_ID;
    
    /**
     * Connection to the mASK database
     */
    private final Connection conn;
    
    /**
     * Constructor for Session. Will take the parameters for capacity and create
     * a new session from it. The Professor parameter will ensure that the
     * person calling the creation method is a Professor.
     * 
     * @param capacity The total amount of people allowed in the session
     * @param host The Professor who creates the session
     * @throws SQLException 
     */
    public Session(int capacity, Professor host) throws SQLException {
        // Connect to the mASK database
        conn = connectToDatabase("root", "csc34001", 
                "mask.clannzsjouuv.us-east-1.rds.amazonaws.com", 1433, "mask");
        
        // Get the session ID
        try {
            String query = "select max(session_id) as session_id from sessions";
            ResultSet rs = queryDB(query); rs.next();
            SESSION_ID = rs.getInt("session_id") + 1;
        } catch (SQLException ex) {
            System.out.println("Could not find session ID: " + ex);
        }
        
        // Create new session in database
        try {
            String query = "insert into sessions (session_id, capacity) values (" + 
                    SESSION_ID + ", " + capacity + ")";
            queryDB(query);
        } catch (SQLException ex) {
            if (!ex.toString().contains("The statement did not return a result set.")) {
                throw ex;
            }
        }
        
        // Add the Professor to the Session
        addUser(host, true);
    }
    
    /**
     * Constructor for Session. The constructor will take in the parameter of 
     * sessionID and link to the database with this session in mind. The student
     * parameter will ensure that the constructor does not get confused with the
     * creation constructor.
     * 
     * @param sessionID
     * @param student 
     */
    public Session(int sessionID, Student student) {
        // Connect to the mASK database
        conn = connectToDatabase("root", "csc34001", 
                "mask.clannzsjouuv.us-east-1.rds.amazonaws.com", 1433, "mask");

        SESSION_ID = sessionID;
    }
    
    /**
     * Connect to a database
     * 
     * @param username Name used to login to the database
     * @param password Password used to login to the database
     * @param serverName Endpoint address of the server
     * @param port Port number for the server
     * @param database Name of the database
     * @return 
     */
    private Connection connectToDatabase(String username, String password, 
            String serverName, int port, String database) {
        
        // Create a ServerDataSource object and add all relavent information
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser(username);
        ds.setPassword(password);
        ds.setServerName(serverName);
        ds.setPortNumber(port);
        ds.setDatabaseName(database);
        
        Connection conn;
        
        try {
            conn = ds.getConnection();
            System.out.println("Connection successful.");
        } catch (SQLServerException ex) {
            conn = null;
            System.out.println("Connection failed.");
        }
        
        return conn;
    }

    /**
     * @return the ID for the current Session
     */
    public int getSessionID() {
        return SESSION_ID;
    }

    /**
     * @return the capacity of the current Session
     */
    public int getCapacity() {
        try {
            String query = "select capacity from sessions where session_id = " 
                    + SESSION_ID;
            
            ResultSet rs = queryDB(query); rs.next();
            
            return rs.getInt("capacity");
        } catch (SQLException ex) {
            System.out.println("Could not find capacity: " + ex);
            return -1;
        }
    }
    
    /**
     * @return the host of the current Session
     */
    public Professor getHost() {
        try {
            String query = "select name, username from users where "
                    + "session_id = " + SESSION_ID + " and is_host = 1";
            
            ResultSet rs = queryDB(query); rs.next();
            
            return new Professor(rs.getString("name"), rs.getString("username"));
        } catch (SQLException ex) {
            System.out.println("Could not find host: " + ex);
            return null;
        }
    }
    
    /**
     * @return the list of Students in the current Session
     */
    public List<Student> getStudents() {
        try {
            String query = "select username, name from users where is_host = 0 "
                    + "and session_id = " + SESSION_ID;

            ResultSet rs = queryDB(query);

            String username, name;
            List<Student> students = new ArrayList<>();

            while (rs.next()) {
                username = rs.getString("username");
                name = rs.getString("name");
                students.add(new Student(name, username));
            }
            
            return students;
        } catch (SQLException ex) {
            System.out.println("Error finding students");
            return null;
        }
    }

    /**
     * @return the list of Questions in the current Session
     */
    public List<Question> getQuestions() {
        // TODO: query the questions table
        return null;
    }
    
    /**
     * @return whether or not the current Session is full
     */
    public boolean isFull() {
        try {
            String query = "select count(username) as count from users where sessionID = " 
                    + SESSION_ID;
            ResultSet rs = queryDB(query); rs.next();
            
            int currentStudents = rs.getInt("count");

            return currentStudents >= getCapacity();
        } catch (SQLException ex) {
            return false;
        }
    }
    
    /**
     * @return the next most important Question
     */
    public Question next() {
        // TOOD: Algorithm for best question (ALEX!)
        return null;
    }
    
    /**
     * Add a Student to the list of Students for the Session
     * 
     * @param student the Student being added
     */
    public void addUser(Person user, boolean isHost) throws SQLException {
        if (!isFull()) {
            String query = "insert into USERS (username, name, is_host, session_id)"
                    + " values ('" + user.ID + "', '" + user.NAME + "', " + 
                    (isHost ? 1 : 0) + ", " + SESSION_ID +  ")";

            try {
                queryDB(query);
            } catch (SQLException ex) {if (!ex.toString().contains("The statement did not return a result set.")) {
                    throw ex;
                }
            }
        }
    }
    
    /**
     * Add a Question to the list of Questions for the Session
     * 
     * @param question the question being asked
     */
    public void addQuestion(Question question) {
        // TODO: Insert into questions table
    }
    
    /**
     * Execute an SQL query
     * 
     * @param query Query to be executed
     * @return The return statement of the query
     */
    public ResultSet queryDB(String query) throws SQLException {
        // Create the statement object
        Statement statement = conn.createStatement();

        // Execute the query
        return statement.executeQuery(query);
    }
}