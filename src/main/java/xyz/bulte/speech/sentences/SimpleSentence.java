package xyz.bulte.speech.sentences;


import lombok.AllArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.NotImplementedException;
import xyz.bulte.speech.Sentence;

/**
 * This is a simple sentence; there is no templating involved.
 */
@ToString
@AllArgsConstructor
public class SimpleSentence implements Sentence {

    private String sentence;

    @Override
    public String getTemplate() {
        return sentence;
    }

    @Override
    public Object getData() {
        throw new NotImplementedException("SimpleSentence does not have any data");
    }

    @Override
    public String asString() {
        return sentence;
    }
}
