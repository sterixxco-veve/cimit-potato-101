/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Aspire
 */

public class SlotPanel extends JPanel {
    private Image backgroundImage;
    private SlotCard[] slotCards = new SlotCard[3];

    public SlotPanel(MainPanel mainFrame) {
        setLayout(null);
        setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));

        // === Background image ===
        URL bgUrl = getClass().getResource("/assets/chooseSlotBG.png");
        if (bgUrl != null) {
            backgroundImage = new ImageIcon(bgUrl).getImage();
        } else {
            System.err.println("Background image not found!");
        }

        // === Back button ===
        URL backUrl = getClass().getResource("/assets/backButton.png");
        JButton backButton = new JButton();
        if (backUrl != null) {
            ImageIcon backIcon = new ImageIcon(backUrl);
            backButton.setIcon(backIcon);
        } else {
            backButton.setText("Back");
        }
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setBounds(10, 430, 100, 100);
        backButton.addActionListener(e -> mainFrame.showMainMenu());
        add(backButton);

        // === Slot Cards Panel ===
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 80));
        rowPanel.setOpaque(false);
        rowPanel.setBounds(80, 50, 800, 500);
        
        for (int i = 0; i < 3; i++) {
            SaveSlotData data = SaveSlotUtils.loadSlotData(i + 1);
            slotCards[i] = new SlotCard(i + 1, data.getPlayerName(), data.getLevel(), data.getStars(), data.getTrophyType(), mainFrame);
            rowPanel.add(slotCards[i]);
        }
        add(rowPanel);
    }
    
    public void refresh() {
        for (SlotCard card : slotCards) {
            if (card != null) {
                card.refresh();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}