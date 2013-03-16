package bank.communication.answer;

/**
 * This class provides a IllegalArgumentException answer. It includes an exception of the
 * type illegal argument. When the getData method will be called the exception will be
 * thrown.
 * 
 * @see IAnswer
 * @author Thomas Baumann
 * @version 1.0
 */
public class IllegalArgumentExceptionAnswer implements IAnswer<Object> {

    private IllegalArgumentException e;

    public IllegalArgumentExceptionAnswer(IllegalArgumentException e) {
        this.e = e;
    }

    @Override
    public Object getData() throws IllegalArgumentException {
        throw this.e;
    }

}
