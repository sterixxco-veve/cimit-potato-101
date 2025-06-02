package cimitpotato101;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Gracia Krisnanda
 */
public class Level {
    private int levelNumber;
    private int duration; // in seconds
    private List<MenuItem> unlockedItems;
    private List<Customer> customers;
    private Oven[] ovens;
    private int[] starGoldThresholds;

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
