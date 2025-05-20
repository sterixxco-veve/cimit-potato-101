/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 *
 * @author Aspire
 */
public class GamePanel extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel;
    private String[] customerImages = {
        "assets/customer1.png",
        "assets/customer2.png",
        "assets/customer3.png"
    };
    private Random rand = new Random();

    public GamePanel() {
        setTitle("Cimit Potato 101");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(970, 570));
        setContentPane(layeredPane);

        backgroundLabel = new JLabel(new ImageIcon(getClass().getResource("/assets/gameBG.png")));
        backgroundLabel.setBounds(-26, -6, 970, 570);
        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        for (int i = 0; i < 3; i++) {
            String randomImage = customerImages[rand.nextInt(customerImages.length)];
            JLabel customerLabel = new JLabel(new ImageIcon(randomImage));
            customerLabel.setBounds(100 + i * 200, 150, 100, 150);
            layeredPane.add(customerLabel, Integer.valueOf(1));
        }

        JLabel microwave = new JLabel(new ImageIcon("assets/microwave.png"));
        microwave.setBounds(100, 450, 100, 100);
        layeredPane.add(microwave, Integer.valueOf(1));

        setSize(970, 570);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
   

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GamePanel());
    }
}

