package cimitpotato101;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
    protected int gold;
    private Map<String, Integer> upgradeLevels;
    
    public Player() {
        this.upgradeLevels = new HashMap<>(); // Inisialisasi di sini
        this.nama = "Player"; // Default name
        this.level = 1;     // Default level
        this.star = 0;
        this.gold = 0;      // Default gold
    }
    
    public Player(String nama) {
        this.nama = nama;
        this.level = 1;
        this.star = 0;
        this.gold = 0; // Atau gold awal default
        this.upgradeLevels = new HashMap<>(); // Inisialisasi di sini
    }

    
    public String getUsername() {
        return nama;
    }

    // Tambahkan setter untuk nama jika belum ada
    public void setUsername(String nama) {
        this.nama = nama;
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

    // Tambahkan setter untuk stars jika dibutuhkan dari SaveSlotData
    public void setStars(int newStars) {
        this.star = newStars;
    }

    public int getGold() { // Tidak lagi static
        return gold;
    }

    public void addGold(int golds) { // Tidak lagi static
        this.gold += golds;
    }

    // Tambahkan setter untuk gold
    public void setGold(int gold) { // Tidak lagi static
        this.gold = gold;
    }

    public void upgradeItem(String itemName) {
        int current = upgradeLevels.getOrDefault(itemName, 1);
        if (current < 3) {
            upgradeLevels.put(itemName, current + 1);
            // Anda mungkin perlu logika untuk mengurangi gold pemain di sini
        }
    }

    public int getUpgradeLevel(String itemName) {
        return upgradeLevels.getOrDefault(itemName, 1); // default Tier 1
    }

    // Untuk memastikan map tidak null saat MenuUnlock mengaksesnya
    public Map<String, Integer> getUpgradeLevels() {
        if (this.upgradeLevels == null) {
            this.upgradeLevels = new HashMap<>();
        }
        return this.upgradeLevels;
    }

    // Metode untuk inisialisasi (jika tidak di constructor)
    public void initializeUpgradeLevels() {
        if (this.upgradeLevels == null) {
            this.upgradeLevels = new HashMap<>();
        }
    }
   

}
