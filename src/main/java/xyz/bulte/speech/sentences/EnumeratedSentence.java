    package xyz.bulte.speech.sentences;


    import org.apache.commons.lang3.StringUtils;
    import xyz.bulte.speech.Sentence;
    import lombok.AllArgsConstructor;
    import lombok.ToString;
    import org.jooq.lambda.tuple.Tuple;
    import xyz.bulte.speech.sentences.helper.SentenceHelper;

    import java.util.List;
    import java.util.function.Predicate;
    import java.util.stream.Collectors;

    import static java.util.function.Predicate.not;

    @ToString
    @AllArgsConstructor
    public class EnumeratedSentence<T extends Tuple> implements Sentence<List<T>> {

        private String template;
        private List<T> data;


        @Override
        public String getTemplate() {
            return template;
        }

        @Override
        public List<T> getData() {
            return data;
        }

        @Override
        public boolean isValid() {
            return data.stream()
                    .noneMatch(not(entry -> SentenceHelper.doesTupleMatchTemplate(getTemplate(), entry)));
        }

        @Override
        public String asString() {
            return getData()
                    .stream()
                    .map(tuple -> SentenceHelper.fillTemplateWithDataFromTuple(template, tuple))
                    .collect(Collectors.joining(" and "))
                    .strip();
        }
    }
