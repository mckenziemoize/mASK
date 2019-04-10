import java.util.List;

/**
 * The subclass of Person to represent the Professor. The Professor is the host
 * of the current Session and will be able to do Session maintenence and 
 * functions.
 * 
 * @author Mckenzie Moize
 */
class Professor extends Person {
    /**
     * Constructor for Professor class
     * 
     * @param name the real name of the Professor
     * @param id the username of the Professor
     */
    public Professor(String name, String id) {
        super(name, id); // Call the constructor from the superclass
        
        // Add to the database
        String query = "insert into USERS (username, name, is_host) values "
                + "('" + ID + "', '" + NAME + "', " + 1 + ")";
    }
    
    /**
     * Ask a short answer question.
     * 
     * @param question the question being asked
     * @param timeLimit the time the question will be able to be answered
     */
    public void askQuestion(String question, int timeLimit) {
        
    }
    
    /**
     * Ask a multiple choice question.
     * 
     * @param question the question being asked
     * @param timeLimit the time the question will be able to be answered
     * @param answersChoices the list of answer choices
     * @param correctAnswer the index of the correct answer
     */
    public void askQuestion(String question, int timeLimit, List<Answer> answersChoices, int correctAnswer) {
        
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
        
    }
    
    /**
     * @return the next most important Question from the Session
     */
    public Question getNextQuestion() {
        return session.next();
    }
    
    /**
     * @return the attendance for the current Session
     */
    public List<Student> getAttendance() {
        return session.getStudents();
    }
}