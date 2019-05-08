package xyz.bulte.speech.sentences;


import lombok.AllArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.tuple.Tuple;
import xyz.bulte.speech.api.Sentence;
import xyz.bulte.speech.constant.Templating;
import xyz.bulte.speech.helper.SentenceHelper;

import java.util.function.Function;

@ToString
@AllArgsConstructor
public class FunctionTemplateSentence<T extends Tuple> implements Sentence<T> {

    private String template;
    private T data;
    private Function<T, String> function;

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

        int amountOfParametersForTemplatedValues = StringUtils.countMatches(getTemplate(), Templating.TEMPLATE);
        int amountOfTransformerFunctions = 1;

        return amountOfParametersForTemplatedValues <= data.degree() + amountOfTransformerFunctions;
    }

    @Override
    public String asString() {
        if (!isValid()) {
            throw new RuntimeException("Couldn't generate string because it was invalid. Template: " + getTemplate() + ", data: " + getData());
        }

        T tuple = getData();

        String template = getTemplate();

        String filledTemplateWithDataFromTuples = SentenceHelper.fillTemplateWithDataFromTuple(template, tuple);
        String filledTemplateWithDataFromTransformer = StringUtils.replaceOnce(filledTemplateWithDataFromTuples, "{}", function.apply(tuple));

        return filledTemplateWithDataFromTransformer;
    }
}
