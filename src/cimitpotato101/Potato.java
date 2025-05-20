package cimitpotato101;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Gracia Krisnanda
 */
public class Potato implements MenuItem {
    protected String name;
    protected int price;
    protected int time;
    protected int upgradeLevel;
    protected String imagePath;

    public Potato(String name, int price, int time, String imagePath) {
        this.name = name;
        this.price = price;
        this.time = time;
        this.upgradeLevel = 0;
        this.imagePath = imagePath;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public int getHarga() {
        return price + (upgradeLevel - 1) * 3;
    }

    @Override
    public String getNama() {
        return name;
    }
    
    public int getTimeAtLevel(int level) {
        return Math.max(1, time - level / 3);
    }  
    
    public void setUpgradeLevel(int level) {
        this.upgradeLevel = level;
    }
    
    public String getImagePath() {
        return imagePath;
    }
}
