package cimitpotato101;


import java.util.ArrayList;
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
    private int goal;
    private int duration; // in seconds
    private BoosterTier booster;
    private List<MenuItem> unlockedItems;
    private List<Customer> customers;
    private List<Oven> ovens;

    public Level(int levelNumber, int goal, int duration, List<MenuItem> unlockedItems, List<Customer> customer, BoosterTier booster) {
        this.levelNumber = levelNumber;
        this.goal = goal;
        this.duration = duration;
        this.unlockedItems = unlockedItems;
        this.customers = customers;
        this.booster = booster;
        this.ovens = createOvens();
    }
    
    private List<Oven> createOvens() {
        return List.of(
            new Oven("Regular"),
            new Oven("Curly"),
            new Oven("Chips"),
            new Oven("Wedges"),
            new Oven("Tornado"),
            new Oven("Mashed")
        );
    }

    public List<Oven> getOvens() {
        return ovens;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getGoal() {
        return goal;
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
    
    public BoosterTier getBoosterTier() {
        return booster;
    }
}
