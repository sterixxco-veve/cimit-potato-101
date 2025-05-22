/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Aspire
 */


public class MainPanel extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainPanel() {
        setBackground(Color.yellow);
        setTitle("Cimit Potato 101");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(970, 570);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Tambahkan panel-panel ke dalam CardLayout
        cardPanel.add(new MenuPanel(this), "menu");
        cardPanel.add(new SlotPanel(this), "slots");
        cardPanel.add(new GamePanel(), "game");

        add(cardPanel);

        setVisible(true);
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }
}

// MenuPanel dengan tombol play yang pindah ke SlotPanel
class MenuPanel extends JPanel {
    private MainPanel mainFrame;

    public MenuPanel(MainPanel mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        URL bgUrl = getClass().getResource("/assets/landingPage.gif");
        if (bgUrl == null) {
            System.out.println("Gambar background tidak ditemukan!");
            setBackground(Color.BLACK); // fallback
        } else {
            JLabel bgLabel = new JLabel(new ImageIcon(bgUrl));
            add(bgLabel);
            bgLabel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 250));
            
            URL playUrl = getClass().getResource("/assets/playButtonLandingPage.png");
            JButton playButton;
            if (playUrl != null) {
                playButton = new JButton(new ImageIcon(playUrl));
            } else {
                System.out.println("Gambar play button tidak ditemukan!");
                playButton = new JButton("Play");
            }

            playButton.setBorderPainted(false);
            playButton.setContentAreaFilled(false);
            playButton.setFocusPainted(false);
            playButton.setOpaque(false);
            playButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            playButton.addActionListener(e -> {
                mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "slots");
            });

            bgLabel.add(playButton);
        }
    }

    // Custom JButton yang hanya merespon klik di area gambar yang tidak transparan
    static class PerPixelButton extends JButton {
        private BufferedImage bufferedImage = null;

        public PerPixelButton(ImageIcon icon) {
            super(icon);
        }

        @Override
        public boolean contains(int x, int y) {
            if (getIcon() == null) {
                return super.contains(x, y);
            }

            if (bufferedImage == null) {
                bufferedImage = toBufferedImage(((ImageIcon) getIcon()).getImage());
            }

            if (x < 0 || y < 0 || x >= bufferedImage.getWidth() || y >= bufferedImage.getHeight()) {
                return false;
            }

            int pixel = bufferedImage.getRGB(x, y);
            int alpha = (pixel >> 24) & 0xff;
            return alpha > 0;
        }

        private BufferedImage toBufferedImage(Image img) {
            if (img instanceof BufferedImage) {
                return (BufferedImage) img;
            }

            BufferedImage bimage = new BufferedImage(
                img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(img, 0, 0, null);
            bGr.dispose();

            return bimage;
        }
    }
}



// SlotPanel menampilkan beberapa SlotCard
class SlotPanel extends JPanel {
    private Image backgroundImage;

    public SlotPanel(MainPanel mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40)); // top, left, bottom, right
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 80));
        rowPanel.setOpaque(false);

        // Load gambar background
        URL bgUrl = getClass().getResource("/assets/chooseSlotBG.png");
        if (bgUrl != null) {
            backgroundImage = new ImageIcon(bgUrl).getImage();
        } else {
            System.out.println("Gambar background slot panel tidak ditemukan!");
        }

        // Data 3 slot
        Object[][] slotData = {
            {1, "Jeje", 1, 10},
            {2, null, 0, 0},   // Empty slot
            {3, "Edo", 1, 10}
        };

        for (Object[] data : slotData) {
            int slotNumber = (int) data[0];
            String playerName = (String) data[1];
            int level = (int) data[2];
            int stars = (int) data[3];

            SlotCard slot = new SlotCard(slotNumber, playerName, level, stars, mainFrame);
            rowPanel.add(slot);
        }


        add(Box.createVerticalGlue()); // dorong ke tengah vertikal (opsional)
        add(rowPanel);
        add(Box.createVerticalGlue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Anti-aliasing biar halus
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Warna background (sesuai selera)
        g2.setColor(Color.BLUE); // warna border
        g2.setStroke(new BasicStroke(2)); // ketebalan
        g2.drawRoundRect(0, 0, getWidth(), getHeight(), 40, 40);


        // Kalau kamu punya background image dan ingin tetap pakai:
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

class SlotCard extends JPanel {
    private final int slotNumber;
    private final String playerName;
    private final int level;
    private final int stars;
    private MainPanel mainFrame;

    public SlotCard(int slotNumber, String playerName, int level, int stars, MainPanel mainFrame) {
        this.slotNumber = slotNumber;
        this.playerName = playerName;
        this.level = level;
        this.stars = stars;
        this.mainFrame = mainFrame;

        setPreferredSize(new Dimension(220, 320));
        setOpaque(false); // penting agar kita bisa gambar background sendiri
        setLayout(null);

        // Komponen UI (judul, nama, level, stars, tombol)
        JLabel numberCircle = new JLabel(String.valueOf(slotNumber), SwingConstants.CENTER);
        numberCircle.setOpaque(true);
        numberCircle.setBackground(Color.WHITE);
        numberCircle.setBounds(90, 10, 40, 40);
        numberCircle.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        numberCircle.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        numberCircle.setForeground(Color.BLACK);
        add(numberCircle);

        JLabel saveLabel = new JLabel("Save Slot", SwingConstants.CENTER);
        saveLabel.setBounds(40, 60, 140, 20);
        saveLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        add(saveLabel);

        JLabel nameLabel = new JLabel(playerName != null ? playerName : "Empty Slot", SwingConstants.CENTER);
        nameLabel.setBounds(40, 90, 140, 30);
        nameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        add(nameLabel);

        JLabel levelLabel = new JLabel(playerName != null ? "LEVEL " + level : "", SwingConstants.CENTER);
        levelLabel.setBounds(40, 130, 140, 25);
        levelLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        add(levelLabel);

        JLabel starsLabel = new JLabel(playerName != null ? stars + " stars" : "", SwingConstants.CENTER);
        starsLabel.setBounds(40, 160, 140, 20);
        starsLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        add(starsLabel);

        JButton playBtn = new JButton("Play \u25B6");
        playBtn.setBounds(60, 230, 100, 40);
        playBtn.setFocusPainted(false);
        playBtn.setBackground(new Color(0, 123, 255));
        playBtn.setForeground(Color.WHITE);
        playBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        playBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playBtn.addActionListener(e -> {
            System.out.println("Play clicked on slot " + slotNumber);
            mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "game");
        });
        add(playBtn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Anti-aliasing biar halus
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Warna background slot
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); // radius 40px

        // Border dashed dengan sudut membulat
        float[] dashPattern = {10, 5}; // panjang garis dan spasi
        Stroke dashed = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0);
        g2.setStroke(dashed);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40); // -1 biar nggak keluar panel
    }
}



// GamePanel sebagai game scene dengan JLayeredPane dan gambar
class GamePanel extends JPanel {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel;
    private String[] customerImages = {
        "/assets/customer1.png",
        "/assets/customer2.png",
        "/assets/customer3.png"
    };

    public GamePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(970, 570));
        setSize(new Dimension(970, 570));

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(970, 570));
        layeredPane.setSize(new Dimension(970, 570));
        layeredPane.setLayout(null);  // **penting**

        add(layeredPane, BorderLayout.CENTER);

        // Background
        URL bgUrl = getClass().getResource("/assets/gameBG.png");
        if (bgUrl != null) {
            backgroundLabel = new JLabel(new ImageIcon(bgUrl));
            backgroundLabel.setBounds(0, 0, 970, 570);
            layeredPane.add(backgroundLabel, Integer.valueOf(0));
        } else {
            System.err.println("Background image not found!");
        }

        // Ovens
        ArrayList<JLabel> ovens = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            String ovenPath = "/assets/Oven" + i + ".png";
            URL ovenUrl = getClass().getResource(ovenPath);
            if (ovenUrl != null) {
                ImageIcon icon = new ImageIcon(ovenUrl);
                JLabel ovenLabel = new JLabel(icon);
                ovenLabel.setBounds(0, 0, 970, 570);  // semua di pojok kiri atas
                ovenLabel.setVisible(false);          // sembunyikan semua dulu
                layeredPane.add(ovenLabel, Integer.valueOf(2));
                ovens.add(ovenLabel);
            }
        }
        // Tampilkan oven pertama
        if (!ovens.isEmpty()) {
            for (int i = 0; i < 6;i++){
                ovens.get(i).setVisible(true);
            }
            
        }

        layeredPane.revalidate();
        layeredPane.repaint();
        
    }
}
