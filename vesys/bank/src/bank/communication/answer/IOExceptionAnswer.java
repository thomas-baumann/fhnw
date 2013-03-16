package bank.communication.answer;

import java.io.IOException;

/**
 * This class provides a IOException answer. It includes an exception of the type IO. When
 * the getData method will be called the exception will be thrown.
 * 
 * @see IAnswer
 * @author Thomas Baumann
 * @version 1.0
 */
public class IOExceptionAnswer implements IAnswer<Object> {

    private IOException e;

    public IOExceptionAnswer(IOException e) {
        this.e = e;
    }

    @Override
    public Object getData() throws IOException {
        throw this.e;
    }

}
