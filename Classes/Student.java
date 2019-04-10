import java.sql.SQLException;

/**
 * The class for a Student. The Students will be able to see a list of Questions
 * asked by other Students, ask a Question for themselves, and answer Questions
 * asked by the professor.
 * 
 * @author Mckenzie Moize
 */
class Student extends Person {
    /**
     * Whether or not the Student is muted
     */
    private boolean isMuted;
    
    /**
     * Constructor for Student class
     * 
     * @param name the real name of the Student
     * @param id the username of the Student
     */
    public Student(String name, String id) {
        super(name, id); // Call the constructor of the superclass
    }
    
    /**
     * Attempt to connect to the desired Session
     * 
     * @param session the Session that is trying to be connected to
     * @return whether or not connection was successful
     */
    public boolean connect(int sessionID) {
        // Assign the new Session to the current Session
        this.session = session;
        
        try {
            // Add this Student to the list of Students in the current Session
            this.session.addUser(this, false);
        } catch (SQLException ex) {
            System.out.println("Failed to add user." + ex);
        }
        
        return true;
    }
    
    /**
     * Ask a question
     * 
     * @param question the question being asked
     */
    public void askQuestion(String question) {
        
    }
    
    /**
     * Answer a short answer question from the Professor or a question asked by
     * another student
     * 
     * @param question the Question being answered
     * @param answer the answer for the question
     */
    public void answerQuestion(Question question, String answer) {
        
    }
    
    /**
     * Answer a multiple choice question asked by the Professor. 
     * 
     * @param question the Question being asked
     * @param answer the index of the answer choice selected
     * @return whether or not you are correct
     */
    public boolean answerQuestion(Question question, int answer) {
        // TODO: Ability to answer question
        return false;
    }
}