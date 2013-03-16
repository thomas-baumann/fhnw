package bank.communication.request;

import java.io.IOException;

import bank.IBank;
import bank.communication.answer.Answer;
import bank.communication.answer.IAnswer;
import bank.communication.answer.IOExceptionAnswer;

/**
 * This class provides a get account request.
 * 
 * @see IRequest
 * @author Thomas Baumann
 * @version 1.1
 */
public class GetAccountRequest implements IRequest {

    private String number;

    public GetAccountRequest(String number) {
        this.number = number;
    }

    @Override
    public IAnswer<?> handleRequest(IBank b) {
        try {
            return new Answer<Boolean>(b.getAccount(this.number) != null);
        } catch (IOException e) {
            return new IOExceptionAnswer(e);
        }
    }

}
