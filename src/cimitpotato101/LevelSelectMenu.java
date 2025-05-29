package cimitpotato101;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;

public class LevelSelectMenu extends JPanel {
    private MainPanel mainPanel;
    private Player player;
    private SaveSlotData slotData;
    private int slotNumber;
    private int maxLevel = 10;

    // Koordinat tombol level (harus disesuaikan dengan background path)
    private final int[][] levelPositions = {
        {390, 450}, // Level 1
        {480, 435}, // Level 2
        {450, 360}, // Level 3
        {390, 310}, // Level 4
        {500, 270}, // Level 5
        {390, 230}, // Level 6
        {480, 160}, // Level 7
        {390, 140}, // Level 8
        {460, 90},  // Level 9
        {390, 40}   // Level 10
    };

    private Image background;
    private Image goldSign;
    private Image starSign;

    // Tambahkan field label agar bisa di-refresh
    private JLabel goldLabel;
    private JLabel starsLabel;

    public LevelSelectMenu(MainPanel mainPanel, Player player, SaveSlotData slotData, int slotNumber) {
        this.mainPanel = mainPanel;
        this.player = player;
        this.slotData = slotData;
        this.slotNumber = slotNumber;

        setLayout(null); // absolute layout

        loadImages();
        addHUD();
        addLevelButtons();
        addBackButton();
    }

    private void loadImages() {
        background = loadImage("/assets/LevelSelectionMenu.gif");
        goldSign = loadImage("/assets/GoldSign.png");
        starSign = loadImage("/assets/StarSign.png");
    }

    private Image loadImage(String path) {
        URL url = getClass().getResource(path);
        if (url != null) {
            return new ImageIcon(url).getImage();
        }
        return null;
    }

    private void addHUD() {
        // GOLD panel kiri atas
        JLabel goldPanel = new JLabel(new ImageIcon(goldSign));
        goldPanel.setBounds(40, 0, 260, 100); // ukuran panel gold
        add(goldPanel);

        goldLabel = new JLabel(String.valueOf(player.getGold()), SwingConstants.CENTER);
        goldLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        goldLabel.setForeground(new Color(0, 0, 0));
        goldLabel.setBounds(145, 55, 90, 30); // di atas panel gold
        add(goldLabel);
        setComponentZOrder(goldLabel, 0); // goldLabel paling atas
        setComponentZOrder(goldPanel, 1);

        // STARS panel kanan atas
        JLabel starPanel = new JLabel(new ImageIcon(starSign));
        starPanel.setBounds(680, 0, 260, 100);
        add(starPanel);

        starsLabel = new JLabel(String.valueOf(player.getStars()), SwingConstants.CENTER);
        starsLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        starsLabel.setForeground(new Color(0, 0, 0));
        starsLabel.setBounds(820, 57, 90, 30);
        add(starsLabel);
        setComponentZOrder(starsLabel, 0);
        setComponentZOrder(starPanel, 1);
    }

    private void addLevelButtons() {
        int unlockedLevel = player.getCurrentLevel();
        for (int i = 1; i <= maxLevel; i++) {
            int idx = i - 1;
            int x = levelPositions[idx][0];
            int y = levelPositions[idx][1];

            JButton btn = createLevelButton(i, i <= unlockedLevel);
            btn.setBounds(x, y, 90, 90);
            add(btn);
        }
    }

    private JButton createLevelButton(int level, boolean unlocked) {
        String imgPath = "/assets/Level" + level + ".png";
        ImageIcon icon;
        try {
            icon = new ImageIcon(getClass().getResource(imgPath));
        } catch (Exception e) {
            icon = new ImageIcon();
        }
        JButton btn = new JButton(icon);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Level " + level);

        if (!unlocked) {
            btn.setEnabled(false);
        } else {
            btn.addActionListener(e -> {
                slotData.setLevel(level);
                mainPanel.startGame(slotData, slotNumber);
            });
        }
        return btn;
    }

    private void addBackButton() {
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(40, 500, 100, 40);
        backBtn.setBackground(new Color(255, 240, 200));
        backBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            // Kembali ke panel Slot
            mainPanel.getCardLayout().show(mainPanel.getCardPanel(), "slots");
        });
        add(backBtn);
    }

    // Method untuk refresh label gold & stars setelah selesai main
    public void refreshHUD() {
        if (goldLabel != null) goldLabel.setText(String.valueOf(player.getGold()));
        if (starsLabel != null) starsLabel.setText(String.valueOf(player.getStars()));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Gambar background
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}