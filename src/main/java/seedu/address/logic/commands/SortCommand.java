package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXAM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.person.examscore.Exam;

/**
 * Sorts the address book based on a parameter.
 * Parameter can be either name or exam scores (midterm/final).
 * @author ndhhh
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_SUCCESS_NAME = "Sorted all people by name";
    public static final String MESSAGE_SUCCESS_EXAM = "Sorted all people by exam score";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts the list of people. Use either "
            + PREFIX_NAME + " for sorting by names, or "
            + PREFIX_EXAM + " for sorting by exam scores.\n"
            + "Exams can be sorted by either midterm or final.\n"
            + "Examples: sort " + PREFIX_NAME + "\nor sort " + PREFIX_EXAM
            + "midterm or sort " + PREFIX_EXAM + "final";

    private final Prefix prefix;
    private final Exam exam;

    /**
     * Creates a new SortCommand to sort the list
     * @param prefix Prefix to sort by name {@code PREFIX_NAME}
     */
    public SortCommand(Prefix prefix) {
        this.prefix = prefix;
        this.exam = null;
    }

    /**
     * Creates a new SortCommand to sort the list.
     * @author ndhhh
     * @param prefix Prefix to sort by exam {@code PREFIX_EXAM}
     * @param exam Exam to sort by if {@code PREFIX_EXAM}, null otherwise
     */
    public SortCommand(Prefix prefix, Exam exam) {
        this.prefix = prefix;
        this.exam = exam;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        switch (prefix.getPrefix()) {
        case "n/":
            // If sorting by name, just call model to sort
            model.sortPersonsByName();
            return new CommandResult(MESSAGE_SUCCESS_NAME);
        case "ex/":
            // If sorting by exam, call model to sort based on exam type
            if (this.exam == null) {
                throw new CommandException(MESSAGE_USAGE);
            }
            model.sortPersonsByExam(this.exam);
            return new CommandResult(MESSAGE_SUCCESS_EXAM);
        default:
            throw new CommandException(MESSAGE_USAGE);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand c = (SortCommand) other;

        if (this.exam == null && c.exam == null) {
            return this.prefix.equals(c.prefix);
        }

        if (this.exam != null && c.exam != null) {
            return this.prefix.equals(c.prefix) && this.exam.equals(c.exam);
        }

        return false;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Prefix", prefix)
                .toString();
    }
}
