package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXAM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAX_SCORE;

import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.examscore.Exam;
import seedu.address.model.person.examscore.ExamList;
import seedu.address.model.person.examscore.ExamScores;

/**
 * Edits the max score of an existing exam.
 */
public class EditScoreCommand extends Command {

    public static final String COMMAND_WORD = "maxscore";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the max score of the exam identified "
            + "to the new given score. "
            + "Existing values will be overwritten by the input values.\n"
            + PREFIX_EXAM + "EXAM "
            + PREFIX_MAX_SCORE + "SCORE "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_EXAM + "midterm "
            + PREFIX_MAX_SCORE + "50";

    public static final String MESSAGE_EDIT_MAX_SCORE_SUCCESS = "Edited Exam %1$s max score to %2$d";
    public static final String MESSAGE_DUPLICATE_SCORE = "Given score is the same as the existing score for this exam.";
    public static final String MESSAGE_MAX_SCORE_INVALID =
            "Max score cannot be less than the recorded score of any student. The highest recorded score is %d";
    public static final String MESSAGE_EXAM_INVALID = ExamList.MESSAGE_CONSTRAINTS;


    private final Exam exam;
    private final int score;

    /**
     * @param exam the exam to edit
     * @param score the new max score
     */
    public EditScoreCommand(Exam exam, int score) {
        requireAllNonNull(exam, score);
        this.exam = exam;
        this.score = score;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!ExamList.isValidExam(this.exam)) {
            throw new CommandException(MESSAGE_EXAM_INVALID);
        }

        if (exam.getMaxScore() == this.score) {
            throw new CommandException(MESSAGE_DUPLICATE_SCORE);
        }

        Optional<Integer> highestRecordedScore = model.isNewMaxScoreValid(exam, this.score);
        if (highestRecordedScore.isPresent()) {
            throw new CommandException(String.format(MESSAGE_MAX_SCORE_INVALID, highestRecordedScore.get()));
        }

        this.exam.setMaxScore(this.score);

        for (Person person : model.getFilteredPersonList()) { //to display the new max scores on the person cards
            ExamScores updateMaxScore = new ExamScores(person.getExamScores().getArrayOfScores());
            Person updatePerson = new Person.PersonBuilder(person).withExamScores(updateMaxScore).build();
            model.setPerson(person, updatePerson);
        }

        return new CommandResult(String.format(MESSAGE_EDIT_MAX_SCORE_SUCCESS, this.exam, this.score));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditScoreCommand)) {
            return false;
        }

        EditScoreCommand otherEditScoreCommand = (EditScoreCommand) other;
        return this.exam.equals(otherEditScoreCommand.exam)
                && this.score == otherEditScoreCommand.score;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("exam", this.exam)
                .add("score", this.score)
                .toString();
    }
}
