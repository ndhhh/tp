package seedu.address.model.person.examscore;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represents a Person's exam scores in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidExamScores(Score[])}
 */
public class ExamScores {

    private final Score[] arrayOfScores;

    /**
     * Creates an {@code ExamScores} object from an array of {@code Score}.
     */
    public ExamScores(Score[] arrayOfScores) {
        requireNonNull(arrayOfScores);
        checkArgument(isValidExamScores(arrayOfScores));
        this.arrayOfScores = arrayOfScores;
    }

    public static int getNumOfExams() {
        return ExamList.numOfExams();
    }

    /**
     * Returns a copy of ExamScores object with appropriate score replaced with new input score
     *
     * @param newScore Score object representing new score to update object with
     * @return new ExamScores object updated with new Score
     */
    public ExamScores updateScore(Score newScore) {
        Score[] arrayOfScoresCopy = this.arrayOfScores.clone();
        for (int i = 0; i < arrayOfScores.length; i++) {
            if (arrayOfScoresCopy[i].getExam() == newScore.getExam()) {
                arrayOfScoresCopy[i] = newScore;
                break;
            }
        }
        return new ExamScores(arrayOfScoresCopy);
    }

    /**
     * Returns an immutable score array, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Score[] getArrayOfScores() {
        return arrayOfScores.clone();
    }

    /**
     * Returns an optional integer representing the score of a particular exam.
     * @param exam The exam to search for, either midterm or final.
     * @return The optional integer exam score.
     */
    public Optional<Integer> getScoreByExam(Exam exam) {
        Optional<Integer> result = Optional.empty();
        for (Score s: this.arrayOfScores) {
            if (s.getExam().equals(exam)) {
                result = s.getScore();
                break;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        String out = arrayOfScores[0].toString();
        for (int i = 1; i < arrayOfScores.length; i++) {
            out += "\n";
            out += arrayOfScores[i].toString();
        }
        return out;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExamScores)) {
            return false;
        }

        ExamScores otherExamScores = (ExamScores) other;
        for (int i = 0; i < arrayOfScores.length; i++) {
            if (!arrayOfScores[i].equals(otherExamScores.arrayOfScores[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static ExamScores getEmptyExamScores() {
        Score[] arrayOfScores = new Score[ExamList.numOfExams()];
        List<Exam> examList = ExamList.values();
        for (int i = 0; i < arrayOfScores.length; i++) {
            arrayOfScores[i] = Score.getUnrecordedScore(examList.get(i));
        }
        return new ExamScores(arrayOfScores);
    }

    /**
     * Returns true if a given array of Scores represents a valid set of exam scores.
     *
     * @param arrayOfScores
     */
    public static boolean isValidExamScores(Score[] arrayOfScores) {
        if (arrayOfScores.length != ExamList.numOfExams()) {
            return false;
        }

        List<Exam> examList = ExamList.values();
        for (int i = 0; i < ExamList.numOfExams(); i++) {
            if (arrayOfScores[i] == null) {
                return false;
            }
            if (examList.get(i) != arrayOfScores[i].getExam()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(arrayOfScores);
    }

    /**
     * Checks if the new max score is valid, by comparing it with the corresponding score for the given exam.
     * Returns {@Code Optional<Integer>} representing the recorded score for an exam, which is empty if the new
     * max score is valid.
     * @param exam the exam to be edited
     * @param newMaxScore the new max score to compare the recorded scores against
     */
    public Optional<Integer> newMaxScoreValid(Exam exam, int newMaxScore) {
        for (int i = 0; i < arrayOfScores.length; i++) {
            if (arrayOfScores[i].getExam().equals(exam)) {
                if (!arrayOfScores[i].isNewMaxScoreValid(newMaxScore)) {
                    return arrayOfScores[i].getScore();
                } else {
                    return Optional.empty();
                }
            }
        }
        throw new IllegalArgumentException("Exam " + exam.getName() + "not found in ExamScores");
    }
}
