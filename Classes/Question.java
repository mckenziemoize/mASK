package mASK;

import java.time.LocalTime;
import java.util.List;

/**
 * Class to hold the object containing all the information relating to a 
 * Question.
 * 
 * @author Mckenzie Moize
 */
public class Question {
    // <editor-fold desc="Data Fields">
    /**
     * The list of answer choices for the multiple choice Questions
     */
    protected List<Answer> answers;
    
    /**
     * The Person who asked the current Question.
     */
    protected final Person ASKER;
    
    /**
     * The index of the correct answer for multiple choice questions
     */
    protected int correctAnswer; 
    
    /**
     * The time the question will no longer be able to be answered.
     */
    protected final LocalTime EXPIRE_TIME;
    
    /**
     * Type of Question that is asked by the Professor and is answered using a 
     * multiple choice format.
     */
    public static final int MULTIPLE_CHOICE = 3;
    
    /**
     * The actual question that was asked in a String format.
     */
    protected final String QUESTION;
    
    /**
     * The unique ID number for the current Question.
     */
    protected final int QUESTION_ID;
    
    /**
     * Stores the type of Question that this is: Student, Short Answer, or
     * Multiple Choice
     */
    protected final int QUESTION_TYPE;
    
    /**
     * The object for the Session the Question was asked in
     */
    protected final Session SESSION;
    
    /**
     * Type of Question that is asked by the Professor and is answered using a
     * short answer format.
     */
    public static final int SHORT_ANSWER = 2;
    
    /**
     * Type of Question that is asked by the Student.
     */
    public static final int STUDENT = 1;
    // </editor-fold>
    
    // <editor-fold desc="Constructors">
    /**
     * Constructor for a Student question. The student asks a question for peers
     * or the professor to answer.
     * 
     * @param asker
     * @param question 
     */
    public Question(Student asker, String question) {
        QUESTION_TYPE = STUDENT;
        SESSION = asker.session;
        QUESTION_ID = SESSION.getNextQuestionID();
        ASKER = asker;
        QUESTION = question;
        EXPIRE_TIME = null;
    }
    
    /**
     * Constructor for Short Answer Professor Question. The Professor asks a
     * question and the Student answers in a short answer format in the given
     * time limit.
     * 
     * @param asker
     * @param question
     * @param timeLimit 
     */
    public Question(Professor asker, String question, int timeLimit) {
        QUESTION_TYPE = SHORT_ANSWER;
        SESSION = asker.session;
        QUESTION_ID = SESSION.getNextQuestionID();
        ASKER = asker;
        QUESTION = question;
        EXPIRE_TIME = LocalTime.now().plusSeconds(timeLimit);
    }
    
    /**
     * Constructor for a Multiple Choice Professor Question. The Professor asks
     * a multiple choice question and allows the student to answer in the given
     * time limit.
     * 
     * @param asker
     * @param question
     * @param timeLimit
     * @param answers
     * @param correctAnswer 
     */
    public Question(Professor asker, String question, int timeLimit, 
            List<Answer> answers, int correctAnswer) {
        QUESTION_TYPE = MULTIPLE_CHOICE;
        SESSION = asker.session;
        QUESTION_ID = SESSION.getNextQuestionID();
        ASKER = asker;
        QUESTION = question;
        EXPIRE_TIME = LocalTime.now().plusSeconds(timeLimit);
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }
    
    /**
     * Constructor for Session to create a Question object to return back. The 
     * parameters for answers will be null and correctAnswer will be -1 
     * for STUDENT and SHORT_ANSWER Questions.
     * 
     * @param type The type of Question being asked
     * @param questionID The unique ID number for the Question
     * @param asker The Person who asked the Question
     * @param question The actual Question being asked
     * @param expireTime The time that the question expires
     * @param answers The answer choices for the question
     * @param correctAnswer The index of the correct answer for the question
     */
    public Question(int type, int questionID, Person asker, String question, 
            LocalTime expireTime, List<Answer> answers, int correctAnswer) {
        QUESTION_TYPE = type;
        SESSION = asker.session;
        QUESTION_ID = questionID;
        ASKER = asker;
        QUESTION = question;
        EXPIRE_TIME = expireTime;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }
    // </editor-fold>
    
    // <editor-fold desc="Methods">
    /**
     * Allows a Student to answer a Question
     * 
     * @param answerer The Student answering the Question
     * @param answer The actual answer to the Question
     */
    public void answer(Student answerer, String answer) {
        SESSION.addAnswer(QUESTION_ID, answerer, answer);
    }
    
    /**
     * Allows a Student to answer a multiple choice Question
     * 
     * @param answerer The Student answering the Question
     * @param answerChoice The index of the Answer chosen by the Student
     * @return Whether or not the Student was correct
     */
    public boolean answer(Student answerer, int answerChoice) {
        return SESSION.addAnswer(QUESTION_ID, answerer, answerChoice);
    }
    // </editor-fold>
    
    // <editor-fold desc="Getters">
    /**
     * 
     * @return 
     */
    public List<Answer> getAnswers() {
        return SESSION.getAnswers(QUESTION_ID);
    }
    
    /**
     * Returns the Question object for the Question with the corresponding ID
     * 
     * @param person The person requesting the Question object
     * @param questionID The ID of the question being asked
     * @return Question object
     */
    public static Question getQuestion(Person person, int questionID) {
        return person.session.getQuestion(questionID);
    }
    // </editor-fold>
}