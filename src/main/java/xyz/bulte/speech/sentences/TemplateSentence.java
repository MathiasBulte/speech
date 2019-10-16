package xyz.bulte.speech.sentences;


import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.tuple.Tuple;
import xyz.bulte.speech.api.Sentence;
import xyz.bulte.speech.helper.SentenceHelper;

@ToString
public class TemplateSentence<T extends Tuple> implements Sentence<Tuple> {

    private String template;
    private T data;

    public TemplateSentence(String template, T data) {
        this.template = template;
        this.data = data;
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public boolean isValid() {
        var data = getData();
        return StringUtils.countMatches(getTemplate(), "{}") == data.degree();
    }

    @Override
    public String asString() {
        if (!isValid()) {
            throw new RuntimeException("Couldn't generate string because it was invalid. Template: " + getTemplate() + ", data: " + getData());
        }

        var tuple = getData();
        return SentenceHelper.fillTemplateWithData(getTemplate(), tuple);
    }
}
