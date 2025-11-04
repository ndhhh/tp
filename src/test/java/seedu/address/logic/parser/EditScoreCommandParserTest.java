package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.EXAM_DESC;
import static seedu.address.logic.commands.CommandTestUtil.SCORE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EXAM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXAM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAX_SCORE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditScoreCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.examscore.Exam;
import seedu.address.model.person.examscore.ExamList;

public class EditScoreCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditScoreCommand.MESSAGE_USAGE);

    private static final String MESSAGE_INVALID_EXAM_SCORE =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, Exam.MESSAGE_SCORE_INVALID_INTEGER);

    private final EditScoreCommandParser parser = new EditScoreCommandParser();

    @BeforeEach
    public void resetExamList() {
        ExamList.setMaxScore("midterm", 80);
        ExamList.setMaxScore("final", 100);
    }

    @Test
    public void parse_success() {
        EditScoreCommand expectedCommand;

        expectedCommand =
                new EditScoreCommand(ExamList.getExamFromName(VALID_EXAM), 105);
        String userinput = EditScoreCommand.COMMAND_WORD + " "
                + PREFIX_EXAM + VALID_EXAM + " " + PREFIX_MAX_SCORE + "105";
        assertParseSuccess(parser, userinput, expectedCommand);

        expectedCommand =
                new EditScoreCommand(ExamList.getExamFromName(VALID_EXAM), 106);
        String uppercaseExam = EditScoreCommand.COMMAND_WORD + " "
                + PREFIX_EXAM + VALID_EXAM.toUpperCase() + " " + PREFIX_MAX_SCORE + "106";
        assertParseSuccess(parser, uppercaseExam, expectedCommand);

        expectedCommand =
                new EditScoreCommand(ExamList.getExamFromName("midterm"), 107);
        String mixedCaseExam = EditScoreCommand.COMMAND_WORD + " "
                + PREFIX_EXAM + "midTerm" + " " + PREFIX_MAX_SCORE + "107";
        assertParseSuccess(parser, mixedCaseExam, expectedCommand);
    }

    @Test
    public void parse_missingPrefix_failure() {
        assertParseFailure(parser, SCORE_DESC, MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser, EXAM_DESC, MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_duplicatePrefix_throwsParseException() {
        String inputDuplicateExam = " " + PREFIX_EXAM + "midterm " + PREFIX_EXAM + "final "
                + PREFIX_MAX_SCORE + "105";
        String inputDuplicateScore = " " + PREFIX_EXAM + "midterm " + PREFIX_MAX_SCORE + "105 "
                + PREFIX_MAX_SCORE + "105";

        assertThrows(ParseException.class, () -> parser.parse(inputDuplicateExam));
        assertThrows(ParseException.class, () -> parser.parse(inputDuplicateScore));
    }

    @Test
    public void parse_invalidScore_throwsParseException() {
        String inputNegativeScore = " " + PREFIX_EXAM + "midterm " + PREFIX_MAX_SCORE + "-1";
        String inputNotNumeric = " " + PREFIX_EXAM + "midterm " + PREFIX_MAX_SCORE + "one";
        String inputNotInteger = " " + PREFIX_EXAM + "midterm " + PREFIX_MAX_SCORE + "80.5";

        assertParseFailure(parser, inputNegativeScore, MESSAGE_INVALID_EXAM_SCORE);
        assertParseFailure(parser, inputNotNumeric, MESSAGE_INVALID_EXAM_SCORE);
        assertParseFailure(parser, inputNotInteger, MESSAGE_INVALID_EXAM_SCORE);
    }

    @Test
    public void parse_invalidExam_throwsParseException() {
        String inputInvalidExam = " " + PREFIX_EXAM + "invalidExam " + PREFIX_MAX_SCORE + "105";

        assertThrows(ParseException.class, () -> parser.parse(inputInvalidExam));
    }
}
