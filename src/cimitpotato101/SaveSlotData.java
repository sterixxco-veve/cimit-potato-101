/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

/**
 *
 * @author Aspire
 */

import java.io.Serializable;

public class SaveSlotData implements Serializable {
    private String playerName;
    private int level;
    private int stars;

    public SaveSlotData(String playerName, int level, int stars) {
        this.playerName = playerName;
        this.level = level;
        this.stars = stars;
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
}

