import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class Tray {
    private int capacity;
    private int quantity;

    public Tray(int capacity) {
        this.capacity = capacity;
        this.quantity = capacity;
    }

    public synchronized boolean isEmpty() {
        return quantity <= 0;
    }

    public synchronized void refill() {
        System.out.println(Thread.currentThread().getName() + " is refilling the tray.");
        quantity = capacity;
    }

    public synchronized void takeItem() {
        quantity--;
    }

    public synchronized int getQuantity() {
        return quantity;
    }
}

class Guest extends Thread {
    private static final int MAX_BOREKS = 4;
    private static final int MAX_CAKE = 2;
    private static final int MAX_DRINK = 4;

    private String name;
    private Tray borekTray, cakeTray, drinkTray;
    private Map<String, Integer> consumption;

    public Guest(String name, Tray borekTray, Tray cakeTray, Tray drinkTray) {
        this.name = name;
        this.borekTray = borekTray;
        this.cakeTray = cakeTray;
        this.drinkTray = drinkTray;
        this.consumption = new HashMap<>();
        this.consumption.put("borek", 0);
        this.consumption.put("cake", 0);
        this.consumption.put("drink", 0);
    }

    @Override
    public void run() {
        Random random = new Random();
        int boreksConsumed = 0, cakeConsumed = 0, drinkConsumed = 0;

        while (boreksConsumed < MAX_BOREKS || cakeConsumed < MAX_CAKE || drinkConsumed < MAX_DRINK) {
            int choice = random.nextInt(3);

            switch (choice) {
                case 0:
                    if (!borekTray.isEmpty()) {
                        borekTray.takeItem();
                        boreksConsumed++;
                        consumption.put("borek", consumption.get("borek") + 1);
                        System.out.println(name + " ate a borek.");
                    } else {
                        System.out.println(name + " is waiting for borek refill.");
                    }
                    break;
                case 1:
                    if (!cakeTray.isEmpty()) {
                        cakeTray.takeItem();
                        cakeConsumed++;
                        consumption.put("cake", consumption.get("cake") + 1);
                        System.out.println(name + " ate a slice of cake.");
                    } else {
                        System.out.println(name + " is waiting for cake refill.");
                    }
                    break;
                case 2:
                    if (!drinkTray.isEmpty()) {
                        drinkTray.takeItem();
                        drinkConsumed++;
                        consumption.put("drink", consumption.get("drink") + 1);
                        System.out.println(name + " drank a glass of drink.");
                    } else {
                        System.out.println(name + " is waiting for drink refill.");
                    }
                    break;
            }
        }

        // Print guest's consumption
        System.out.println(name + "'s consumption:");
        System.out.println("Boreks: " + consumption.get("borek"));
        System.out.println("Cakes: " + consumption.get("cake"));
        System.out.println("Drinks: " + consumption.get("drink"));
    }
}

class Waiter extends Thread {
    private Tray borekTray, cakeTray, drinkTray;

    public Waiter(Tray borekTray, Tray cakeTray, Tray drinkTray) {
        this.borekTray = borekTray;
        this.cakeTray = cakeTray;
        this.drinkTray = drinkTray;
    }

    @Override
    public void run() {
        while (true) {
            if (borekTray.isEmpty()) {
                borekTray.refill();
            }
            if (cakeTray.isEmpty()) {
                cakeTray.refill();
            }
            if (drinkTray.isEmpty()) {
                drinkTray.refill();
            }

            // Eğer tüm yiyecek ve içecekler tükendi ise dur
            if (borekTray.isEmpty() && cakeTray.isEmpty() && drinkTray.isEmpty()) {
                break;
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Tray borekTray = new Tray(5); // Borek tray capacity: 5
        Tray cakeTray = new Tray(5);  // Cake tray capacity: 5
        Tray drinkTray = new Tray(5); // Drink tray capacity: 5

        // Starting
    }
}