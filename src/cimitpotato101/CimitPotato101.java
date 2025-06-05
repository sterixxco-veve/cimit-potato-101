package cimitpotato101;

import javax.swing.SwingUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aspire
 */
public class CimitPotato101{

    public CimitPotato101() {
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainPanel()); // Ini akan menampilkan JFrame
        AudioPlayer a = new AudioPlayer();
        a.playSound();
//        SaveSlotUtils s = new SaveSlotUtils(); //Ini reset data slot
//        s.saveSlotData(3, new SaveSlotData("", 0, 0));

     
    }
}

