public class Main {
    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();

        var acc1 = bank.createAccount(1);
        acc1.setAmount(3000);
        var acc2 = bank.createAccount(2);
        acc2.setAmount(1000);
        var acc3 = bank.createAccount(3);
        acc3.setAmount(0);
        var acc4 = bank.createAccount(4);
        acc4.setAmount(5000);

        try {
            bank.transferMoney(acc1.getId(), acc3.getId(), 1500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Thread thread : bank.getTransferTask()) {
            thread.join();
        }


    }
}
