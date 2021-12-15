import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();

        List<Thread> list = new ArrayList<>();

        //создание пользователей
        for (int i = 1; i < 9; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                try {
                    var acc = bank.createAccount(finalI);
                    acc.setAmount(new Random().nextDouble(3000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            list.add(thread);
            thread.start();
        }

        //проверка блокировки
        Thread thread1 = new Thread(() -> {
            try {
                bank.blockAccount(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        list.add(thread1);
        thread1.start();
        Thread thread2 = new Thread(() -> {
            try {
                var res1 = bank.isAccountBlocked(1);
                var res2 = bank.isAccountBlocked(2);
                System.out.println("1: " + res1);
                System.out.println("2: " + res2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        list.add(thread2);
        thread2.start();

        //проверка снятие больше чем есть на счету
        try{
            bank.changeAmount(1, -4000);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //проверка transfer
        Thread thread3 = new Thread(() -> {
            try {
                bank.transferMoney(4, 5, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        list.add(thread3);
        thread3.start();
        Thread thread4 = new Thread(() -> {
            try {
                bank.transferMoney(4, 5, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        list.add(thread4);
        thread4.start();



        for (Thread thread: list){
            thread.join();
        }
    }
}
