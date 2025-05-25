package cimitpotato101;

import java.util.Random;

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
    private String customerImageID; // ID untuk gambar customer (misal: "customer1", "customer2")

    public Customer(MenuItem potato, Topping topping, Topping sauce, int patienceTime, String customerImageID) {
        this.potato = potato;
        this.topping = topping;
        this.sauce = sauce;
        this.patienceTime = patienceTime;
        this.customerImageID = customerImageID;
    }

    public MenuItem getPotato() { return potato; }
    public Topping getTopping() { return topping; }
    public Topping getSauce() { return sauce; }
    public int getPatienceTime() { return patienceTime; }
    public String getCustomerImageID() { return customerImageID; } // Getter untuk image ID

    public void decreasePatience(int seconds) {
        patienceTime -= seconds;
        if (patienceTime < 0) {
            patienceTime = 0;
        }
    }

    public boolean isAngry() {
        return patienceTime <= 0;
    }
}
