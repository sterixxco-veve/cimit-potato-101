/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cimitpotato101;

/**
 *
 * @author Gracia Krisnanda
 */
public class BoosterTier {
    private String name;
    private double revenueMultiplier;
    private double tipMultiplier;

    public BoosterTier(String name, double revenueMultiplier, double tipMultiplier) {
        this.name = name;
        this.revenueMultiplier = revenueMultiplier;
        this.tipMultiplier = tipMultiplier;
    }

    public String getName() {
        return name;
    }

    public double getRevenueMultiplier() {
        return revenueMultiplier;
    }

    public double getTipMultiplier() {
        return tipMultiplier;
    }
}
