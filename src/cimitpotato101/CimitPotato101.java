/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.Timer;


/**
 *
 * @author Aspire
 */

public class CimitPotato101 extends JFrame {

    private JPanel foregroundPanel;
    private JButton playButton;
    private ImageIcon backgroundImageIcon;
    private BufferedImage playButtonImage;
    private int originalY;
    private Timer animationTimer;
    private boolean gameStarted = false;

    // For slot selection
    private JPanel slotPanel;
    private BufferedImage chooseSlotBackground;

    public CimitPotato101() {
        super("Cimit Potato 101");

        try {
            backgroundImageIcon = new ImageIcon(getClass().getResource("/assets/landingPage.gif"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading background image: " + e.getMessage());
        }

        JLabel backgroundLabel = new JLabel(backgroundImageIcon);
        backgroundLabel.setBounds(0, 0, backgroundImageIcon.getIconWidth(), backgroundImageIcon.getIconHeight());
        getLayeredPane().add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        foregroundPanel = new JPanel();
        foregroundPanel.setLayout(null);
        foregroundPanel.setOpaque(false);
        foregroundPanel.setBounds(0, 0, backgroundImageIcon.getIconWidth(), backgroundImageIcon.getIconHeight());
        getLayeredPane().add(foregroundPanel, JLayeredPane.PALETTE_LAYER);

        // Load play button image
        try {
            InputStream playButtonStream = getClass().getResourceAsStream("/assets/playButtonLandingPage.png");
            if (playButtonStream == null) {
                System.out.println("File not found: /assets/playButtonLandingPage.png");
                playButtonImage = null;
            } else {
                playButtonImage = ImageIO.read(playButtonStream);
                playButtonStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            playButtonImage = null;
        }

        if (playButtonImage != null) {
            playButton = new JButton(new ImageIcon(playButtonImage));
            playButton.setSize(playButtonImage.getWidth(), playButtonImage.getHeight());
        } else {
            playButton = new JButton("Play");
            playButton.setFont(new Font("Arial", Font.BOLD, 24));
        }

        playButton.setOpaque(false);
        playButton.setContentAreaFilled(false);
        playButton.setBorderPainted(false);
        playButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        int x = 300;
        int y = 250;
        playButton.setLocation(x, y);
        originalY = y;
        foregroundPanel.add(playButton);

        // Load "Choose Slot" background image for slot panel
        try {
            InputStream chooseSlotStream = getClass().getResourceAsStream("/assets/chooseSlotBG.png");
            if (chooseSlotStream == null) {
                System.out.println("File not found: /assets/chooseSlotBG.png");
                chooseSlotBackground = null;
            } else {
                chooseSlotBackground = ImageIO.read(chooseSlotStream);
                chooseSlotStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            chooseSlotBackground = null;
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(970, 570);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        animationTimer = new Timer(10, new ActionListener() {
            int currentY = originalY;
            int targetY = originalY - 20;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentY > targetY) {
                    currentY -= 2;
                    playButton.setLocation(playButton.getX(), currentY);
                } else {
                    animationTimer.stop();
                    currentY = originalY;
                    playButton.setLocation(playButton.getX(), originalY);

                    // After animation ends, show slot buttons
                    showSlotButtons();
                }
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!animationTimer.isRunning() && !gameStarted) {
                    animationTimer.start();
                    gameStarted = true;
                }
            }
        });
    }

    private void showSlotButtons() {
        // Remove play button
        foregroundPanel.remove(playButton);

        // Create slot panel with chooseSlotBG background
        slotPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (chooseSlotBackground != null) {
                    g.drawImage(chooseSlotBackground, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(255, 255, 255, 230));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        slotPanel.setLayout(null);
        slotPanel.setBounds(0, 0, backgroundImageIcon.getIconWidth(), backgroundImageIcon.getIconHeight());
        foregroundPanel.add(slotPanel);
        foregroundPanel.revalidate();
        foregroundPanel.repaint();

        // Updated SlotCard inner class
        class SlotCard extends JPanel {
            private int slotNumber;
            private String slotName;
            private int level;
            private int stars;
            private boolean empty;

            public SlotCard(int slotNumber, String slotName, int level, int stars, boolean empty) {
                this.slotNumber = slotNumber;
                this.slotName   = slotName;
                this.level      = level;
                this.stars      = stars;
                this.empty      = empty;

                setOpaque(false);
                setLayout(null);
                setPreferredSize(new Dimension(220, 280));

                // Build file names and paths
                String fileName      = "playButtonPlayer" + slotNumber + ".png";
                String loaderPath    = "assets/" + fileName;     // for ClassLoader
                String classResPath  = "/assets/" + fileName;    // for Class#getResource

                BufferedImage btnImg = null;
                URL imgURL = null;

                // 1) Try ClassLoader lookup
                System.out.println("Slot " + slotNumber + " → trying ClassLoader.getResource(\"" + loaderPath + "\")");
                imgURL = getClass().getClassLoader().getResource(loaderPath);

                // 2) Fallback to Class#getResource if not found
                if (imgURL == null) {
                    System.out.println("Slot " + slotNumber + " → not found. Trying getClass().getResource(\"" + classResPath + "\")");
                    imgURL = getClass().getResource(classResPath);
                }

                if (imgURL == null) {
                    System.err.println("❌ Slot " + slotNumber + " → FAILED to find resource for: " + fileName);
                    // No button if missing
                } else {
                    System.out.println("✅ Slot " + slotNumber + " → found resource at: " + imgURL);
                    try {
                        btnImg = ImageIO.read(imgURL);
                        System.out.println("   → image size: " + btnImg.getWidth() + "×" + btnImg.getHeight());
                    } catch (IOException ex) {
                        System.err.println("❌ Slot " + slotNumber + " → could not read image: " + fileName);
                        ex.printStackTrace();
                    }

                    // Create and position the play button
                    JButton playBtn = new JButton(new ImageIcon(btnImg));
                    playBtn.setSize(btnImg.getWidth(), btnImg.getHeight());
                    playBtn.setOpaque(false);
                    playBtn.setContentAreaFilled(false);
                    playBtn.setBorderPainted(false);
                    playBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    playBtn.setToolTipText(fileName);
                    playBtn.addActionListener(e ->
                        System.out.println("Play clicked on slot " + slotNumber)
                    );

                    // center horizontally, 20px above bottom
                    int px = (220 - btnImg.getWidth()) / 2;
                    int py = 280 - btnImg.getHeight() - 20;
                    playBtn.setBounds(px, py, btnImg.getWidth(), btnImg.getHeight());

                    // add and ensure it's on top
                    add(playBtn);
                    repaint();
                    revalidate();

                    setComponentZOrder(playBtn, getComponentCount() - 1); // biar paling atas

                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw card background
                int arc = 30;
                g2.setColor(new Color(255, 255, 255, 230));
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, arc, arc);

                // Dashed border
                float[] dash = {10f, 6f};
                Stroke old = g2.getStroke();
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));
                g2.setColor(new Color(0, 0, 0, 150));
                g2.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, arc, arc);
                g2.setStroke(old);

                // Slot number circle
                int d = 50, cx = (getWidth() - d)/2, cy = 10;
                g2.setColor(new Color(244, 180, 26));
                g2.fillOval(cx, cy, d, d);
                g2.setFont(new Font("Arial", Font.BOLD, 22));
                g2.setColor(Color.BLACK);
                String num = String.valueOf(slotNumber);
                int sw = g2.getFontMetrics().stringWidth(num);
                int sh = g2.getFontMetrics().getAscent();
                g2.drawString(num, cx + (d-sw)/2, cy + d/2 + sh/3);

                // Texts
                g2.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
                int ty = cy + d + 30;
                g2.drawString("Save Slot", (getWidth() - g2.getFontMetrics().stringWidth("Save Slot"))/2, ty);
                ty += 30;
                g2.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
                String nameText = empty ? "Empty Slot" : slotName;
                g2.drawString(nameText, (getWidth() - g2.getFontMetrics().stringWidth(nameText))/2, ty);
                ty += 35;
                g2.setFont(new Font("Arial", Font.BOLD, 26));
                String lvl = "LEVEL " + level;
                g2.drawString(lvl, (getWidth() - g2.getFontMetrics().stringWidth(lvl))/2, ty);
                ty += 35;
                g2.setFont(new Font("Arial", Font.PLAIN, 18));
                String starsText = stars + " stars";
                g2.drawString(starsText, (getWidth() - g2.getFontMetrics().stringWidth(starsText))/2, ty);

                g2.dispose();
            }
        }

        // Example data and layout
        SlotCard[] slots = new SlotCard[3];
        slots[0] = new SlotCard(1, "Jeje", 1, 10, false);
        slots[1] = new SlotCard(2, "",   0,  0, true);
        slots[2] = new SlotCard(3, "Edo", 1, 10, false);

        int slotW = 220, slotH = 280, spacing = 25;
        int startX = (backgroundImageIcon.getIconWidth() - (slotW*3 + spacing*2)) / 2;
        int yPos   = 150;

        for (int i = 0; i < slots.length; i++) {
            SlotCard card = slots[i];
            card.setBounds(startX + i*(slotW + spacing), yPos, slotW, slotH);
            slotPanel.add(card);
        }

        slotPanel.revalidate();
        slotPanel.repaint();
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CimitPotato101().setVisible(true));
    }
}
