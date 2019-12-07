package xyz.bulte.speech.api;

import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple1;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.jupiter.api.Test;
import xyz.bulte.speech.api.Speech;
import xyz.bulte.speech.api.Transform;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static xyz.bulte.speech.api.Transform.of;
import static xyz.bulte.speech.api.Transform.plural;


public class SpeechTest {

    @Test
    public void testTemplateSentence() {
        var speech = Speech.builder()
                .sentence("{} application is having trouble", tuple(1))
                .build();

        assertEquals("1 application is having trouble.", speech);
    }

    @Test
    public void testEnumeration() {
        var speech = Speech.builder()
                .enumeration("{} {} {} {}", List.of(
                        tuple("It's", "time", "to", "begin"),
                        tuple("now", "count", "it", "in"),
                        tuple("5", "6", "7", "8")))
                .build();

        assertEquals("It's time to begin, now count it in and 5 6 7 8.", speech);
    }

    @Test
    public void testCreationWithTransformerFunctionWhenConditionIsTrue() {
        var speech = Speech.builder()
                .sentence("{} {} having trouble", tuple(2),
                        of(tuple -> tuple.v1 > 1, "application is", "applications are"))
                .build();

        assertEquals("2 applications are having trouble.", speech);
    }

    @Test
    public void testCreationWithTransformerFunctionWhenConditionIsFalse() {
        var speech = Speech.builder()
                .sentence("{} {} having trouble", tuple(1),
                        of(tuple -> tuple.v1 > 1, "application is", "applications are"))
                .build();

        assertEquals("1 application is having trouble.", speech);
    }

    @Test
    public void testCreationWithTransformerFunctionWhenIdentifierIsPresent() {
        var speech = Speech.builder()
                .sentence("{} {} having trouble", tuple(1),
                        new Transform<>(tuple -> tuple.v1 > 1, "application is", "applications are"))
                .build();

        assertEquals("1 application is having trouble.", speech);
    }

    @Test
    public void testCreationWithPluralTransformerFunctionWhenNotPlural() {
        var speech = Speech.builder()
                .sentence("{} {} having trouble",
                        tuple(1),
                        plural(Tuple1::v1, "application is", "applications are"))
                .build();

        assertEquals("1 application is having trouble.", speech);
    }

    @Test
    public void testCreationWithPluralTransformerFunctionWhenItIsPlural() {
        var speech = Speech.builder()
                .sentence("{} {} having trouble",
                        tuple(2),
                        plural(Tuple1::v1, "application is", "applications are"))
                .build();

        assertEquals("2 applications are having trouble.", speech);
    }

    @Test
    public void testRealisticScenario() {
        var speech = Speech.builder()
                .sentence("{} {} having trouble", tuple(2),
                        plural(Tuple1::v1, "application is", "applications are"))
                .enumeration("{} out of {} of the {} instances are down",
                        List.of(tuple(1, 2, "alexa-service"),
                                tuple(2, 4, "adoption-service")))
                .build();

        assertEquals("2 applications are having trouble. 1 out of 2 of the alexa-service instances are down " +
                "and 2 out of 4 of the adoption-service instances are down.", speech);
    }

    @Test
    public void testArrayOfTransformers() {
        var speech = Speech.builder()
                .sentence("{} {singularOrPlural} having trouble and Mathias is the {truth}", tuple(2, 0),
                        plural(Tuple2::v1, "application is", "applications are", "singularOrPlural"),
                        of(tuple -> tuple.v2 == 1, "best", "most awesome person ever", "truth")
                )
                .build();

        assertEquals("2 applications are having trouble and Mathias is the best.", speech);
    }

    @Test
    public void testTransformerWhenSentenceOnlyHasTransformer() {
        var speech = Speech.builder()
                .sentence("Good evening")
                .enumeration("{} the highs will reach {} degrees Celsius",
                        List.of(
                                tuple("today", 25),
                                tuple("tomorrow", 22),
                                tuple("tuesday", 23)
                        ))
                .sentence("Seems like it'll be very {typeOfWeather} weather",
                        tuple(25),
                        of(tuple -> tuple.v1 > 20, "bad", "nice", "typeOfWeather"))
                .build();

        assertEquals("Good evening. today the highs will reach 25 degrees Celsius, tomorrow the highs will reach 22 degrees Celsius and tuesday the highs will reach 23 degrees Celsius. " +
                "Seems like it'll be very nice weather.", speech);
    }

    @Test
    public void bla() {
        String singular = Speech.builder()
                .sentence("{} {} having trouble",
                        tuple(1),
                        plural(Tuple1::v1, "application is", "applications are"))
                .build();

        assertEquals("1 application is having trouble.", singular);

        String plural = Speech.builder()
                .sentence("{} {} having trouble",
                        tuple(2),
                        plural(Tuple1::v1, "application is", "applications are"))
                .build();

        assertEquals("2 applications are having trouble.", plural);
    }
}