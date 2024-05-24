import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class GradingSystemTest {

    private QuizImpl quiz;

    @Before
    public void setUp() {
        quiz = QuizImpl.getInstance();
    }

    @Test
    public void testCreateQuiz() {
        List<String> questions = Arrays.asList("Question 1", "Question 2", "Question 3");
        List<String> answers = Arrays.asList("Answer 1", "Answer 2", "Answer 3");

        quiz.createQuiz("Test Quiz", questions, answers);

        assertTrue(quiz.quizzes.containsKey("Test Quiz"));
        assertEquals(questions, quiz.quizzes.get("Test Quiz"));
        assertEquals(answers, quiz.quizAnswers.get("Test Quiz"));
    }

    @Test
    public void testSetInstructions() {
        quiz.setInstructions("Test Quiz", "Read carefully and answer all questions.");

        assertTrue(quiz.instructions.containsKey("Test Quiz"));
        assertEquals("Read carefully and answer all questions.", quiz.instructions.get("Test Quiz"));
    }

    @Test
    public void testAssignGradesWithTwoCorrectAnswers() {
        List<String> questions = Arrays.asList("Question 1", "Question 2", "Question 3");
        List<String> answers = Arrays.asList("Answer 1", "Answer 2", "Answer 3");
        quiz.createQuiz("Test Quiz", questions, answers);
        quiz.students.put("student2", new Student("student2", "password", "Student Two", 1));

        List<String> studentAnswers = Arrays.asList("Answer 1", "Answer 2", "Wrong Answer");
        int percentage = quiz.assignGrades("student2", "Test Quiz", studentAnswers);


        // Expected grade percentage when 2 out of 3 answers are correct: 66%
        assertEquals(66, percentage, 0.01);
    }



    @Test
    public void testAssignGrades() {
        List<String> questions = Arrays.asList("Question 1", "Question 2", "Question 3");
        List<String> answers = Arrays.asList("Answer 1", "Answer 2", "Answer 3");
        quiz.createQuiz("Test Quiz", questions, answers);
        quiz.students.put("student1", new Student("student1", "password", "Student One", 1));

        List<String> studentAnswers = Arrays.asList("Answer 1", "Answer 2", "Answer 3");
        quiz.assignGrades("student1", "Test Quiz", studentAnswers);

        assertEquals(1, quiz.studentAnswers.size());
        assertEquals(1, quiz.quizTakers.get("Test Quiz").size());
    }

    @Test
    public void testProvideFeedback() {
        quiz.students.put("student1", new Student("student1", "password", "Student One", 1));

        quiz.provideFeedback("student1", "Good job!");

        assertTrue(quiz.feedbacks.containsKey("student1"));
        assertEquals("Good job!", quiz.feedbacks.get("student1"));
    }
}
