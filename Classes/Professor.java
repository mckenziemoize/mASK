package mASK;

import java.sql.SQLException;
import java.util.List;

/**
 * The subclass of Person to represent the Professor. The Professor is the host
 * of the current Session and will be able to do Session maintenence and 
 * functions.
 * 
 * @author Mckenzie Moize
 */
class Professor extends Person {
    // <editor-fold desc="Constructors">
    /**
     * Constructor for Professor class
     * 
     * @param name the real name of the Professor
     * @param id the username of the Professor
     */
    public Professor(String name, String id, int capacity) {
        super(name, id); // Call the constructor from the superclass

        try {
            session = new Session(capacity, this);
        } catch (SQLException ex) {
            System.out.println("Failed to create Session: " + ex);
        }
    }
    
    /**
     * Constructor for Professor to connect to an existing Session
     * 
     * @param session The Session being connected to
     * @param name The real name of the Professor
     * @param id The username for the Professor
     */
    public Professor(Session session, String name, String id) {
        super(name, id);
        
        this.session = session;
    }
    // </editor-fold>
    
    // <editor-fold desc="Getters">
    /**
     * @return the attendance for the current Session
     */
    public List<Student> getAttendance() {
        return session.getStudents();
    }
    
    /**
     * @return the next most important Question from the Session
     */
    public Question getNextQuestion() {
        return session.next();
    }
    // </editor-fold>
    
    // <editor-fold desc="Methods">
    /**
     * Ask a short answer question.
     * 
     * @param question the question being asked
     * @param timeLimit the time the question will be able to be answered
     */
    public void askQuestion(String question, int timeLimit) {
        session.addQuestion(new Question(this, question, timeLimit));
    }
    
    /**
     * Ask a multiple choice question.
     * 
     * @param question the question being asked
     * @param timeLimit the time the question will be able to be answered
     * @param answersChoices the list of answer choices
     * @param correctAnswer the index of the correct answer
     */
    public void askQuestion(String question, int timeLimit, 
            List<Answer> answers, int correctAnswer) {
        session.addQuestion(new Question(this, question, timeLimit, 
                answers, correctAnswer));
    }
    
    /**
     * Send the question to each Student
     * 
     * @param question the Question that is being asked
     * @param timeLimit the time the Question will be displayed on Student screen
     */
    private void broadcastToStudents(Question question, int timeLimit) {
        // Loop through each Student in the Session
        for (Student student : session.getStudents()) {
            // TODO: Broadcast to students
        }
    }
    
    /**
     * Take away the privilege of a Student to ask questions and submit answers 
     * 
     * @param student the Student being muted
     */
    public void muteStudent(Student student) {
        student.isMuted = true;
    }
    // </editor-fold>
}