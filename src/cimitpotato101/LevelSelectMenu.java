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
        {140, 510}, // Level 1
        {230, 435}, // Level 2
        {310, 370}, // Level 3
        {390, 310}, // Level 4
        {480, 270}, // Level 5
        {570, 230}, // Level 6
        {670, 200}, // Level 7
        {790, 170}, // Level 8
        {920, 120}, // Level 9
        {1040, 80}  // Level 10
    };

    private Image background;
    private Image goldSign;
    private Image starSign;

    public LevelSelectMenu(MainPanel mainPanel, Player player, SaveSlotData slotData, int slotNumber) {
        this.mainPanel = mainPanel;
        this.player = player;
        this.slotData = slotData;
        this.slotNumber = slotNumber;

        setLayout(null); // pakai absolute layout supaya tombol bisa diatur manual

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
        goldPanel.setBounds(40, 10, 190, 60); // sesuaikan ukuran panel
        add(goldPanel);

        JLabel goldLabel = new JLabel(player.getGold() + "", SwingConstants.CENTER);
        goldLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        goldLabel.setForeground(new Color(242, 181, 30));
        goldLabel.setBounds(120, 25, 90, 30); // di atas panel gold
        add(goldLabel);

        // STARS panel kanan atas
        JLabel starPanel = new JLabel(new ImageIcon(starSign));
        starPanel.setBounds(960, 10, 190, 60);
        add(starPanel);

        JLabel starsLabel = new JLabel(player.getStars() + "", SwingConstants.CENTER);
        starsLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        starsLabel.setForeground(new Color(255, 200, 50));
        starsLabel.setBounds(1040, 25, 90, 30);
        add(starsLabel);
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
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(getClass().getResource(imgPath));
            if (!unlocked) {
                icon = new ImageIcon(toGrayScale(icon.getImage()));
            }
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

    // Konversi gambar menjadi grayscale
    private Image toGrayScale(Image srcImg) {
        BufferedImage src = new BufferedImage(
            srcImg.getWidth(null), srcImg.getHeight(null), BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2 = src.createGraphics();
        g2.drawImage(srcImg, 0, 0, null);
        g2.dispose();

        BufferedImageOp op = new ColorConvertOp(
                ColorSpace.getInstance(ERROR).getInstance(ColorSpace.CS_GRAY), null
        );
        return op.filter(src, null);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Gambar background
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}