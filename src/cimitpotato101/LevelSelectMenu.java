package cimitpotato101;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LevelSelectMenu extends JPanel {
    private MainPanel mainPanel;
    private Player player;
    private SaveSlotData slotData;
    private int slotNumber;
    private int maxLevel = 10;

    // Koordinat Tombol level
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

    private JLabel goldLabel;
    private JLabel starsLabel;

    // Simpan semua tombol level agar bisa dibersihkan saat refresh
    private JButton[] levelButtons = new JButton[maxLevel];

    public LevelSelectMenu(MainPanel mainPanel, Player player, SaveSlotData slotData, int slotNumber) {
        this.mainPanel = mainPanel;
        this.player = player;
        this.slotData = slotData;
        this.slotNumber = slotNumber;

        setLayout(null);

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
        if (url != null) return new ImageIcon(url).getImage();
        System.out.println("Gagal load asset: " + path);
        return null;
    }

    private void addHUD() {
        // GOLD panel kiri atas
        JLabel goldPanel = new JLabel(new ImageIcon(goldSign));
        goldPanel.setBounds(40, 0, 260, 100);
        add(goldPanel);

        goldLabel = new JLabel(String.valueOf(player.getGold()), SwingConstants.CENTER);
        goldLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        goldLabel.setForeground(new Color(0, 0, 0));
        goldLabel.setBounds(145, 55, 90, 30);
        add(goldLabel);
        setComponentZOrder(goldLabel, 0);
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
            levelButtons[idx] = btn;
        }
    }

    // Tambahan: untuk refresh layout level setelah update
    private void refreshLevelButtons() {
        // Remove semua tombol level yang sudah ada
        for (JButton btn : levelButtons) {
            if (btn != null) remove(btn);
        }
        addLevelButtons();
        revalidate();
        repaint();
    }

    private JButton createLevelButton(int level, boolean unlocked) {
        boolean isCompleted = slotData.getStarsForLevel(level) > 0;
        String imgPath = isCompleted
            ? "/assets/Clevel" + level + ".png"
            : "/assets/Level" + level + ".png";

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

        btn.setEnabled(unlocked);

        if (unlocked) {
            btn.addActionListener(e -> {
                slotData.setLevel(level);
                mainPanel.startGame(slotData, slotNumber);
            });
        }
        return btn;
    }

    private void addBackButton() {
        JButton backBtn = new JButton();

        // Load image dari path
        URL backUrl = getClass().getResource("/assets/backButton.png");
        if (backUrl != null) {
            ImageIcon backIcon = new ImageIcon(backUrl);
            backBtn.setIcon(backIcon);
            backBtn.setBounds(10, 430, backIcon.getIconWidth(), backIcon.getIconHeight()); // sesuaikan ukuran dgn src gambar
        } else {
            System.err.println("Back button image not found!");
            backBtn.setText("Back");
            backBtn.setBounds(10, 200, 100, 100); // Atur posisi tombol
            backBtn.setBackground(new Color(255, 240, 200));
            backBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        }

        // Style agar hanya gambar yang tampak (tanpa border/warna)
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // back button on clicked
        backBtn.addActionListener(e -> {
            mainPanel.showSlotSelectionScreen();
        });

        add(backBtn);
    }

    // Method untuk refresh label gold & stars setelah selesai main
    public void refreshHUD() {
        if (goldLabel != null) goldLabel.setText(String.valueOf(player.getGold()));
        if (starsLabel != null) starsLabel.setText(String.valueOf(player.getStars()));
        refreshLevelButtons(); // buat refresh tombol level
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}