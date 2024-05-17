import java.util.Random;

// Tepsi sınıfı, bir tepsinin kapasitesini, miktarını ve adını tutar
class Tray {
    private int capacity;
    private int quantity;
    private String name;
    private static int remainingTray = 3;  // Kalan tepsi sayısı, statik olarak tutulur
    private boolean flag = true;  // Tepsinin dolup dolmadığını belirten bayrak

    // Tepsi yapıcı metodu, kapasite, miktar ve adı alır
    public Tray(int capacity, int quantity, String name) {
        this.capacity = capacity;
        this.quantity = quantity;
        this.name = name;
    }

    // Kalan tepsi sayısını döner
    public static int getRemainingTray() {
        return remainingTray;
    }

    // Tepsideki miktarı döner
    public int getQuantity() {
        return quantity;
    }

    // Tepsinin boş olup olmadığını senkronize olarak kontrol eder
    public synchronized boolean isEmpty() {
        return capacity <= 0;
    }

    // Tepsinin dolup dolmadığını döner
    public boolean isFlag() {
        return flag;
    }

    // Tepsiyi senkronize olarak doldurur
    public synchronized void refill() {
        if (quantity != 0){
            System.out.println(name + " tepsi dolduruldu. Kalan Miktar: " + quantity);
            capacity += 5;
            quantity = quantity - 5;
        } else {
            if (flag){
                System.out.println("------" +name + " bitti");
                remainingTray--;
                flag = false;
            }
        }
    }

    // Tepsiden bir öğe alır
    public synchronized void takeItem() {
        capacity--;
    }
}

// Misafir sınıfı, bir misafirin özelliklerini ve davranışlarını tanımlar
class Guest extends Thread {
    private static final int MAX_BOREKS = 4;
    private static final int MAX_CAKE = 2;
    private static final int MAX_DRINK = 4;

    private String name;
    private Tray borekTray, cakeTray, drinkTray;

    // Misafir yapıcı metodu, adını ve tepsileri alır
    public Guest(String name, Tray borekTray, Tray cakeTray, Tray drinkTray) {
        this.name = name;
        this.borekTray = borekTray;
        this.cakeTray = cakeTray;
        this.drinkTray = drinkTray;
    }

    // Misafirin davranışlarını tanımlar
    @Override
    public void run() {
        Random random = new Random();
        int boreksConsumed = 0, cakeConsumed = 0, drinkConsumed = 0;

        while (boreksConsumed < MAX_BOREKS || cakeConsumed < MAX_CAKE || drinkConsumed < MAX_DRINK) {
            int choice = random.nextInt(3);  // 0, 1 veya 2 arasında rastgele bir seçim yapar

            switch (choice) {
                case 0:
                    if (!borekTray.isEmpty()) {
                        borekTray.takeItem();
                        boreksConsumed++;
                        System.out.println(name + " 1 börek yedi.");
                        try {
                            Thread.sleep(1000);  // 1 saniye bekler
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (borekTray.getQuantity() != 0){
                        System.out.println(name + " börek tepsisinin dolması bekleniyor.");
                    }
                    break;
                case 1:
                    if (!cakeTray.isEmpty()) {
                        cakeTray.takeItem();
                        cakeConsumed++;
                        System.out.println(name + " 1 dilim kek yedi.");
                        try {
                            Thread.sleep(1000);  // 1 saniye bekler
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (cakeTray.getQuantity() != 0) {
                        System.out.println(name + " kek tepsisinin dolması bekleniyor.");
                    }
                    break;
                case 2:
                    if (!drinkTray.isEmpty()) {
                        drinkTray.takeItem();
                        drinkConsumed++;
                        System.out.println(name + " 1 bardak içecek içti.");
                        try {
                            Thread.sleep(1000);  // 1 saniye bekler
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (drinkTray.getQuantity() != 0) {
                        System.out.println(name + " içecek tepsisinin dolması bekleniyor.");
                    }
                    break;
            }
        }
    }
}

// Garson sınıfı, tepsileri doldurmakla sorumlu
class Waiter extends Thread {
    private Tray borekTray, cakeTray, drinkTray;

    // Garson yapıcı metodu, tepsileri alır
    public Waiter(Tray borekTray, Tray cakeTray, Tray drinkTray) {
        this.borekTray = borekTray;
        this.cakeTray = cakeTray;
        this.drinkTray = drinkTray;
    }

    // Garsonun davranışlarını tanımlar
    @Override
    public void run() {
        while (true) {
            if (borekTray.isEmpty() && borekTray.isFlag() ) {
                borekTray.refill();
            }
            if (cakeTray.isEmpty() && cakeTray.isFlag() ) {
                cakeTray.refill();
            }
            if (drinkTray.isEmpty() && drinkTray.isFlag() ) {
                drinkTray.refill();
            }
            if (Tray.getRemainingTray() == 0){
                System.out.println("Parti is over!!! 🥳🥳");
                System.exit(0);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Tepsiler oluşturuluyor
        Tray borekTray = new Tray(5, 30, "börek");
        Tray cakeTray = new Tray(5, 15, "cake");
        Tray drinkTray = new Tray(5, 30, "drink");

        // Misafirler oluşturuluyor ve başlatılıyor
        Guest[] guests = new Guest[8];
        for (int i = 0; i < 8; i++) {
            guests[i] = new Guest("Guest " + (i + 1), borekTray, cakeTray, drinkTray);
            guests[i].start();
        }

        // Garson oluşturuluyor ve başlatılıyor
        Waiter waiter = new Waiter(borekTray, cakeTray, drinkTray);
        waiter.start();
    }
}
