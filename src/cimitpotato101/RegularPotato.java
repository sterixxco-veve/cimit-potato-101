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
    private ArrayList<String> toppings; 
    
    public RegularPotato() {
        super("Regular Potato", 0, 0, "/assets/potatoRegular.png");
        this.toppings = new ArrayList<>();
    }
    
    public void addTopping(String topping) {
        if (!toppings.contains(topping)) {
            toppings.add(topping);
            // Ubah image path sesuai topping terakhir
            this.imagePath = "/assets/regular_" + topping + ".png";
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
