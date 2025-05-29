package cimitpotato101;

import java.io.Serializable;

public class SaveSlotData implements Serializable {
    private static final long serialVersionUID = 1L; // Tambahkan ini agar serialisasi stabil

    private String playerName;
    private int level;
    private int stars;
    private int gold;

    // Constructor utama
    public SaveSlotData(String playerName, int level, int stars, int gold) {
        this.playerName = playerName;
        this.level = level;
        this.stars = stars;
        this.gold = gold;
    }

    // Constructor alternatif (tanpa gold, default gold=0)
    public SaveSlotData(String playerName, int level, int stars) {
        this.playerName = playerName;
        this.level = level;
        this.stars = stars;
        this.gold = 0;
    }

    // Constructor default (jika perlu untuk serialisasi)
    public SaveSlotData() {
        this.playerName = "";
        this.level = 1;
        this.stars = 0;
        this.gold = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getLevel() {
        return level;
    }

    public int getStars() {
        return stars;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}