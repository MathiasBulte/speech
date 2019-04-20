package xyz.bulte.speech.sentences;


import lombok.AllArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.tuple.Tuple;
import xyz.bulte.speech.Sentence;
import xyz.bulte.speech.sentences.helper.SentenceHelper;

@ToString
@AllArgsConstructor
public class TransformerTemplateSentence<T extends Tuple> implements Sentence<T> {

    private String template;
    private T data;
    private Transform<T>[] transformers;

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public T getData() {
        return data;
    }

    public boolean isValid() {
        var data = getData();

        int amountOfParametersForTemplatedValues = StringUtils.countMatches(getTemplate(), "{}");
        int amountOfTransformerFunctions = transformers.length;

        return amountOfParametersForTemplatedValues <= data.degree() + amountOfTransformerFunctions;
    }

    @Override
    public String asString() {
        if (!isValid()) {
            throw new RuntimeException("Couldn't generate string because it was invalid. Template: " + getTemplate() + ", data: " + getData());
        }

        T tuple = getData();
        String filledTemplateWithDataFromTuples = SentenceHelper.fillTemplateWithDataFromTuple(template, tuple);
        String filledTemplateWithDataFromTransformer = filledTemplateWithDataFromTuples;

        for (Transform<T> transformer : transformers) {
            String templateIdentifier = extractTemplateIdentifier(transformer);
            filledTemplateWithDataFromTransformer = StringUtils.replaceOnce(filledTemplateWithDataFromTransformer, templateIdentifier, transformer.apply(tuple));
        }

        return filledTemplateWithDataFromTransformer;
    }

    private String extractTemplateIdentifier(Transform<T> transformer) {
        return transformer.getIdentifier()
                .map(id -> "{" + id + "}")
                .orElse("{}");
    }
}
