package mASK;


import java.util.List;

/**
 * Class to package all information together for an Answer to a Question.
 * 
 * @author Mckenzie Moize
 */
class Answer {
    // <editor-fold desc="Static Variables">
    /**
     * The type value for an Answer to a multiple choice Question.
     */
    public static final int MULTIPLE_CHOICE_ANSWER = 2;
    
    /**
     * The type value for an Answer to a Student asked Question or short 
     * answer from the Professor.
     */
    public static final int STUDENT_ANSWER = 1;
    // </editor-fold>
    
    // <editor-fold desc="Data Fields">
    /**
     * The contents of this Answer.
     */
    protected final String ANSWER;
    
    /**
     * The Person is submitted this Answer.
     */
    protected final Person ANSWERER;
    
    /**
     * Marker for whether this Answer has been marked correct.
     */
    protected boolean isCorrect;
    
    /**
     * The unique ID number of the Question this Answer is connected to.
     */
    protected final int QUESTION_ID;
    
    /**
     * The Session this Answer is connected to.
     */
    protected final Session SESSION;
    
    /**
     * The type of Answer that this is.
     */
    protected final int TYPE;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    /**
     * Constructor for an Answer object.
     * 
     * @param answerer The Person who submitted this Answer.
     * @param questionID The unique ID number for the Question corresponding to 
     * this Answer     
     * @param answer The contents of this Answer.
     * @param type The type of Answer.
     * @param isCorrect Whether or not the Answer is marked as correct
     */
    public Answer(Person answerer, int questionID, String answer, int type, boolean isCorrect) {
        ANSWERER = answerer;
        QUESTION_ID = questionID;
        ANSWER = answer;
        TYPE = type;
        this.isCorrect = isCorrect;
        SESSION = answerer.session;
    }
    // </editor-fold>
    
    // <editor-fold desc="Getters">
    /**
     * Method to get the index of the correct Answer from a list of Answers
     * 
     * @param answers A list of Answers for a given Question
     * @return The index of the correct Answer or -1 if none exists
     */
    public static int getCorrectAnswer(List<Answer> answers) {
        // Iterate through the List of Answers
        for (Answer answer : answers) {
            if (answer.isCorrect) {
                return answers.indexOf(answer);
            }
        }
        
        return -1; // Return -1 if no correct Answer exists
    }
    // </editor-fold>
}