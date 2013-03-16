package bank.communication.request;

import java.io.IOException;

import bank.IBank;
import bank.communication.answer.Answer;
import bank.communication.answer.IAnswer;
import bank.communication.answer.IOExceptionAnswer;

/**
 * This class provides a get owner request for an account.
 * 
 * @see IRequest
 * @author Thomas Baumann
 * @version 1.1
 */
public class GetOwnerRequest implements IRequest {

    private String number;

    public GetOwnerRequest(String number) {
        this.number = number;
    }

    @Override
    public IAnswer<?> handleRequest(IBank b) {
        try {
            String owner = b.getAccount(this.number).getOwner();
            return new Answer<String>(owner);
        } catch (IOException e) {
            return new IOExceptionAnswer(e);
        }
    }

}
