package xyz.bulte.speech.sentences;

import org.jooq.lambda.tuple.Tuple;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class Transform<T> implements Function<T, String> {

    private Predicate<T> predicate;
    private String ifFalse;
    private String ifTrue;
    private String identifier;

    public Transform(Predicate<T> predicate, String ifFalse, String ifTrue) {
        this.predicate = predicate;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    public Transform(Predicate<T> predicate, String ifFalse, String ifTrue, String identifier) {
        this.predicate = predicate;
        this.ifFalse = ifFalse;
        this.ifTrue = ifTrue;
        this.identifier = identifier;
    }

    public static <T> Transform<T> of(Predicate<T> predicate, String ifFalse, String ifTrue) {
        return new Transform<>(predicate, ifFalse, ifTrue);
    }

    public static <T> Transform<T> of(Predicate<T> predicate, String ifFalse, String ifTrue, String identifier) {
        return new Transform<>(predicate, ifFalse, ifTrue, identifier);
    }

    public static <T> Transform<T> plural(Function<T, Number> function, String whenSingular, String whenPlural) {
        return new Transform<>(data -> function.apply(data).intValue() > 1, whenSingular, whenPlural);
    }

    public static <T> Transform<T> plural(Function<T, Number> function, String whenSingular, String whenPlural, String identifier) {
        return new Transform<>(data -> function.apply(data).intValue() > 1, whenSingular, whenPlural, identifier);
    }

    @Override
    public String apply(T tuple) {
        if (predicate.test(tuple)) {
            return ifTrue;
        } else {
            return ifFalse;
        }
    }

    public Optional<String> getIdentifier() {
        return Optional.ofNullable(identifier);
    }
}
