package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXAM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAX_SCORE;

import java.util.stream.Stream;

import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.EditScoreCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.examscore.Exam;

/**
 * Parses input arguments and creates a new EditScoreCommand object
 */
public class EditScoreCommandParser implements Parser<EditScoreCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditScoreCommand
     * and returns an EditScoreCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditScoreCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_MAX_SCORE, PREFIX_EXAM);

        if (!arePrefixesPresent(argMultimap, PREFIX_MAX_SCORE, PREFIX_EXAM)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditScoreCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_EXAM, PREFIX_MAX_SCORE);

        String examName = argMultimap.getValue(PREFIX_EXAM).get().toLowerCase();
        Exam exam = ParserUtil.parseExam(examName);

        String score = argMultimap.getValue(PREFIX_MAX_SCORE).get();
        if (!StringUtil.isUnsignedInteger(score)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    Exam.MESSAGE_SCORE_INVALID_INTEGER));
        }
        int scoreInt = Integer.parseInt(score);

        return new EditScoreCommand(exam, scoreInt);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
