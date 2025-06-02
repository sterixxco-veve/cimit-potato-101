package cimitpotato101;

import java.util.ArrayList;

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
    protected int upgradeLevel;
    protected String imagePath;
    private ArrayList<String> sauces;
    private ArrayList<String> toppings;
    public Potato(String name, int price, String imagePath) {
        this.name = name;
        this.price = price;
        this.upgradeLevel = 1;
        this.imagePath = imagePath;
        this.toppings = new ArrayList<>();
        this.sauces = new ArrayList<>();
    }

    @Override
    public int getHarga() {
        return price + (upgradeLevel - 1) * 3;
    }

    @Override
    public String getNama() {
        return name;
    } 
    
    public void setUpgradeLevel(int level) {
        this.upgradeLevel = level;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    
    public boolean addTopping(Topping topping) {
        String kategori = topping.getKategori();
        String nama = topping.getNama().toLowerCase();
        System.out.println("run");

        if (kategori.equals("sauce") && sauces.size() == 0) {
            sauces.add(nama);
            updateImagePath();
            return true;
        } else if (kategori.equals("topping") && toppings.size() == 0) {
            toppings.add(nama);
            updateImagePath();
            return true;
        }
        return false;
        
    }

    private void updateImagePath() {
        if (sauces.isEmpty() && toppings.isEmpty()) {
//            this.imagePath = "/assets/potatoRegular.png";
        } else {
            StringBuilder path = new StringBuilder("/assets/"+name);
            for (String topping : toppings) path.append("_").append(topping);
            for (String sauce : sauces) path.append("_").append(sauce);
            path.append(".png");
            System.out.println(path.toString());
            this.imagePath = path.toString();
        }
    }

    public ArrayList<String> getSauces() {
        return sauces;
    }

    

    public ArrayList<String> getToppings() {
       return toppings;
    }

 
    
}
