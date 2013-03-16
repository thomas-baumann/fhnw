package bank.communication.request;

import java.io.IOException;
import java.util.Set;

import bank.IBank;
import bank.communication.answer.Answer;
import bank.communication.answer.IAnswer;
import bank.communication.answer.IOExceptionAnswer;

/**
 * This class provides a get account numbers request.
 * 
 * @see IRequest
 * @author Thomas Baumann
 * @version 1.1
 */
public class GetAccountNumbersRequest implements IRequest {

    @Override
    public IAnswer<?> handleRequest(IBank b) {
        try {
            Set<String> s = b.getAccountNumbers();
            return new Answer<Set<String>>(s);
        } catch (IOException e) {
            return new IOExceptionAnswer(e);
        }

    }

}
