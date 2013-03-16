package bank.communication.request;

import java.io.IOException;

import bank.IBank;
import bank.communication.answer.Answer;
import bank.communication.answer.IAnswer;
import bank.communication.answer.IOExceptionAnswer;

/**
 * This class provides a get balance request for an account.
 * 
 * @see IRequest
 * @author Thomas Baumann
 * @version 1.1
 */
public class GetBalanceRequest implements IRequest {

    private String number;

    public GetBalanceRequest(String number) {
        this.number = number;
    }

    @Override
    public IAnswer<?> handleRequest(IBank b) {
        try {
            Double balance = b.getAccount(this.number).getBalance();
            return new Answer<Double>(balance);
        } catch (IOException e) {
            return new IOExceptionAnswer(e);
        }
    }

}
