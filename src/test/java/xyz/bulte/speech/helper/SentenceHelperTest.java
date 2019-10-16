package xyz.bulte.speech.helper;

import org.jooq.lambda.tuple.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SentenceHelperTest {

    @Test
    void fillTemplateWithDataFromTupleWhenTemplateMatchesTuple() {
        var template = "{} {} {} {}";
        var tuple = Tuple.tuple(5, 6, 7, 8);
        var expected = "5 6 7 8";

        var result = SentenceHelper.fillTemplateWithData(template, tuple);
        assertEquals(expected, result);
    }

    @Test
    void fillTemplateWithDataFromTupleWhenTemplateHasMoreParametersThanTuple() {
        var template = "{} {} {} {} {}";
        var tuple = Tuple.tuple(5, 6, 7, 8);
        var expected = "5 6 7 8 {}";

        var result = SentenceHelper.fillTemplateWithData(template, tuple);
        assertEquals(expected, result);
    }

    @Test
    void fillTemplateWithDataFromTupleWhenTupleHasMoreParametersThanTemplate() {
        var template = "{} {} {}";
        var tuple = Tuple.tuple(5, 6, 7, 8);
        var expected = "5 6 7";

        var result = SentenceHelper.fillTemplateWithData(template, tuple);
        assertEquals(expected, result);
    }

    @Test
    void doesTupleMatchTemplateWhenItMatches() {
        var template = "{} {} {} {}";
        var tuple = Tuple.tuple(5, 6, 7, 8);

        var result = SentenceHelper.doesTupleMatchTemplate(template, tuple);
        assertTrue(result);
    }

    @Test
    void doesTupleMatchTemplateWhenTupleHasMoreParametersThanTemplate() {
        var template = "{} {} {} {}";
        var tuple = Tuple.tuple(5, 6, 7, 8, 9);

        var result = SentenceHelper.doesTupleMatchTemplate(template, tuple);
        assertFalse(result);
    }

    @Test
    void doesTupleMatchTemplateWhenTemplateHasMoreParametersThanTuple() {
        var template = "{} {} {} {}";
        var tuple = Tuple.tuple(5, 6, 7);

        var result = SentenceHelper.doesTupleMatchTemplate(template, tuple);
        assertFalse(result);
    }
}