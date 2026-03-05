package problem2;

import java.util.*;

public class FlashSaleInventoryManager {

    // productId -> stock count
    private HashMap<String, Integer> inventory = new HashMap<>();

    // productId -> waiting list
    private HashMap<String, Queue<Integer>> waitingList = new HashMap<>();


    // Add product with stock
    public void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }


    // Check stock
    public int checkStock(String productId) {
        return inventory.getOrDefault(productId, 0);
    }


    // Purchase item
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = inventory.getOrDefault(productId, 0);

        if (stock > 0) {
            inventory.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        }

        // add to waiting list
        Queue<Integer> queue = waitingList.get(productId);
        queue.add(userId);

        return "Out of stock, added to waiting list. Position #" + queue.size();
    }


    // Show waiting list
    public Queue<Integer> getWaitingList(String productId) {
        return waitingList.get(productId);
    }


    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        manager.addProduct("IPHONE15_256GB", 3);

        System.out.println("Stock: " + manager.checkStock("IPHONE15_256GB"));

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 11111));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 22222));
    }
}