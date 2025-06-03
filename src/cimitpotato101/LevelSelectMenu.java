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
    

    // Tombol level
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

    // Asset bintang
    private Image[] starColored = new Image[3]; // star1, star2, star3
    private Image[] starGray = new Image[3];    // starAbu1, starAbu2, starAbu3

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
        addLevelButtonsWithStars();
        addBackButton();
    }

    private void loadImages() {
        background = loadImage("/assets/LevelSelectionMenu.gif");
        goldSign = loadImage("/assets/GoldSign.png");
        starSign = loadImage("/assets/StarSign.png");
        // Bintang berwarna
        starColored[0] = loadImage("/assets/star1.png");
        starColored[1] = loadImage("/assets/star2.png");
        starColored[2] = loadImage("/assets/star3.png");
        // Bintang abu-abu
        starGray[0] = loadImage("/assets/starAbu1.png");
        starGray[1] = loadImage("/assets/starAbu2.png");
        starGray[2] = loadImage("/assets/starAbu3.png");
    }

    private Image loadImage(String path) {
    URL url = getClass().getResource(path);
    if (url != null) {
        return new ImageIcon(url).getImage();
    } else {
        System.out.println("Gagal load asset: " + path);
        return null;
    }
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

    private void addLevelButtonsWithStars() {
    int unlockedLevel = player.getCurrentLevel();
    int starSize = 28;
    int spacing = 8;
    int offsetY = 112;

    for (int i = 1; i <= maxLevel; i++) {
        int idx = i - 1;
        int x = levelPositions[idx][0];
        int y = levelPositions[idx][1];

        JButton btn = createLevelButton(i, i <= unlockedLevel);
        btn.setBounds(x, y, 90, 90);
        add(btn);

        // Bintang-bintang (kiri, tengah, kanan)
        int stars = slotData.getStarsForLevel(i); // level 1 = index 0

        int centerX = x + 45; // center tombol
        int starY = y + offsetY;

        for (int b = 0; b < 3; b++) {
            Image img = (stars > b) ? starColored[b] : starGray[b];
            if (img != null) {
                ImageIcon icon = new ImageIcon(img.getScaledInstance(starSize, starSize, Image.SCALE_SMOOTH));
                JLabel starLabel = new JLabel(icon);

                int starX;
                if (b == 0)      starX = centerX - starSize - spacing; // kiri
                else if (b == 1) starX = centerX - starSize/2;         // tengah
                else             starX = centerX + starSize + spacing - starSize; // kanan

                starLabel.setBounds(starX, starY, starSize, starSize);
                starLabel.setOpaque(false); // transparan background

                add(starLabel);
                setComponentZOrder(starLabel, 0); // pastikan bintang di depan tombol
            }
        }
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
        JButton backBtn = new JButton();

        // Load image dari path
        URL backUrl = getClass().getResource("/assets/backButton.png");
        if (backUrl != null) {
            ImageIcon backIcon = new ImageIcon(backUrl);
            backBtn.setIcon(backIcon);
            backBtn.setBounds(10, 430, backIcon.getIconWidth(), backIcon.getIconHeight()); // sesuaikan ukuran dgn gambar
        } else {
            System.err.println("Back button image not found!");
            backBtn.setText("Back");
            backBtn.setBounds(10, 200, 100, 100); // 👈 Atur posisi tombol
            backBtn.setBackground(new Color(255, 240, 200));
            backBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        }

        // Style agar hanya gambar yang tampak (tanpa border/warna)
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Action
        backBtn.addActionListener(e -> {
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
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }

        int logoWidth = 90;   // lebar tombol/logo level (sesuaikan dengan asset kamu)
        int logoHeight = 90;  // tinggi tombol/logo level
        int starSize = 28;    // ukuran bintang
        int spacing = 7;      // jarak antar bintang
        int offsetY = 80;     // jarak dari atas logo ke posisi bintang

        for (int i = 0; i < maxLevel; i++) {
            int logoX = levelPositions[i][0];
            int logoY = levelPositions[i][1];

            // Titik tengah bawah logo level
            int centerX = logoX + logoWidth / 2;
            int baseY = logoY + offsetY;

            int stars = slotData.getStarsForLevel(i + 1);

            // Hitung posisi X masing-masing bintang (kiri, tengah, kanan)
            int[] starX = new int[] {
                centerX - starSize - spacing, // kiri
                centerX - starSize / 2,       // tengah
                centerX + starSize + spacing - starSize // kanan
            };

            // Gambar bintang satu per satu
            for (int b = 0; b < 3; b++) {
                Image img = (stars > b) ? starColored[b] : starGray[b];
                if (img != null) {
                    g.drawImage(img, starX[b], baseY, starSize, starSize, this);
                }
            }
        }
    }
}