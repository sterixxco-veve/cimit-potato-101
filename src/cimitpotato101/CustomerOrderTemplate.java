/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cimitpotato101;

/**
 *
 * @author Gracia Krisnanda
 */
public class CustomerOrderTemplate {
    private String potato;
    private String topping; // bisa null
    private String sauce;   // bisa null
    private boolean isRare;

    public CustomerOrderTemplate(String potato, String topping, String sauce, boolean isRare) {
        this.potato = potato;
        this.topping = topping;
        this.sauce = sauce;
        this.isRare = isRare;
    }

    public String getPotato() { return potato; }
    public String getTopping() { return topping; }
    public String getSauce() { return sauce; }
    public boolean isRare() { return isRare; }
}
