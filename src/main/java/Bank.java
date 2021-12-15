import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.sleep;

public class Bank {
    private volatile long count;
    private final ConcurrentHashMap<Long, Account> accMap;
    private final List<Thread> transferTask;

    public Bank() {
        this.accMap = new ConcurrentHashMap<Long, Account>();
        count = 0;
        transferTask = new ArrayList<>();
    }

    public Account createAccount(long userId){
        Account account = new Account(nextId(), userId);
        accMap.put(account.getId(), account);
        return account;
    }

    public double getAmount(long accId) throws TransactionsException {
        var acc = getAccount(accId);
        synchronized (acc){
            if (isAccountBlocked(accId)){
                throw new TransactionsException("account blocked");
            }
            return accMap.get(accId).getAmount();
        }
    }

    public void blockAccount(long accId) throws TransactionsException {
        var acc = getAccount(accId);
        synchronized (acc){
            acc.setBlocked(true);
        }
    }

    public void unblockAccount(long accId) throws TransactionsException {
        var acc = getAccount(accId);
        synchronized (acc){
            acc.setBlocked(false);
        }
    }

    public boolean isAccountBlocked(long accId) throws TransactionsException {
        var acc = getAccount(accId);
        synchronized (acc){
            return accMap.get(accId).isBlocked();
        }
    }

    public void changeAmount(long accId, double amount) throws TransactionsException {
        var acc = getAccount(accId);
        synchronized (acc){
            if (isAccountBlocked(accId)){
                throw new TransactionsException("account blocked");
            }
            double amountAcc = acc.getAmount();
            if (amount < 0 && amountAcc < Math.abs(amount)){
                throw new TransactionsException("insufficient funds");
            }
            acc.setAmount(acc.getAmount() + amount);
        }
    }

    public void transferMoney(long srcAccId, long dstAccId, double amount) throws TransactionsException {
        var acc1 = getAccount(srcAccId);
        var acc2 = getAccount(dstAccId);
        synchronized (acc1){
            synchronized (acc2){
                if (isAccountBlocked(srcAccId) || isAccountBlocked(dstAccId)){
                    throw new TransactionsException("account blocked");
                }
                acc1.setBlocked(true);
                acc2.setBlocked(true);
                if (acc1.getAmount() < amount){
                    throw new TransactionsException("insufficient funds");
                }
                acc1.setAmount(acc1.getAmount() - amount);
                acc2.setAmount(acc2.getAmount() + amount);
                acc1.setBlocked(false);
                acc2.setBlocked(false);
            }
        }
    }

    private synchronized long nextId(){
        return ++count;
    }

    private synchronized Account getAccount(long id) throws TransactionsException {
        if (accMap.containsKey(id)){
            return accMap.get(id);
        }
        throw new TransactionsException("no id found");
    }
}
