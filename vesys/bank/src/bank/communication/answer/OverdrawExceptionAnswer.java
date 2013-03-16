package bank.communication.answer;

import bank.OverdrawException;

/**
 * This class provides a OverdrawException answer. It includes an exception of the type
 * Overdraw. When the getData method will be called the exception will be thrown.
 * 
 * @see IAnswer
 * @author Thomas Baumann
 * @version 1.0
 */
public class OverdrawExceptionAnswer implements IAnswer<Object> {

    private OverdrawException e;

    public OverdrawExceptionAnswer(OverdrawException e) {
        this.e = e;
    }

    @Override
    public Object getData() throws OverdrawException {
        throw this.e;
    }

}
