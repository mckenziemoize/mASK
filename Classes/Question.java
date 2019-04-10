import java.util.List;
import java.util.ArrayList;

class Question {
    public static final int STUDENT_QUESTION = 1;
    
    public static final int SHORT_ANSWER_QUESTION = 2;
    
    public static final int MULTIPLE_CHOICE_QUESTION = 3;
    
    public static int nextQuestionID = 1;
    
    private final int QUESTION_ID;
    
    private final String QUESTION;
    
    private final Person ASKER;
    
    private boolean isAnswered;
    
    private int type;
    
    private final List<Answer> answers;
    
    private final int CORRECT_ANSWER;
    
    private final long TIME_LIMIT;
    
    /**
     * The Student-Question constructor
     * 
     * @param question the question being asked
     * @param asker the Student asking the question
     */
    public Question(String question, Student asker) {
        QUESTION_ID = Question.nextQuestionID++;
        
        QUESTION = question;
        
        ASKER = asker;
        
        isAnswered = false;
        
        answers = new ArrayList<>();
        
        type = STUDENT_QUESTION;
        
        // Since they are not needed, set the correct answer and time limit to -1
        CORRECT_ANSWER = -1;
        TIME_LIMIT = -1;
        
        // Add to the list of questions in session
        
    }
    
    public Question(String question, Professor asker, int timeLimit) {
        QUESTION_ID = 1;
        QUESTION = question;
        ASKER = asker;
        TIME_LIMIT = timeLimit;
        answers = null; CORRECT_ANSWER = -1;
    }
}