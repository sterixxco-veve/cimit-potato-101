package cimitpotato101;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Level { // Mewakili satu level permainan, berisi seluruh data dan aturan pada level tersebut (durasi, pelanggan, menu yang terbuka, oven, dan kriteria bintang).
    private int levelNumber; // nomor urut level
    private int duration; // lama permainan dalam detik
    private List<MenuItem> unlockedItems; // menu yang ke unlocked di level tersebut
    private List<Customer> customers; // jumlah customer yang bakal muncul
    private Oven[] ovens; // untuk membuat oven 
    private int[] starGoldThresholds; // batas gold untuk mendapat kan bintang 1, 2, atau 3

    public Level(int levelNumber, int duration, List<MenuItem> unlockedItems, List<Customer> customer) {
        this.levelNumber = levelNumber;
        this.duration = duration;
        this.unlockedItems = unlockedItems;
        this.customers = customers;
        this.ovens = createOvens();
        this.starGoldThresholds = starGoldThresholds;
    }
    
    private Oven[] createOvens() {
        Oven[] oven = {new Oven("Regular", 3000),
            new Oven("Curly", 3500),
            new Oven("Chips", 5000),
            new Oven("Wedges", 5500),
            new Oven("Tornado", 4000),
            new Oven("Mashed", 7000)};
        return oven;
    }


    public Oven[] getOvens() {
        return ovens;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getDuration() {
        return duration;
    }

    public List<Potato> getAvailablePotatoes() {
        return MenuManager.filterPotatoes(unlockedItems);
    }

    public List<Topping> getAvailableSauces() {
        return MenuManager.filterAddonsByKategori(unlockedItems, "sauce");
    }

    public List<Topping> getAvailableToppings() {
        return MenuManager.filterAddonsByKategori(unlockedItems, "topping");
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public int[] getStarGoldThresholds() {
        return starGoldThresholds;
    }

    public void setStarGoldThresholds(int[] starGoldThresholds) {
        this.starGoldThresholds = starGoldThresholds;
    }
    
    
    
    
}
