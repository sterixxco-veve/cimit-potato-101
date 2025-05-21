package cimitpotato101;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Gracia Krisnanda
 */
public class Topping implements MenuItem {
    private String nama;
    
    private int harga;
    private int time;
    private String kategori; // "sauce" atau "topping"
    protected int upgradeLevel;
    protected String imagePath;

    public Topping(String nama, int harga, String kategori, int level, String imagePath) {
        this.nama = nama;
        this.harga = harga;
        this.time = 0;
        this.kategori = kategori.toLowerCase();
        this.upgradeLevel = level;
        this.imagePath = imagePath;
    }

    @Override
    public String getNama() {
        return nama;
    }

    @Override
    public int getHarga() {
        return harga;
    }

    @Override
    public int getTime() {
        return time;
    }

    public String getKategori() {
        return kategori;
    }
    
    public void setUpgradeLevel(int level) {
        this.upgradeLevel = level;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    
}
