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
        MainPanel mainMenu = new MainPanel(); // tampilkan GUI dari constructor
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CimitPotato101());
    }
}

