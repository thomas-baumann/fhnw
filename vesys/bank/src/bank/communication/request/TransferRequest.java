package bank.communication.request;

import java.io.IOException;

import bank.IAccount;
import bank.IBank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.communication.answer.Answer;
import bank.communication.answer.IAnswer;
import bank.communication.answer.IOExceptionAnswer;
import bank.communication.answer.IllegalArgumentExceptionAnswer;
import bank.communication.answer.InactiveExceptionAnswer;
import bank.communication.answer.OverdrawExceptionAnswer;

/**
 * This class provides a transfer request.
 * 
 * @see IRequest
 * @author Thomas Baumann
 * @version 1.1
 */
public class TransferRequest implements IRequest {

    private String numberFrom;
    private String numberTo;
    private Double amount;

    public TransferRequest(String numberFrom, String numberTo, Double amount) {
        this.numberFrom = numberFrom;
        this.numberTo = numberTo;
        this.amount = amount;
    }

    @Override
    public IAnswer<?> handleRequest(IBank b) {
        try {
            IAccount f = b.getAccount(this.numberFrom);
            IAccount t = b.getAccount(this.numberTo);
            b.transfer(f, t, this.amount);
            return new Answer<Object>(null);
        } catch (IllegalArgumentException e) {
            return new IllegalArgumentExceptionAnswer(e);
        } catch (IOException e) {
            return new IOExceptionAnswer(e);
        } catch (OverdrawException e) {
            return new OverdrawExceptionAnswer(e);
        } catch (InactiveException e) {
            return new InactiveExceptionAnswer(e);
        }
    }

}
