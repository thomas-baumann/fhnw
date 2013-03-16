package bank.communication.request;

import java.io.IOException;

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
 * This class provides a withdraw request for an account.
 * 
 * @see IRequest
 * @author Thomas Baumann
 * @version 1.1
 */
public class WithdrawRequest implements IRequest {

    private String number;
    private Double amount;

    public WithdrawRequest(String number, Double amount) {
        this.number = number;
        this.amount = amount;
    }

    @Override
    public IAnswer<?> handleRequest(IBank b) {
        try {
            b.getAccount(this.number).withdraw(this.amount);
            return new Answer<Object>(null);
        } catch (IOException e) {
            return new IOExceptionAnswer(e);
        } catch (IllegalArgumentException e) {
            return new IllegalArgumentExceptionAnswer(e);
        } catch (OverdrawException e) {
            return new OverdrawExceptionAnswer(e);
        } catch (InactiveException e) {
            return new InactiveExceptionAnswer(e);
        }
    }

}
