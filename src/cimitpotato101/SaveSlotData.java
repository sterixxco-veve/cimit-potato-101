package cimitpotato101;

import java.io.Serializable;

public class SaveSlotData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerName;
    private int level;
    private int stars;
    private int gold;
    private int[] starsPerLevel;

    // Constructor utama
    public SaveSlotData(String playerName, int level, int stars, int gold) {
        this.playerName = playerName;
        this.level = level;
        this.stars = stars;
        this.gold = gold;
        this.initStarsPerLevel(); // pastikan array terinisialisasi
    }

    // Constructor alternatif
    public SaveSlotData(String playerName, int level, int stars) {
        this.playerName = playerName;
        this.level = level;
        this.stars = stars;
        this.gold = 0;
        this.initStarsPerLevel();
    }

    // Constructor default
    public SaveSlotData() {
        this.playerName = "";
        this.level = 1;
        this.stars = 0;
        this.gold = 0;
        this.initStarsPerLevel();
    }

    // Inisialisasi array jika null
    private void initStarsPerLevel() {
        if (this.starsPerLevel == null) {
            this.starsPerLevel = new int[10];
            // Optional: inisialisasi semua 0
            for (int i = 0; i < 10; i++) this.starsPerLevel[i] = 0;
        }
    }

    // Getter dan setter
    public String getPlayerName() { return playerName; }
    public int getLevel() { return level; }
    public int getStars() { return stars; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setLevel(int level) { this.level = level; }
    public void setStars(int stars) { this.stars = stars; }
    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }

    // PROTEKSI NPE!
    public int getStarsForLevel(int level) {
        initStarsPerLevel();
        if (level >= 1 && level <= 10) return starsPerLevel[level-1];
        else return 0;
    }

    public void setStarsForLevel(int level, int stars) {
        initStarsPerLevel();
        if (level >= 1 && level <= 10) starsPerLevel[level-1] = stars;
    }
}