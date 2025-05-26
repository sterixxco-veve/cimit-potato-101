/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import java.util.ArrayList;

/**
 *
 * @author Aspire
 */
public class RegularPotato extends Potato{
    private ArrayList<String> sauces;
    private ArrayList<String> toppings;

  
    public RegularPotato() {
        super("Regular Potato", 0, 0, "/assets/potatoRegular.png");
        this.toppings = new ArrayList<>();
        this.sauces = new ArrayList<>();
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
            this.imagePath = "/assets/potatoRegular.png";
        } else {
            StringBuilder path = new StringBuilder("/assets/regular");
            for (String topping : toppings) path.append("_").append(topping);
            for (String sauce : sauces) path.append("_").append(sauce);
            path.append(".png");
            System.out.println(path.toString());
            this.imagePath = path.toString();
        }
    }

    

    public ArrayList<String> getToppings() {
       return toppings;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }
}
