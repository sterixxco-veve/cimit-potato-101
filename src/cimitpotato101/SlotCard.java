/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Aspire
 */
public class SlotCard extends JPanel {
    private final int slotNumber;
    private String playerName;
    private int level;
    private int stars;
    private final MainPanel mainFrame;

    public SlotCard(int slotNumber, String playerName, int level, int stars, MainPanel mainFrame) {
        this.slotNumber = slotNumber;
        this.playerName = playerName;
        this.level = level;
        this.stars = stars;
        this.mainFrame = mainFrame;

        setPreferredSize(new Dimension(220, 320));
        setOpaque(false);
        setLayout(null);

        addSlotComponents();
    }

    private void addSlotComponents() {
        JLabel numberCircle = createLabel(String.valueOf(slotNumber), 90, 10, 40, 40, 18, true);
        numberCircle.setBackground(Color.WHITE);
        numberCircle.setOpaque(true);
        numberCircle.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        add(numberCircle);

        add(createLabel("Save Slot", 40, 60, 140, 20, 14, false));
        add(createLabel(playerName != null ? playerName : "Empty Slot", 40, 90, 140, 30, 20, true));
        add(createLabel(playerName != null ? "LEVEL " + level : "", 40, 130, 140, 25, 16, false));
        add(createLabel(playerName != null ? stars + " stars" : "", 40, 160, 140, 20, 14, false));

        JButton playBtn = new JButton("Play ▶");
        playBtn.setBounds(60, 230, 100, 40);
        playBtn.setFocusPainted(false);
        playBtn.setBackground(new Color(0, 123, 255));
        playBtn.setForeground(Color.WHITE);
        playBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        playBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playBtn.addActionListener(e -> {
            boolean isEmpty = (playerName == null || playerName.isEmpty());
            if (isEmpty) {
                String inputName = JOptionPane.showInputDialog(this, "Enter your name:");
                if (inputName != null && !inputName.trim().isEmpty()) {
                    playerName = inputName.trim();
                    level = 1;
                    stars = 0;
                    SaveSlotData newData = new SaveSlotData(playerName, level, stars);
                    SaveSlotUtils.saveSlotData(slotNumber, newData);
                } else {
                    return; // Cancelled or empty input
                }
            }
            // Lanjut ke game (bisa tambahkan data ke game panel jika perlu)
            mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "game");
        });
        add(playBtn);
    }

    private JLabel createLabel(String text, int x, int y, int w, int h, int fontSize, boolean bold) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setBounds(x, y, w, h);
        label.setFont(new Font("Comic Sans MS", bold ? Font.BOLD : Font.PLAIN, fontSize));
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

        float[] dashPattern = {10, 5};
        Stroke dashed = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0);
        g2.setStroke(dashed);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
    }
}

