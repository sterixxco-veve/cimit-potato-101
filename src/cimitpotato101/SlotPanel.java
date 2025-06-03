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

    public SlotPanel(MainPanel mainFrame) {
        setLayout(null); // Absolute positioning
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
            System.err.println("Back button image not found!");
            backButton.setText("Back");
        }
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);

        backButton.setBounds(10, 430, 100, 100); // 👈 Atur posisi tombol
        backButton.addActionListener(e -> mainFrame.showMainMenu());
        add(backButton);

        // === Slot Cards Panel ===
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 80));
        rowPanel.setOpaque(false);
        rowPanel.setBounds(80, 50, 800, 500); // 👈 Atur posisi slot cards
        for (int i = 1; i <= 3; i++) {
            SaveSlotData data = SaveSlotUtils.loadSlotData(i);
            SlotCard card = new SlotCard(i, data.getPlayerName(), data.getLevel(), data.getStars(), data.getTrophyType(), mainFrame);
            rowPanel.add(card);
        }
        add(rowPanel); // 👈 Tambahkan ke SlotPanel
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
