/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Aspire
 */
public class SlotPanel extends JPanel {
    private Image backgroundImage;

    public SlotPanel(MainPanel mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));

        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 80));
        rowPanel.setOpaque(false);

        // Load background image
        URL bgUrl = getClass().getResource("/assets/chooseSlotBG.png");
        if (bgUrl != null) {
            backgroundImage = new ImageIcon(bgUrl).getImage();
        } else {
            System.err.println("Background image not found!");
        }

        // Generate slot cards
        for (int i = 1; i <= 3; i++) {
            SaveSlotData data = SaveSlotUtils.loadSlotData(i);
            SlotCard card = new SlotCard(i, data.getPlayerName(), data.getLevel(), data.getStars(), mainFrame);
            rowPanel.add(card);
        }

        add(Box.createVerticalGlue());
        add(rowPanel);
        add(Box.createVerticalGlue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
