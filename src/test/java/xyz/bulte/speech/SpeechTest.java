package xyz.bulte.speech;

import org.jooq.lambda.tuple.Tuple1;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.jupiter.api.Test;
import xyz.bulte.speech.sentences.Transform;

import java.util.List;

import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static xyz.bulte.speech.sentences.Transform.of;
import static xyz.bulte.speech.sentences.Transform.plural;


public class SpeechTest {

    @Test
    public void testTemplateSentence() {
        Speech speech = Speech.builder()
                .sentence("{} application is having trouble", tuple(1))
                .build();

        assertEquals("1 application is having trouble.", speech.asString());
    }

    @Test
    public void testEnumeration() {
        Speech speech = Speech.builder()
                .enumeration("{} out of {} of the {} instances are down", List.of(tuple(1, 2, "alexa-service")))
                .build();

        assertEquals("1 out of 2 of the alexa-service instances are down.", speech.asString());
    }

    @Test
    public void testCreationWithTransformerFunctionWhenConditionIsTrue() {
        Speech build = Speech.builder()
                .sentence("{} {} having trouble", tuple(2),
                        of(tuple -> tuple.v1 > 1, "application is", "applications are"))
                .build();;

        assertEquals("2 applications are having trouble.", build.asString());
    }

    @Test
    public void testCreationWithTransformerFunctionWhenConditionIsFalse() {
        Speech build = Speech.builder()
                .sentence("{} {} having trouble", tuple(1),
                        of(tuple -> tuple.v1 > 1, "application is", "applications are"))
                .build();

        assertEquals("1 application is having trouble.", build.asString());
    }

    @Test
    public void testCreationWithTransformerFunctionWhenIdentifierIsPresent() {
        Speech build = Speech.builder()
                .sentence("{} {} having trouble", tuple(1),
                        new Transform<>(tuple -> tuple.v1 > 1, "application is", "applications are"))
                .build();

        assertEquals("1 application is having trouble.", build.asString());
    }

    @Test
    public void testCreationWithPluralTransformerFunctionWhenNotPlural() {
        Speech speech = Speech.builder()
                .sentence("{} {} having trouble", tuple(1),
                        plural(Tuple1::v1, "application is", "applications are"))
                .build();

        assertEquals("1 application is having trouble.", speech.asString());
    }

    @Test
    public void testCreationWithPluralTransformerFunctionWhenItIsPlural() {
        Speech build = Speech.builder()
                .sentence("{} {} having trouble", tuple(2),
                        plural(Tuple1::v1, "application is", "applications are"))
                .build();

        assertEquals("2 applications are having trouble.", build.asString());
    }

    @Test
    public void testRealisticScenario() {
        var speech = Speech.builder()
                .sentence("{} {} having trouble", tuple(2),
                        plural(Tuple1::v1, "application is", "applications are"))
                .enumeration("{} out of {} of the {} instances are down",
                        List.of(tuple(1, 2, "alexa-service"),
                                tuple(2, 4, "adoption-service")))
                .sentence("{} is de {}", tuple("Mathias", "beste"))
                .build();

        assertEquals("2 applications are having trouble. 1 out of 2 of the alexa-service instances are down " +
                "and 2 out of 4 of the adoption-service instances are down. Mathias is de beste.", speech.asString());
    }

    @Test
    public void testArrayOfTransformers() {
        var speech = Speech.builder()
                .sentence("{} {singularOrPlural} having trouble and Mathias is the {truth}", tuple(2, 0),
                        plural(Tuple2::v1, "application is", "applications are", "singularOrPlural"),
                        of(tuple -> tuple.v2 == 1, "best", "most awesome person ever", "truth")
                )
                .build();

        assertEquals("2 applications are having trouble and Mathias is the best.", speech.asString());
    }

    @Test
    public void testCreationWithPluralTransformer() {
        Speech.builder()
//                .sentence("{} {} having trouble", 1, plural("application is", "applications are"))
                .build();
    }

}