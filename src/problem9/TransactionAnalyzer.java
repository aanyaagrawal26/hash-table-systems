package problem9;

import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    long time;


    public Transaction(int id, int amount, String merchant, String account, long time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }


}

public class TransactionAnalyzer {


    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // ---------- Classic Two-Sum ----------
    public void findTwoSum(int target) {

        Map<Integer, Transaction> map = new HashMap<>();

        System.out.println("\nTwo-Sum Results:");

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                System.out.println(
                        "(" + other.id + ", " + t.id + ") → "
                                + other.amount + " + " + t.amount
                                + " = " + target
                );
            }

            map.put(t.amount, t);
        }
    }

    // ---------- Two-Sum within time window ----------
    public void findTwoSumWithTime(int target, long windowMillis) {

        Map<Integer, List<Transaction>> map = new HashMap<>();

        System.out.println("\nTwo-Sum With Time Window:");

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                for (Transaction other : map.get(complement)) {

                    if (Math.abs(t.time - other.time) <= windowMillis) {

                        System.out.println(
                                "(" + other.id + ", " + t.id + ") → within time window"
                        );
                    }
                }
            }

            map.putIfAbsent(t.amount, new ArrayList<>());
            map.get(t.amount).add(t);
        }
    }

    // ---------- Duplicate detection ----------
    public void detectDuplicates() {

        Map<String, List<Transaction>> map = new HashMap<>();

        System.out.println("\nDuplicate Transactions:");

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.print("Duplicate: amount=" + list.get(0).amount
                        + " merchant=" + list.get(0).merchant + " accounts=[ ");

                for (Transaction t : list) {
                    System.out.print(t.account + " ");
                }

                System.out.println("]");
            }
        }
    }

    // ---------- K-Sum ----------
    public void findKSum(int k, int target) {

        System.out.println("\nK-Sum Results:");

        backtrack(0, k, target, new ArrayList<>());
    }

    private void backtrack(int index, int k, int target, List<Transaction> current) {

        if (k == 0 && target == 0) {

            System.out.print("Match: ");

            for (Transaction t : current) {
                System.out.print(t.id + " ");
            }

            System.out.println();
            return;
        }

        if (k == 0 || target < 0) return;

        for (int i = index; i < transactions.size(); i++) {

            Transaction t = transactions.get(i);

            current.add(t);

            backtrack(i + 1, k - 1, target - t.amount, current);

            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        long now = System.currentTimeMillis();

        analyzer.addTransaction(new Transaction(1, 500, "StoreA", "acc1", now));
        analyzer.addTransaction(new Transaction(2, 300, "StoreB", "acc2", now + 1000));
        analyzer.addTransaction(new Transaction(3, 200, "StoreC", "acc3", now + 2000));
        analyzer.addTransaction(new Transaction(4, 500, "StoreA", "acc4", now + 3000));

        analyzer.findTwoSum(500);

        analyzer.findTwoSumWithTime(500, 3600000);

        analyzer.detectDuplicates();

        analyzer.findKSum(3, 1000);
    }


}
