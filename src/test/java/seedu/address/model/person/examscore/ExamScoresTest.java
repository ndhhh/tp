package seedu.address.model.person.examscore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalExamScores.EMPTY_EXAM_SCORES;
import static seedu.address.testutil.TypicalScores.FINAL_SCORE_A;
import static seedu.address.testutil.TypicalScores.FINAL_SCORE_UNRECORDED;
import static seedu.address.testutil.TypicalScores.MIDTERM_SCORE_A;
import static seedu.address.testutil.TypicalScores.MIDTERM_SCORE_B;
import static seedu.address.testutil.TypicalScores.MIDTERM_SCORE_UNRECORDED;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ExamScoresTest {

    @BeforeEach
    public void resetExamsList() {
        ExamList.setMaxScore("midterm", 70);
        ExamList.setMaxScore("final", 100);
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ExamScores(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ExamScores(
                new Score[] {null, null}));
        assertThrows(IllegalArgumentException.class, () -> new ExamScores(
                new Score[] {MIDTERM_SCORE_A, MIDTERM_SCORE_A}));
    }

    @Test
    public void getEmptyExamScores() {
        assertEquals(MIDTERM_SCORE_UNRECORDED, ExamScores.getEmptyExamScores().getArrayOfScores()[0]);
        assertEquals(FINAL_SCORE_UNRECORDED, ExamScores.getEmptyExamScores().getArrayOfScores()[1]);
        assertEquals(MIDTERM_SCORE_UNRECORDED.toString() + "\n"
                        + FINAL_SCORE_UNRECORDED.toString(),
                ExamScores.getEmptyExamScores().toString());
    }

    @Test
    public void updateScore() {
        assertEquals(EMPTY_EXAM_SCORES.updateScore(MIDTERM_SCORE_A).getArrayOfScores()[0],
                MIDTERM_SCORE_A);
        assertEquals(EMPTY_EXAM_SCORES.updateScore(MIDTERM_SCORE_A).updateScore(MIDTERM_SCORE_B).getArrayOfScores()[0],
                MIDTERM_SCORE_B);
        assertEquals(EMPTY_EXAM_SCORES.updateScore(MIDTERM_SCORE_A).updateScore(FINAL_SCORE_A).getArrayOfScores()[0],
                MIDTERM_SCORE_A);
        assertEquals(EMPTY_EXAM_SCORES.updateScore(MIDTERM_SCORE_A).updateScore(FINAL_SCORE_A).getArrayOfScores()[1],
                FINAL_SCORE_A);
    }

    @Test
    public void isValidExamScores() {
        assertTrue(ExamScores.isValidExamScores(new Score[] {MIDTERM_SCORE_A, FINAL_SCORE_A}));

        assertFalse(ExamScores.isValidExamScores(new Score[]{MIDTERM_SCORE_A, FINAL_SCORE_A, null}));
        assertFalse(ExamScores.isValidExamScores(new Score[]{null, null}));

        assertFalse(ExamScores.isValidExamScores(new Score[]{MIDTERM_SCORE_A, MIDTERM_SCORE_A}));
        assertFalse(ExamScores.isValidExamScores(new Score[]{FINAL_SCORE_A, FINAL_SCORE_A}));
        assertFalse(ExamScores.isValidExamScores(new Score[]{FINAL_SCORE_A, MIDTERM_SCORE_A}));
    }

    @Test
    public void getArrayOfScores() {
        Score[] originalArrayOfScores = new Score[] {MIDTERM_SCORE_A, FINAL_SCORE_A};
        ExamScores examScores = new ExamScores(originalArrayOfScores);
        Score[] newArrayOfScores = examScores.getArrayOfScores();

        // getArrayOfScores returns the score it was initialized with
        assertTrue(Arrays.equals(originalArrayOfScores, newArrayOfScores));
        assertEquals(examScores, new ExamScores(newArrayOfScores));

        // modifying output of getArrayOfScores doesnt change original value in examScores
        newArrayOfScores[0] = MIDTERM_SCORE_UNRECORDED;
        assertTrue(Arrays.equals(originalArrayOfScores, examScores.getArrayOfScores()));
    }

    @Test
    public void newMaxScoreValid() {
        Score[] arrayOfScores = new Score[] {MIDTERM_SCORE_A, FINAL_SCORE_A};
        ExamScores examScores = new ExamScores(arrayOfScores);

        // valid new max score > MIDTERM_SCORE_A = 50
        assertTrue(examScores.newMaxScoreValid(MIDTERM_SCORE_A.getExam(), 90).isEmpty());
        assertTrue(examScores.newMaxScoreValid(MIDTERM_SCORE_A.getExam(), 105).isEmpty());

        // invalid new max score < MIDTERM_SCORE_A = 50
        assertFalse(examScores.newMaxScoreValid(MIDTERM_SCORE_A.getExam(), 40).isEmpty());

        // exam name not found throws exception
        assertThrows(IllegalArgumentException.class, () ->
                examScores.newMaxScoreValid(new Exam("fake exam", 50), 100));
    }

    @Test
    public void equals() {
        ExamScores examScore = new ExamScores(new Score[] {MIDTERM_SCORE_A, FINAL_SCORE_A});

        // same values -> returns true
        assertTrue(examScore.equals(new ExamScores(new Score[] {MIDTERM_SCORE_A, FINAL_SCORE_A})));

        // same object -> returns true
        assertTrue(examScore.equals(examScore));

        // null -> returns false
        assertFalse(examScore.equals(null));

        // different types -> returns false
        assertFalse(examScore.equals(5.0f));

        // different values -> returns false
        assertFalse(examScore.equals(new ExamScores(new Score[] {MIDTERM_SCORE_B, FINAL_SCORE_A})));
    }
}
