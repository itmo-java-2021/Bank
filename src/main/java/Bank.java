import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {
    private long count;
    private ConcurrentHashMap<Long, Account> accMap;
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

    public double getAmount(long accId) throws Exception {
        if (accMap.containsKey(accId)){
            if (isAccountBlocked(accId)){
                throw new Exception("");
            }
            return accMap.get(accId).getAmount();
        }
        return 0;
    }

    public void blockAccount(long accId){
        if (accMap.containsKey(accId)){
            accMap.get(accId).setBlocked(true);
        }
    }

    public void unblockAccount(long accId){
        if (accMap.containsKey(accId)){
            accMap.get(accId).setBlocked(false);
        }
    }

    public boolean isAccountBlocked(long accId){
        if (accMap.containsKey(accId)){
            return accMap.get(accId).isBlocked();
        }
        return false;
    }

    public void changeAmount(long accId, double amount) throws Exception {
        if (accMap.containsKey(accId)){
            if (isAccountBlocked(accId)){
                throw new Exception("");
            }
            Account account = accMap.get(accId);
            account.setAmount(account.getAmount() + amount);
        }
    }

    public void transferMoney(long srcAccId, long dstAccId, double amount) throws Exception {
        if (isAccountBlocked(srcAccId) || isAccountBlocked(dstAccId)){
            throw new Exception("");
        }
        Transfer transfer = new Transfer(srcAccId, dstAccId, amount);
        transferTask.add(transfer);
        transfer.start();
    }

    private long nextId(){
        return ++count;
    }

    public List<Thread> getTransferTask() {
        return new ArrayList<>(transferTask);
    }

    class Transfer extends Thread implements Closeable{
        private long srcAccId;
        private long dstAccId;
        private double amount;

        public Transfer(long srcAccId, long dstAccId, double amount) {
            this.srcAccId = srcAccId;
            this.dstAccId = dstAccId;
            this.amount = amount;
        }

        @Override
        public void run() {
            Account acc1 = accMap.get(srcAccId);
            Account acc2 = accMap.get(dstAccId);
            acc1.setBlocked(true);
            acc2.setBlocked(true);
            if (acc1.getAmount() >= amount){
                acc1.setAmount(acc1.getAmount() - amount);
                acc2.setAmount(acc2.getAmount() + amount);
            }acc1.setBlocked(false);
            acc2.setBlocked(false);
        }

        public void close() {
            interrupt();
            transferTask.remove(this);
        }
    }
}
