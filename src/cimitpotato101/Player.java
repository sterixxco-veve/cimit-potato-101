package cimitpotato101;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
        this.upgradeLevels = new HashMap<>();
        this.nama = "Player";
        this.level = 1;
        this.star = 0;
        this.gold = 0;
    }

    public Player(String nama) {
        this.nama = nama;
        this.level = 1;
        this.star = 0;
        this.gold = 0;
        this.upgradeLevels = new HashMap<>();
    }
    
    
    

    public String getUsername() {
        return nama;
    }

    public void setUsername(String nama) {
        this.nama = nama;
    }

    public int getCurrentLevel() {
        return level;
    }

    public void setCurrentLevel(int level) {
        this.level = level;
    }

    // Getter yang kamu butuhkan:
    public int getStars() {
        return star;
    }

    // Getter kompatibel (boleh dihapus jika tidak dipakai lagi)
    public int getTotalStars() {
        return star;
    }

    public void addStars(int stars) {
        this.star += stars;
    }

    public void setStars(int newStars) {
        this.star = newStars;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int golds) {
        this.gold += golds;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void upgradeItem(String itemName) {
        int current = upgradeLevels.getOrDefault(itemName, 1);
        if (current < 3) {
            upgradeLevels.put(itemName, current + 1);
        }
    }

    public int getUpgradeLevel(String itemName) {
        return upgradeLevels.getOrDefault(itemName, 1);
    }

    public Map<String, Integer> getUpgradeLevels() {
        if (this.upgradeLevels == null) {
            this.upgradeLevels = new HashMap<>();
        }
        return this.upgradeLevels;
    }

    public void initializeUpgradeLevels() {
        if (this.upgradeLevels == null) {
            this.upgradeLevels = new HashMap<>();
        }
    }
}