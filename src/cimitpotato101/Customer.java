package cimitpotato101;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Gracia Krisnanda
 */
public class Customer {
    private MenuItem potato;
    private Topping topping;
    private Topping sauce;
    private int patienceTime;

    public Customer(MenuItem potato, Topping topping, Topping sauce, int patienceTime) {
        this.potato = potato;
        this.topping = topping;
        this.sauce = sauce;
        this.patienceTime = patienceTime;
    }

    public MenuItem getPotato() { return potato; }
    public Topping getTopping() { return topping; }
    public Topping getSauce() { return sauce; }
    public int getPatienceTime() { return patienceTime; }

    public void decreasePatience(int seconds) {
        patienceTime -= seconds;
    }

    public boolean isAngry() {
        return patienceTime <= 0;
    }
}
