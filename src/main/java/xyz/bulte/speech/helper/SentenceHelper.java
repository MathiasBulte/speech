package xyz.bulte.speech.helper;

import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.tuple.Tuple;
import xyz.bulte.speech.constant.Templating;

import java.util.List;

public class SentenceHelper {

    public static <T> String fillTemplateWithData(String template, String templateIdentifier, List<T> data) {
        var outputString = template;

        for (T value : data) {
            outputString = StringUtils.replaceOnce(outputString, templateIdentifier, value.toString());
        }

        return outputString;
    }

    public static String fillTemplateWithData(String template, Tuple tuple) {
        return fillTemplateWithData(template, tuple.toList());
    }

    public static <T> String fillTemplateWithData(String template, List<T> data) {
        return fillTemplateWithData(template, Templating.TEMPLATE, data);
    }

    public static <T extends Tuple> boolean doesTupleMatchTemplate(String template, T inputTuple) {
        int amountOfParametersFromTemplate = StringUtils.countMatches(template, Templating.TEMPLATE);

        return amountOfParametersFromTemplate == inputTuple.degree();
    }
}
