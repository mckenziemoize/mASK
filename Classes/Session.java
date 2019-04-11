package mASK;

/* TODO: Algorithm for determining best Question */

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.time.LocalTime;

/**
 * Mediator class for storing Session information
 * 
 * @author Mckenzie Moize
 */
class Session {
    // <editor-fold desc="Data Fields">
    /**
     * Connection to the mASK database
     */
    private final Connection conn;
    
    /**
     * ID number of the current session
    */
    private Integer SESSION_ID;
    // </editor-fold>
    
    // <editor-fold desc="Constructors">
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
     * sessionID and link to the database with this session in mind. 
     * 
     * @param sessionID The unique ID number for this Session
     */
    public Session(int sessionID) {
        // Connect to the mASK database
        conn = connectToDatabase("root", "csc34001", 
                "mask.clannzsjouuv.us-east-1.rds.amazonaws.com", 1433, "mask");

        SESSION_ID = sessionID;
    }
    // </editor-fold>
    
    // <editor-fold desc="Getters">
    /**
     * Method to receive the list of Answers from the database for the question
     * corresponding to the given Question ID
     * 
     * @param questionID The unique ID for the Question
     * @return The list of Answers for the given Question or null if the query 
     * fails
     */
    public List<Answer> getAnswers(int questionID) {
        try {
            String query = "select * from ANSWERS where question_id = " + 
                    questionID + " and session_id = " + SESSION_ID; 
        
            ResultSet rs = queryDB(query);

            // Stores all the answers for the given question
            List<Answer> answers = new ArrayList<>();
            
            while (rs.next()) {   
                answers.add(new Answer(getPerson(rs.getString("answerer")),
                        questionID, rs.getString("answer"), rs.getInt("type"), 
                        rs.getInt("is_correct") == 1));
            }
            
            return answers;
        } catch (SQLException ex) {
            System.out.println("Error getting Answers for given Question: " + ex);
            return null;
        }
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
            /* Query the database to get information on the Professor */
            String query = "select name, username from users where "
                    + "session_id = " + SESSION_ID + " and is_host = 1";
            
            ResultSet rs = queryDB(query); rs.next();
            
            return new Professor(this, rs.getString("name"), rs.getString("username"));
        } catch (SQLException ex) {
            System.out.println("Could not find host: " + ex);
            return null;
        }
    }
    
    /**
     * @return The next Question ID available for this Session or -1 if the 
     * query fails
     */
    public int getNextQuestionID() {
        try {
            /* Query the DB for the highest Question ID ID */
            String query = "select max(question_id) as question_id from QUESTIONS "
                    + "where session_id = " + SESSION_ID;
            
            ResultSet rs = queryDB(query); rs.next();

            // Increment by 1 to get the next Question ID
            return rs.getInt("question_id") + 1;
        } catch (SQLException ex) {
            System.out.println("Could not find question_id: " + ex);
            return -1; // Return -1 if the query fails
        } 
    }
    
    /**
     * Method to get a Person based on their username
     * 
     * @param id The username of the Person being queried
     * @return The Person object or null if the Person cannot be found
     */
    public Person getPerson(String id) {
        try {
            String query = "select * from USERS where username = " + id;
            ResultSet rs = queryDB(query); rs.next();

            String name = rs.getString("name"), username = rs.getString("username"); 

            switch (rs.getInt("is_host")) {
                case 1: return new Professor(this, name, username);
                default: return new Student(name, username, SESSION_ID);
            }
        } catch (SQLException ex) {
            System.out.println("Error getting Person from database: " + ex);
            return null;
        }
    }
    
    /**
     * Method to get the Question object from the Question ID
     * 
     * @param questionID The unique ID number for the Question for this Session
     * @return The Question object
     */
    public Question getQuestion(int questionID) {
        try {
            /* Query the database to get the information about the Question */
            String query = "elect * from QUESTIONS where question_id = " 
                    + questionID + " and session_id = " + SESSION_ID;

            ResultSet rs = queryDB(query); rs.next();

            // Get the type of the Question
            int type = rs.getInt("type");

            // Get the actual question
            String question = rs.getString("question");

            // Get the expire-time for the Question
            LocalTime expireTime = rs.getTime("expire_time").toLocalTime();

            // Get the Person who asked the Question
            Person asker = getPerson(rs.getString("asker"));

            // Get the answer choices for the question
            List<Answer> answers = getAnswers(questionID);
            
            // Get the index of the correct Answer
            int correctAnswer = Answer.getCorrectAnswer(answers);

            return new Question(type, questionID, asker, question, expireTime,
                    answers, correctAnswer);
        } catch (SQLException ex) {
            System.out.println("Error getting Question: " + ex);
            return null;
        }
    }

    /**
     * @return the list of Questions in the current Session
     */
    public List<Question> getQuestions() {
        try {
            String query = "select question_id from QUESTIONS where "
                    + "session_id = " + SESSION_ID;
            
            ResultSet rs = queryDB(query);
            
            List<Question> questions = new ArrayList<>();
            
            while (rs.next()) {
                questions.add(getQuestion(rs.getInt("question_id")));
            }
            
            return questions;
        } catch (SQLException ex) {
            System.out.println("Error getting Question list from database: " + ex);
            return null;
        }
    }
    
    /**
     * @return the ID for the current Session
     */
    public int getSessionID() {
        return SESSION_ID;
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
                students.add(new Student(name, username, SESSION_ID));
            }
            
            return students;
        } catch (SQLException ex) {
            System.out.println("Error finding students");
            return null;
        }
    }
    // </editor-fold>
    
    // <editor-fold desc="Database Insertion Methods">
    /**
     * Method to insert a Student answer to a Question into the database
     * 
     * @param questionID The unique ID number for the Question
     * @param answerer The Person who is answering the Question
     * @param answer The actual answer to the Question
     */
    public void addAnswer(int questionID, Student answerer, String answer) {
        try {
            String query = "insert into ANSWERS (question_id, answerer, answer, "
                    + "session_id, type, is_correct) values (" + questionID + 
                    ", " + answerer.ID + ", " + answer + ", " + SESSION_ID + 
                    ", " + 0 + ", " + 0 + ")";
            
            queryDB(query);
        } catch (SQLException ex) {
            System.out.println("Error adding Answer to database: " + ex);
        }
    }
    
    /**
     * Method to allow the Student to answer a multiple choice question
     * 
     * @param questionID The unique ID of the Question
     * @param answerer The Student who is answering the Question
     * @param answerChoice The index of the answer choice chosen
     * @return Whether or not the Student was correct
     */
    public boolean addAnswer(int questionID, Student answerer, int answerChoice) {
        try {
            /* Get the correct answer from the database */
            String query = "select answer from ANSWERS where question_id = " + questionID + " and session_id = " + SESSION_ID;
            ResultSet rs = queryDB(query); rs.next();
            String answer = rs.getString("answer"); 

            // Get the answer choice by finding the first letter from the answer
            boolean isCorrect = answerChoice == (int)answer.charAt(0) - 65;
            
            /* Add the Student's answer choice to the database */
            query = "insert into ANSWERS (question_id, answerer, answer, "
                    + "session_id, type, is_correct) values (" + questionID + 
                    ", " + answerer.ID + ", " + answer + ", " + SESSION_ID + 
                    ", " + 1 + ", " + isCorrect + ")";
            
            queryDB(query);
            
            return isCorrect;
        } catch (SQLException ex) {
            System.out.println("Error adding Answer to database: " + ex);
            return false;
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
    // </editor-fold>
    
    // <editor-fold desc="System Methods">
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
     * Execute an SQL query
     * 
     * @param query Query to be executed
     * @return The return statement of the query
     */
    private ResultSet queryDB(String query) throws SQLException {
        // Create the statement object
        Statement statement = conn.createStatement();

        // Execute the query
        return statement.executeQuery(query);
    }
    // </editor-fold>
    
    // <editor-fold desc="Session Methods">
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
     * Algorithm for determining which Question is the most important. For now
     * will return the oldest Question.
     * 
     * @return the next most important Question.
     */
    public Question next() {
        try {
            String query = "select min(question_id) as question_id from QUESTIONS where session_id = " + SESSION_ID;
            ResultSet rs = queryDB(query); rs.next();

            return getQuestion(rs.getInt("question_id"));
        } catch (SQLException ex) {
            System.out.println("Error getting the next Question: " + ex);
            return null;
        }
    }
    // </editor-fold>
}