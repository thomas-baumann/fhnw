package bank.communication.request;

import java.io.IOException;

import bank.IBank;
import bank.communication.answer.Answer;
import bank.communication.answer.IAnswer;
import bank.communication.answer.IOExceptionAnswer;

/**
 * This class provides a create account request.
 * 
 * @see IRequest
 * @author Thomas Baumann
 * @version 1.1
 */
public class CreateAccountRequest implements IRequest {

    private String owner;

    public CreateAccountRequest(String owner) {
        this.owner = owner;
    }

    @Override
    public IAnswer<?> handleRequest(IBank b) {
        try {
            String s = b.createAccount(this.owner);
            return new Answer<String>(s);
        } catch (IOException e) {
            return new IOExceptionAnswer(e);
        }
    }

}
