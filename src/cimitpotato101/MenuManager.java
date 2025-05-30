package cimitpotato101;


import cimitpotato101.Potato;
import cimitpotato101.MenuItem;
import cimitpotato101.Topping;
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
public class MenuManager {
    private Player player;

    public MenuManager(Player player) {
        this.player = player;
    }

    public Level createLevel(int levelNumber, int goal, int duration, List<Customer> customers) {
        List<MenuItem> unlockedItems = MenuUnlock.getUnlockedItems(levelNumber, player);
        return new Level(levelNumber, goal, duration, unlockedItems, customers);
    }


    // Helper untuk filter potato
    public static List<Potato> filterPotatoes(List<MenuItem> items) {
        List<Potato> result = new ArrayList<>();
        for (MenuItem item : items) {
            if (item instanceof Potato) {
                result.add((Potato) item);
            }
        }
        return result;
    }

    // Helper untuk filter topping/sauce berdasarkan kategori
    public static List<Topping> filterAddonsByKategori(List<MenuItem> items, String kategori) {
        List<Topping> result = new ArrayList<>();
        for (MenuItem item : items) {
            if (item instanceof Topping) {
                Topping topping = (Topping) item;
                if (topping.getKategori().equalsIgnoreCase(kategori)) {
                    result.add(topping);
                }
            }
        }
        return result;
    }
}
