import java.util.*;

// Abstract class representing a user in the system
abstract class User {
    protected String username;
    protected String password;
    protected String fullname;

    // Constructor to initialize user attributes
    public User(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }

    // Method to authenticate user
    public boolean authenticate(String enteredUsername, String enteredPassword) {
        return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
    }

    // Abstract method representing user menu
    public abstract void menu(QuizImpl quiz);
}

// Class representing an admin user
class Admin extends User {
    // Constructor to initialize admin attributes
    public Admin(String username, String password, String fullname) {
        super(username, password, fullname);
    }

    // Method to display admin menu and handle admin operations
    @Override
    public void menu(QuizImpl quiz) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Create quiz");
            System.out.println("2. Set instructions");
            System.out.println("3. Check quiz status");
            System.out.println("4. Assign grades");
            System.out.println("5. Generate reports");
            System.out.println("6. Provide feedback");
            System.out.println("7. List all student details");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    // Create quiz
                    System.out.print("Enter quiz name: ");
                    String quizName = scanner.nextLine();
                    System.out.println("Enter questions and correct answers:");
                    List<String> questions = new ArrayList<>();
                    List<String> answers = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        System.out.print("Question " + (i + 1) + ": ");
                        questions.add(scanner.nextLine());
                        System.out.print("Correct answer: ");
                        answers.add(scanner.nextLine());
                    }
                    quiz.createQuiz(quizName, questions, answers);
                    break;
                case 2:
                    // Set instructions
                    System.out.print("Enter quiz name: ");
                    String setInstrQuizName = scanner.nextLine();
                    System.out.print("Enter instructions: ");
                    String instructions = scanner.nextLine();
                    quiz.setInstructions(setInstrQuizName, instructions);
                    break;
                case 3:
                    // Check quiz status
                    System.out.print("Enter quiz name: ");
                    String checkStatusQuizName = scanner.nextLine();
                    quiz.checkQuizStatus(checkStatusQuizName);
                    break;
                case 4:
                    // Assign grades
                    System.out.print("Enter student username: ");
                    String studentUsername = scanner.nextLine();
                    if (!quiz.students.containsKey(studentUsername)) {
                        System.out.println("Student " + studentUsername + " not found.");
                        break;
                    }
                    System.out.print("Enter quiz name: ");
                    String assignGradesQuizName = scanner.nextLine();
                    if (!quiz.quizzes.containsKey(assignGradesQuizName)) {
                        System.out.println("Quiz " + assignGradesQuizName + " not found.");
                        break;
                    }
                    List<String> studentAnswers;
                    do {
                        System.out.println("Enter answers (separated by space): ");
                        studentAnswers = Arrays.asList(scanner.nextLine().split(" "));
                        if (studentAnswers.size() != 3) {
                            System.out.println("Please enter exactly three answers.");
                        }
                    } while (studentAnswers.size() != 3);
                    quiz.assignGrades(studentUsername, assignGradesQuizName, studentAnswers);
                    break;
                case 5:
                    // Generate reports
                    System.out.print("Enter quiz name: ");
                    String reportQuizName = scanner.nextLine();
                    quiz.generateReports(reportQuizName);
                    break;
                case 6:
                    // Provide feedback
                    System.out.print("Enter student username: ");
                    String feedbackStudentUsername = scanner.nextLine();
                    if (!quiz.students.containsKey(feedbackStudentUsername)) {
                        System.out.println("Student " + feedbackStudentUsername + " not found.");
                        break;
                    }
                    System.out.print("Enter feedback: ");
                    String feedback = scanner.nextLine();
                    quiz.provideFeedback(feedbackStudentUsername, feedback);
                    break;
                case 7:
                    // List all student details
                    System.out.println("\nList of all student details:");
                    for (Student student : quiz.students.values()) {
                    	System.out.println("Username: " + student.username + ", Full Name: " + student.fullname + ", Semester: " + ((Student)student).getSemester() + ", Feedback: " + quiz.feedbacks.getOrDefault(student.username, "No feedback"));
                    }
                    break;
                case 8:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}

// Class representing a student user that extends User class and has an additional attribute Semester (currently not implemented fully)
class Student extends User {
	private int semester;
    // Constructor to initialize student attributes
    public Student(String username, String password, String fullname, int semester) {
        super(username, password, fullname);
        this.semester=semester;
    }

    public int getSemester() {
    	return semester;
    }
    
    // Currently not implemented
    @Override
    public void menu(QuizImpl quiz) {
        // Out of the scope of this sub-system
    }
}

// Interface representing quiz functionalities
interface Quiz {
    void createQuiz(String quizName, List<String> questions, List<String> answers);
    void setInstructions(String quizName, String instructions);
    void checkQuizStatus(String quizName);
    int assignGrades(String studentUsername, String quizName, List<String> studentAnswers);
    void generateReports(String quizName);
    void provideFeedback(String studentUsername, String feedback);
}

// Class implementing quiz functionalities
class QuizImpl implements Quiz {
    Map<String, List<String>> quizzes;
    Map<String, String> instructions;
    Map<String, List<String>> quizStatus;
    Map<String, List<String>> studentAnswers;
    Map<String, String> feedbacks;
    Map<String, List<String>> quizAnswers;
    Map<String, List<String>> quizTakers;
    Map<String, Student> students;
    private static QuizImpl instance;

    // Private constructor to restrict instantiation from outside
    private QuizImpl() {
        quizzes = new HashMap<>();
        instructions = new HashMap<>();
        quizStatus = new HashMap<>();
        studentAnswers = new HashMap<>();
        feedbacks = new HashMap<>();
        quizAnswers = new HashMap<>();
        quizTakers = new HashMap<>();
        students = new HashMap<>();
    }

    // Method to get the singleton instance of QuizImpl
    public static synchronized QuizImpl getInstance() {
        if (instance == null) {
            instance = new QuizImpl();
        }
        return instance;
    }

    // Method to create a new quiz
    @Override
    public void createQuiz(String quizName, List<String> questions, List<String> answers) {
        quizzes.put(quizName, questions);
        quizAnswers.put(quizName, answers);
        quizStatus.put(quizName, new ArrayList<>());
        for (int i = 0; i < questions.size(); i++) {
            quizStatus.get(quizName).add("Unanswered");
        }
        System.out.println("Quiz " + quizName + " created successfully.");
    }

    // Method to set instructions for a quiz
    @Override
    public void setInstructions(String quizName, String instructions) {
        this.instructions.put(quizName, instructions);
    }

    // Method to check the status of a quiz
    @Override
    public void checkQuizStatus(String quizName) {
        System.out.println("\nDetails of quiz: " + quizName);
        System.out.println("Instructions: " + instructions.getOrDefault(quizName, "No instructions provided."));
        System.out.println("List of questions: " + quizzes.get(quizName));
        System.out.println("List of correct answers: " + quizAnswers.get(quizName));
        System.out.println("List of student usernames who took the quiz: " + quizTakers.getOrDefault(quizName, Collections.emptyList()));
    }

    // Method to assign grades to a student for a quiz
    @Override
    public int assignGrades(String studentUsername, String quizName, List<String> studentAnswers) {
        List<String> correctAnswers = quizAnswers.get(quizName);
        int score = 0;
        for (int i = 0; i < studentAnswers.size(); i++) {
            if (studentAnswers.get(i).equalsIgnoreCase(correctAnswers.get(i))) {
                score++;
            }
        }
        int total = studentAnswers.size();
        int percentage = (score * 100) / total;
        System.out.println("Student " + students.get(studentUsername).fullname + " scored " + percentage + "% in quiz " + quizName);
        // Add the student to the list of attempts
        quizTakers.computeIfAbsent(quizName, k -> new ArrayList<>()).add(studentUsername);
        // Add student answers for report generation
        this.studentAnswers.put(studentUsername, studentAnswers);
        return percentage;
    }

    // Method to generate reports for a quiz
    @Override
    public void generateReports(String quizName) {
        System.out.println("\nReport for quiz: " + quizName);
        System.out.println("Student IDs who attempted the quiz: " + quizTakers.getOrDefault(quizName, Collections.emptyList()));
        System.out.println("Their answers and grades:");
        for (Map.Entry<String, List<String>> entry : studentAnswers.entrySet()) {
            String studentUsername = entry.getKey();
            List<String> answers = entry.getValue();
            int score = 0;
            List<String> correctAnswers = quizAnswers.get(quizName);
            for (int i = 0; i < answers.size(); i++) {
                if (answers.get(i).equalsIgnoreCase(correctAnswers.get(i))) {
                    score++;
                }
            }
            int total = answers.size();
            int percentage = (score * 100) / total;
            System.out.println("Student " + students.get(studentUsername).fullname + " - Answers: " + answers + ", Grade: " + percentage + "%");
        }
    }

    // Method to provide feedback to a student
    @Override
    public void provideFeedback(String studentUsername, String feedback) {
        feedbacks.put(studentUsername, feedback);
        System.out.println("Feedback provided for student " + students.get(studentUsername).fullname);
    }
}

// Main class to run the program
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        QuizImpl quiz = QuizImpl.getInstance();

        // Create dummy admin object
        Admin admin = new Admin("admin", "adminpass", "Admin Jowel");
        
     // Create dummy student objects
        quiz.students.put("s1", new Student("s1", "qweqwe", "John Doe",1));
        quiz.students.put("s2", new Student("s2", "asdasd", "Julia Roberts",1));
        quiz.students.put("s3", new Student("s3", "zxczcx", "Jane Dawson",2));

        while (true) {
            System.out.println("\nWelcome to Quiz Management System");
            System.out.println("1. Admin Login");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String adminUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String adminPassword = scanner.nextLine();
                    if (admin.authenticate(adminUsername, adminPassword)) {
                        System.out.println("Admin login successful!");
                        admin.menu(quiz);
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                    }
                    break;
                case 2:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }
}
