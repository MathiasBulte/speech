package xyz.bulte.speech;

import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.tuple.Tuple;
import xyz.bulte.speech.sentences.*;

import java.util.List;
import java.util.function.Function;

public interface Sentence<T> {

    String getTemplate();

    T getData();

    default boolean isValid() {
        return StringUtils.isNotBlank(getTemplate());
    }

    String asString();


    static Sentence of(String sentence) {
        return new SimpleSentence(sentence);
    }

    static Sentence of(String template, Tuple data) {
        return new TemplateSentence<>(template, data);
    }

    static <T extends Tuple> Sentence of(String template, T data, Function<T, String> transformer) {
        return new FunctionTemplateSentence<>(template, data, transformer);
    }

    static Sentence of(String template, List<Tuple> data) {
        return new EnumeratedSentence<>(template, data);
    }

    static <T extends Tuple> Sentence of(String template, T data, Transform<T>[] transformers) {
        return new TransformerTemplateSentence<>(template, data, transformers);
    }

}
