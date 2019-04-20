package xyz.bulte.speech.sentences.helper;

import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.tuple.Tuple;

public class SentenceHelper {

    public static String fillTemplateWithDataFromTuple(String template, Tuple tuple) {
        var outputString = template;
        for (Object value : tuple.toSeq()) {
            outputString = StringUtils.replaceOnce(outputString, "{}", value.toString());
        }

        return outputString;
    }

    public static <T extends Tuple> boolean doesTupleMatchTemplate(String template, T inputTuple) {
        int amountOfParametersFromTemplate = StringUtils.countMatches(template, "{}");

        return amountOfParametersFromTemplate == inputTuple.degree();
    }
}
