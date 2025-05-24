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
//        MainPanel mainMenu = new MainPanel(); // tampilkan GUI dari constructor
//        GamePanel framePlay = new GamePanel();
    }
    
    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new CimitPotato101());
        SwingUtilities.invokeLater(() -> new MainPanel()); // Ini akan menampilkan JFrame
//        SwingUtilities.invokeLater(() -> new SaveSlotMenu()); // Ini akan menampilkan JFrame
    }
}

