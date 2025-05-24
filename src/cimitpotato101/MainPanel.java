/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Aspire
 */


public class MainPanel extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainPanel() {
        setBackground(Color.yellow);
        setTitle("Cimit Potato 101");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(970, 570);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Tambahkan panel-panel ke dalam CardLayout
        cardPanel.add(new MenuPanel(this), "menu");
        cardPanel.add(new SlotPanel(this), "slots");
        cardPanel.add(new GamePanel(), "game");

        add(cardPanel);

        setVisible(true);
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }
}