package seedu.address.model.person.examscore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A list of all the exams in the system. This class is final and cannot be instantiated.
 */
public class ExamList {

    public static final Exam MIDTERM = new Exam("midterm", 70); //default max score is 70
    public static final Exam FINAL = new Exam("final", 100); //default max score is 100
    public static final String MESSAGE_CONSTRAINTS;

    private static final List<Exam> examList = new ArrayList<>();

    static {
        examList.add(MIDTERM);
        examList.add(FINAL);
        MESSAGE_CONSTRAINTS = "Exam name must be one of " + getAllExamNames();
    }

    private ExamList() { //prevent instantiation
    }

    public static void setMaxScore(String examName, int newMaxScore) {
        getExamFromName(examName).setMaxScore(newMaxScore);
    }

    public static List<Exam> values() {
        return Collections.unmodifiableList(examList);
    }

    public static int numOfExams() {
        return examList.size();
    }

    /**
     * Get associated Exam object from input examName.
     *
     * @param examName A valid exam name
     */
    public static Exam getExamFromName(String examName) {
        for (int i = 0; i < examList.size(); i++) {
            Exam currentExam = examList.get(i);
            if (examName.equals(currentExam.getName())) {
                return currentExam;
            }
        }

        throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns if a given string is a valid exam name.
     */
    public static boolean isValidExamName(String examName) {
        for (int i = 0; i < examList.size(); i++) {
            String currentExamName = examList.get(i).getName();
            if (examName.toLowerCase().equals(currentExamName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if a given Exam is a valid exam.
     */
    public static boolean isValidExam(Exam exam) {
        for (int i = 0; i < examList.size(); i++) {
            Exam currentExam = examList.get(i);
            if (exam.equals(currentExam)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a string with all exam names separated by commas.
     */
    public static String getAllExamNames() {
        String out = examList.get(0).getName();
        for (int i = 1; i < examList.size(); i++) {
            out += ", ";
            out += examList.get(i).getName();
        }
        return out;
    }
}
