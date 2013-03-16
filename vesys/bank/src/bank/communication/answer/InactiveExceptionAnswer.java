package bank.communication.answer;

import bank.InactiveException;

/**
 * This class provides a InactiveException answer. It includes an exception of the type
 * inactive. When the getData method will be called the exception will be thrown.
 * 
 * @see IAnswer
 * @author Thomas Baumann
 * @version 1.0
 */
public class InactiveExceptionAnswer implements IAnswer<Object> {

    private InactiveException e;

    public InactiveExceptionAnswer(InactiveException e) {
        this.e = e;
    }

    @Override
    public Object getData() throws InactiveException {
        throw this.e;
    }

}
