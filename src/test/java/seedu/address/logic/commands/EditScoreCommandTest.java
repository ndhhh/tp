package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalScores.FINAL_SCORE_B;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.examscore.Exam;
import seedu.address.model.person.examscore.ExamList;
import seedu.address.model.person.examscore.ExamScores;

public class EditScoreCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @BeforeEach
    public void resetExamList() {
        ExamList.setMaxScore("midterm", 70);
        ExamList.setMaxScore("final", 100);
    }

    @Test
    public void execute_validEdit_success() throws CommandException {
        Exam exam = ExamList.getExamFromName("midterm");
        int newMaxScore = exam.getMaxScore() + 5;
        EditScoreCommand command = new EditScoreCommand(exam, newMaxScore);

        CommandResult result = command.execute(model);

        assertEquals(newMaxScore, ExamList.getExamFromName("midterm").getMaxScore());
    }
    @Test
    public void execute_invalidExam_throwsCommandException() {
        EditScoreCommand command = new EditScoreCommand(new Exam("o level", 60), 70);

        Exception exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(EditScoreCommand.MESSAGE_EXAM_INVALID, exception.getMessage());
    }

    @Test
    public void execute_duplicateScore_throwsCommandException() {
        EditScoreCommand command = new EditScoreCommand(ExamList.getExamFromName("midterm"), 70);

        Exception exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(EditScoreCommand.MESSAGE_DUPLICATE_SCORE, exception.getMessage());
    }

    @Test
    public void execute_scoreLessThanRecorded_throwsCommandException() {
        ExamScores newExamScores = ALICE.getExamScores().updateScore(FINAL_SCORE_B);
        Person updatedAlice = new Person.PersonBuilder(ALICE)
                .withExamScores(newExamScores)
                .build();
        model.setPerson(ALICE, updatedAlice);
        EditScoreCommand command = new EditScoreCommand(ExamList.getExamFromName("final"), 60);

        Exception exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(EditScoreCommand.MESSAGE_MAX_SCORE_INVALID, FINAL_SCORE_B.getScore().get()),
                exception.getMessage());
    }

    @Test
    public void equals() {
        Exam midtermExam = ExamList.getExamFromName("midterm");
        Exam finalExam = ExamList.getExamFromName("final");

        EditScoreCommand midtermTo50 = new EditScoreCommand(midtermExam, 50);
        EditScoreCommand finalTo50 = new EditScoreCommand(finalExam, 50);
        EditScoreCommand midtermTo200 = new EditScoreCommand(midtermExam, 200);
        EditScoreCommand finalTo200 = new EditScoreCommand(finalExam, 200);

        // True if same object
        assertTrue(midtermTo50.equals(midtermTo50));
        assertTrue(finalTo200.equals(finalTo200));

        // Same values -> returns true
        assertTrue(midtermTo50.equals(new EditScoreCommand(midtermExam, 50)));

        // different types -> returns false
        assertFalse(midtermTo50.equals(1));

        // null -> returns false
        assertFalse(midtermTo50.equals(null));

        // different exam -> returns false
        assertFalse(midtermTo50.equals(finalTo50));
        assertFalse(midtermTo200.equals(finalTo200));

        // different score -> returns false
        assertFalse(midtermTo50.equals(midtermTo200));
        assertFalse(finalTo50.equals(finalTo200));

        // different exam and score -> returns false
        assertFalse(midtermTo50.equals(finalTo200));
        assertFalse(finalTo50.equals(midtermTo200));

    }

    @Test
    public void toStringMethod() {
        String examName = "exam name";
        int examScore = 60;
        Exam exam = new Exam(examName, 20);
        EditScoreCommand editScoreCommand = new EditScoreCommand(exam, examScore);
        String expected =
                EditScoreCommand.class.getCanonicalName() + "{exam=" + examName + ", score=" + examScore + "}";
        assertEquals(expected, editScoreCommand.toString());
    }
}
