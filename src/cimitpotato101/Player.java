package cimitpotato101;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Gracia Krisnanda
 */
public class Player implements Serializable {
    protected String nama;
    protected int level;
    protected int star;
    protected static int gold;
    private Map<String, Integer> upgradeLevels;
	
    public String getUsername() {
        return nama;
    }

    public int getCurrentLevel() {
        return level;
    }

    public void setCurrentLevel(int level) {
        this.level = level;
    }

    public int getTotalStars() {
        return star;
    }

    public void addStars(int stars) {
        this.star += stars;
    }
    
    public int getGold() {
        return gold;
    }

    public void addGold(int golds) {
        Player.gold += golds;
    }
    
    public void upgradeItem(String itemName) {
        int current = upgradeLevels.getOrDefault(itemName, 1);
        if (current < 3) {
            upgradeLevels.put(itemName, current + 1);
        }
    }

    public int getUpgradeLevel(String itemName) {
        return upgradeLevels.getOrDefault(itemName, 1); // default Tier 1
    }
   

}
