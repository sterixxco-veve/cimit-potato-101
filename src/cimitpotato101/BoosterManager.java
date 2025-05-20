/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cimitpotato101;

/**
 *
 * @author Gracia Krisnanda
 */
public class BoosterManager {
    
    public static BoosterTier getBoosterForLevel(int level) {
        if (level >= 1 && level <= 3) {
            return new BoosterTier("Poster", 2.0, 1.5);
        } else if (level >= 4 && level <= 6) {
            return new BoosterTier("Billboard", 4.0, 2.0);
        } else {
            return new BoosterTier("Mobile and News", 6.0, 2.5);
        }
    }
}


