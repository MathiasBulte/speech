package xyz.bulte.speech;

import lombok.ToString;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import xyz.bulte.speech.sentences.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@ToString
public class Speech {

    private List<Sentence> sentences;

    @java.beans.ConstructorProperties({"sentences"})
    private Speech(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public String asString() {
        return sentences.stream()
                .map(Sentence::asString)
                .collect(Collectors.joining(". ", "", "."));
    }

    public static SpeechBuilder builder() {
        return new SpeechBuilder();
    }

    static class SpeechBuilder {
        private List<Sentence> sentences = new ArrayList<>();

        public SpeechBuilder sentence(String sentence) {
            sentences.add(Sentence.of(sentence));
            return this;
        }

        public <T extends Tuple> SpeechBuilder sentence(String template, T data) {
            sentences.add(Sentence.of(template, data));
            return this;
        }

        public <T extends Tuple> SpeechBuilder sentence(String template, T data, Function<T, String> transform) {
            sentences.add(Sentence.of(template, data, transform));
            return this;
        }

        @SafeVarargs
        public final <T extends Tuple> SpeechBuilder sentence(String template, T data, Transform<T>... transform) {
            sentences.add(Sentence.of(template, data, transform));
            return this;
        }

        public SpeechBuilder enumeration(String template, List<Tuple> data) {
            sentences.add(Sentence.of(template, data));
            return this;
        }

        public Speech build() {
            List<Sentence> invalidSentences = sentences
                    .stream()
                    .map(sentence -> Tuple.tuple(sentence, sentence.isValid()))
                    .filter(tuple -> !tuple.v2)
                    .map(Tuple2::v1)
                    .collect(Collectors.toList());

            if (!invalidSentences.isEmpty()) {
                throw new RuntimeException("Sentences that could not be parsed: " + invalidSentences);
            }

            return new Speech(sentences);
        }

        public SpeechBuilder sentences(List<Sentence> sentences) {
            this.sentences = sentences;
            return this;
        }

        public String toString() {
            return "Speech.SpeechBuilder(sentences=" + this.sentences + ")";
        }
    }

}
