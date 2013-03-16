package bank.communication.answer;

/**
 * This class provides a close account answer. It includes an boolean which specifies, if
 * the close account request was successful or not.
 * 
 * @see IAnswer
 * @author Thomas Baumann
 * @version 1.0
 */
public class Answer<T> implements IAnswer<T> {

    private T value;

    public Answer(T value) {
        this.value = value;
    }

    @Override
    public T getData() {
        return this.value;
    }

}
