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
        
//        sementara hardcode utk level 1
        playBtn.addActionListener(e -> {
        boolean isEmpty = (this.playerName == null || this.playerName.isEmpty() || this.playerName.equals("Empty Slot"));
        String playerNameForGame = this.playerName; // Ambil nama yang sudah ada
        int starsForGame = this.stars; // Ambil bintang yang sudah ada

        if (isEmpty) {
            String inputName = JOptionPane.showInputDialog(this, "Enter your name for Level 1 test:");
            if (inputName != null && !inputName.trim().isEmpty()) {
                playerNameForGame = inputName.trim();
                // Untuk test Level 1, level dan bintang bisa di-reset atau default
                SaveSlotData newSlotData = new SaveSlotData(playerNameForGame, 1, 0);
                SaveSlotUtils.saveSlotData(slotNumber, newSlotData); // Simpan data pemain baru

                // Update tampilan kartu jika ini slot baru
                this.playerName = playerNameForGame;
                this.level = 1;
                this.stars = 0;
                // Anda mungkin perlu refresh label-label di SlotCard di sini
                repaint(); 
            } else {
                return; // Batal atau input kosong
            }
        } else {
            // Jika slot sudah ada, kita tetap pakai nama dan bintangnya, tapi levelnya paksa ke 1 untuk testing
            // (Optional) Muat ulang data slot untuk memastikan konsistensi,
            // tapi karena kita paksa level 1, ini mungkin tidak terlalu krusial untuk nama/bintang.
            SaveSlotData existingData = SaveSlotUtils.loadSlotData(slotNumber);
            playerNameForGame = existingData.getPlayerName();
            starsForGame = existingData.getStars();
        }

        // Buat SaveSlotData yang SELALU untuk LEVEL 1, tapi dengan nama & bintang dari slot
        SaveSlotData slotDataForLevel1 = new SaveSlotData(playerNameForGame, 1, starsForGame);

        // Panggil metode startGame di MainPanel (mainFrame adalah instance MainPanel)
        mainFrame.startGame(slotDataForLevel1);
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

