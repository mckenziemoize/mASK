package mASK;

/**
 * The class for a Student. The Students will be able to see a list of Questions
 * asked by other Students, ask a Question for themselves, and answer Questions
 * asked by the professor.
 * 
 * @author Mckenzie Moize
 */
class Student extends Person {
    // <editor-fold desc="Data Fields">
    /**
     * Whether or not the Student is muted
     */
    protected boolean isMuted;
    // </editor-fold>
    
    // <editor-fold desc="Constructors">
    /**
     * Constructor for Student class
     * 
     * @param name the real name of the Student
     * @param id the username of the Student
     */
    public Student(String name, String id, int sessionID) {
        super(name, id); // Call the constructor of the superclass
        
        session = new Session(sessionID);
    }
    // </editor-fold>
    
    // <editor-fold desc="Methods">
    /**
     * Answer a short answer question from the Professor or a question asked by
     * another student
     * 
     * @param question the Question being answered
     * @param answer the answer for the question
     */
    public void answerQuestion(Question question, String answer) {
        question.answer(this, answer);
    }
    
    /**
     * Answer a multiple choice question asked by the Professor. 
     * 
     * @param question the Question being asked
     * @param answer the index of the answer choice selected
     * @return whether or not you are correct
     */
    public boolean answerQuestion(Question question, int answerChoice) {
        // TODO: Ability to answer question
        return question.answer(this, answerChoice);
    }
    
    /**
     * Ask a question
     * 
     * @param question the question being asked
     */
    public void askQuestion(String question) {
        session.addQuestion(new Question(this, question));
    }
    // </editor-fold>
}