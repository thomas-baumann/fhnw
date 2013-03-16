package bank.communication.request;

import java.io.IOException;

import bank.IBank;
import bank.communication.answer.Answer;
import bank.communication.answer.IAnswer;
import bank.communication.answer.IOExceptionAnswer;

/**
 * This class provides a close account request.
 * 
 * @see IRequest
 * @author Thomas Baumann
 * @version 1.1
 */
public class CloseAccountRequest implements IRequest {

    private String number;

    public CloseAccountRequest(String number) {
        this.number = number;
    }

    @Override
    public IAnswer<?> handleRequest(IBank b) {
        try {
            boolean ans = b.closeAccount(this.number);
            return new Answer<Boolean>(ans);
        } catch (IOException e) {
            return new IOExceptionAnswer(e);
        }
    }

}
