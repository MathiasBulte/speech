    package xyz.bulte.speech.sentences;


    import xyz.bulte.speech.api.Sentence;
    import lombok.AllArgsConstructor;
    import lombok.ToString;
    import org.jooq.lambda.tuple.Tuple;
    import xyz.bulte.speech.constant.Templating;
    import xyz.bulte.speech.helper.SentenceHelper;

    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;
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
            var stringWithDelimiters = createStringWithTechnicalDelimiters();

            return replaceTechnicalDelimiters(stringWithDelimiters);
        }

        private String replaceTechnicalDelimiters(String stringWithDelimiters) {
            var listOfDelimiters = Arrays.stream(stringWithDelimiters.split(" "))
                    .filter(word -> word.equals(Templating.TECHNICAL_DELIMITER))
                    .collect(Collectors.toList());
            var functionalDelimiters = decideFunctionalDelimiters(listOfDelimiters);

            return SentenceHelper.fillTemplateWithDataFromList(stringWithDelimiters, Templating.TECHNICAL_DELIMITER_WITH_LEADING_AND_TRAILING_SPACES, functionalDelimiters);
        }

        private List<String> decideFunctionalDelimiters(List<String> listOfTechnicalDelimiters) {
            var listOfFunctionalDelimiters = new ArrayList<String>();
            var iterator = listOfTechnicalDelimiters.listIterator();

            while (iterator.hasNext()) {
                iterator.next();

                // if the iterator has next, this means we are still in the middle of an enumeration,
                // so we use a comma.
                // if the iterator has no next, we are at the last enumeration,
                // so we use 'and'
                if (iterator.hasNext()) {
                    listOfFunctionalDelimiters.add(Templating.COMMA_DELIMITER);
                } else {
                    listOfFunctionalDelimiters.add(Templating.AND_DELIMITER);
                }
            }

            return listOfFunctionalDelimiters;
        }

        private String createStringWithTechnicalDelimiters() {
            return getData()
                    .stream()
                    .map(tuple -> SentenceHelper.fillTemplateWithDataFromTuple(template, tuple))
                    .collect(Collectors.joining(Templating.TECHNICAL_DELIMITER_WITH_LEADING_AND_TRAILING_SPACES))
                    .strip();
        }
    }
