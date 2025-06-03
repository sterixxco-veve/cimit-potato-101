/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;
import javax.swing.*;
import java.awt.*;


public class SaveSlotMenu extends JFrame {
    private JPanel mainPanel;
    private SlotCard[] slots = new SlotCard[3];
    private MainPanel mainFrame;

    public SaveSlotMenu(MainPanel mainFrame) {
        this.mainFrame = mainFrame;
        setTitle("Choose a Slot");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < 3; i++) {
            SaveSlotData data = SaveSlotUtils.loadSlotData(i + 1);
            slots[i] = new SlotCard(i + 1, data.getPlayerName(), data.getLevel(), data.getStars(), data.getTrophyType(), mainFrame);
            mainPanel.add(slots[i]);
        }

        add(mainPanel);
        setVisible(true);
    }
}