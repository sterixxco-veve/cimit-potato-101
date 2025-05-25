/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Aspire
 */

public class GamePanel extends JPanel {
    private JLabel[] ovenNameLabels = new JLabel[6];
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel;
    private Oven[] ovenLogic = new Oven[6];
    private JLabel[] ovenLabels = new JLabel[6];
    private final String[] ovenNames = {"Regular", "Curly", "Chips", "Wedges", "Tornado", "Mashed"};
    private int[][] koordinat = {{450,275}, {600,275},{450,375}, {600,375}};
    private JLabel[] piringLabels = new JLabel[4];
    private Potato[] arrKentang = {new EmptyPotato(), new EmptyPotato(), new EmptyPotato(), new EmptyPotato()};
    private int[][] koordinatTopping = {{100,330}, {235,335}, {130,280}, {720,340}, {820, 340}};

    private Timer gameTimer;
    private boolean timerStarted = false;

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
        Level level = new Level(1, 1, 1, new ArrayList<>(), new ArrayList<>() , new BoosterTier("", 0, 0));
        ovenLogic = level.getOvens();

        for (int i = 0; i < 6; i++) {
            String ovenPath = "/assets/oven.png";
            URL ovenUrl = getClass().getResource(ovenPath);
            if (ovenUrl != null) {
                ImageIcon icon = new ImageIcon(ovenUrl);
                JLabel ovenLabel = new JLabel(icon);

                int x = 125;
                int y = 470;

                ovenLabel.setBounds(x + i*125, y, 100, 100); // posisi oven
                layeredPane.add(ovenLabel, Integer.valueOf(2));
                ovens.add(ovenLabel);
                ovenLabels[i] = ovenLabel;

                // ==== Tambahkan nama oven di bawahnya ====
                JLabel nameLabel = new JLabel(ovenLogic[i].getOvenName(), SwingConstants.CENTER);
                nameLabel.setForeground(Color.WHITE); // Ubah warna sesuai background
                nameLabel.setBounds(x + i*125, y , 100, 20); // tepat di bawah oven
                ovenNameLabels[i] = nameLabel;
                layeredPane.add(nameLabel, Integer.valueOf(2));

                // Klik listener
                final int index = i;
                ovenLabel.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        Oven oven = ovenLogic[index];

                        System.out.println("Clicked "+index);

                        if (!oven.isOccupied()) {
                            String potatoName = ovenNames[index] + " Potato";
                            boolean started = oven.startCooking(potatoName); // 5 detik
                            if (started) {
                                String ovenPath = "/assets/ovenNyala.png";
                                URL ovenUrl = getClass().getResource(ovenPath);
                                if (ovenUrl != null) {
                                    ImageIcon icon = new ImageIcon(ovenUrl);
                                    ovens.get(index).setIcon(icon);
                                }

                                JOptionPane.showMessageDialog(null, "Masak dimulai di Oven " + oven.getOvenName() + " Potato");
                            }
                        } else if (oven.isReady()) {
                            for (int i = 0; i < arrKentang.length ; i++){
                                if (arrKentang[i] instanceof EmptyPotato){
                                    if (null != oven.getOvenName())switch (oven.getOvenName()) {
                                        case "Regular":
                                            arrKentang[i] = new RegularPotato();
                                            break;
                                        case "Curly":
                                            arrKentang[i] = new CurlyPotato();
                                            break;
                                        case "Chips":
                                            arrKentang[i] = new ChipsPotato();
                                            break;
                                        case "Wedges" :
                                            arrKentang[i] = new WedgesPotato();
                                            break;
                                        case "Tornado" :
                                            arrKentang[i] = new TornadoPotato();
                                            break;
                                        case "Mashed" :
                                            arrKentang[i] = new MashedPotato();
                                            break;
                                        default:
                                            break;
                                    }
                                    String result = oven.takeOut();
                                    String ovenPath = "/assets/oven.png";
                                    URL ovenUrl = getClass().getResource(ovenPath);
                                    if (ovenUrl != null) {
                                        ImageIcon icon = new ImageIcon(ovenUrl);
                                        ovens.get(index).setIcon(icon);
                                    }
                                    JOptionPane.showMessageDialog(null, oven.getOvenName() + " selesai! Hasil: " + result);

                                    break;
                                }
                            }

                        } else {
                            long remaining = oven.getRemainingTimeMs() / 1000;
                            JOptionPane.showMessageDialog(null, "Masih dimasak (" + remaining + " detik lagi)");
                        }
                    }
                });
            }
        }

        ArrayList<JLabel> piring = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            String piringPath = arrKentang[i].getImagePath();
            URL piringUrl = getClass().getResource(piringPath);
            if (piringUrl != null) {
                ImageIcon icon = new ImageIcon(piringUrl);
                JLabel piringLabel = new JLabel(icon);

                int x = koordinat[i][0];
                int y = koordinat[i][1];

                piringLabel.setBounds(x, y, 100, 100); // posisi oven
                layeredPane.add(piringLabel, Integer.valueOf(2));
                piring.add(piringLabel);
                piringLabels[i] = piringLabel;

                final int index = i;
                piringLabel.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            arrKentang[index] = new EmptyPotato();
                        }
                    }
                });
            }
        }

        Timer ovenTimer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < ovenLogic.length; i++) {
                    ovenLogic[i].updateStatus();
                    if (ovenLogic[i].isReady()) {
                        ovenLabels[i].setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                    } else {
                        ovenLabels[i].setBorder(null);
                    }
                }
                for (int i = 0; i < 4; i++) {
                    String piringPath = arrKentang[i].getImagePath();
                    URL piringUrl = getClass().getResource(piringPath);
                    if (piringUrl != null) {
                        ImageIcon icon = new ImageIcon(piringUrl);
                        piring.get(i).setIcon(icon);
                    }
                }
                layeredPane.revalidate();
                layeredPane.repaint();
            }
        });
        ovenTimer.start();

        displayOven(ovens);
        displayPiring(piring);

        String[] toppingNames = {"bacon", "cheese", "pepperoni", "mayo", "tomato"};

        for (int i = 0; i < 5 ; i++) {
            String toppingPath = "/assets/" + toppingNames[i] + ".png"; // pastikan path benar
            URL toppingUrl = getClass().getResource(toppingPath);
            if (toppingUrl != null) {
                ImageIcon toppingIcon = new ImageIcon(toppingUrl);
                JLabel toppingLabel = new JLabel(toppingIcon);

                int xTopping = koordinatTopping[i][0];
                int yTopping = koordinatTopping[i][1];

                toppingLabel.setBounds(xTopping, yTopping, 100, 100);
                layeredPane.add(toppingLabel, Integer.valueOf(3));

                // MouseListener
                toppingLabel.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        for (int j = 0; j < arrKentang.length; j++) {
                            if (arrKentang[j] instanceof RegularPotato) {
                                ((RegularPotato) arrKentang[j]).addTopping(toppingNames[j]); // asumsi method ini ada
                                String newPath = arrKentang[j].getImagePath();
                                URL newUrl = getClass().getResource(newPath);
                                if (newUrl != null) {
                                    piringLabels[j].setIcon(new ImageIcon(newUrl));
                                }
                                break; // hanya tambahkan ke satu kentang
                            }
                        }
                    }
                });
            }
        }

        layeredPane.revalidate();
        layeredPane.repaint();

        // Load gambar tombol pause
        String pauseImagePath = "/assets/pauseButton.png";
        URL pauseUrl = getClass().getResource(pauseImagePath);
        if (pauseUrl != null) {
            ImageIcon pauseIcon = new ImageIcon(pauseUrl);
            JButton pauseButton = new JButton(pauseIcon);

            pauseButton.setBounds(850, 20, 100, 100); // posisi kiri atas, bisa sesuaikan ukuran gambar
            pauseButton.setContentAreaFilled(false); // tombol transparan
            pauseButton.setBorderPainted(false);     // tanpa border
            pauseButton.setFocusPainted(false);      // tanpa efek fokus

            layeredPane.add(pauseButton, Integer.valueOf(5)); // tambahkan ke lapisan atas

            pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Game dijeda.");
            }
        });
    }
}

    @Override
    public void addNotify() {
        super.addNotify();

        if (!timerStarted) {
            timerStarted = true;

            final int totalSeconds = 3 * 60;
            final int[] timeLeft = {totalSeconds};

            gameTimer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    timeLeft[0]--;
                    System.out.println("Waktu tersisa: " + timeLeft[0] + " detik");

                    if (timeLeft[0] <= 0) {
                        ((Timer)e.getSource()).stop();
                        JOptionPane.showMessageDialog(GamePanel.this, "Waktu habis!");
                    }
                }
            });

            gameTimer.start();
        }
    }

    private void displayOven(ArrayList<JLabel> ovens) {
        if (!ovens.isEmpty()) {
            for (int i = 0; i < ovens.size(); i++){
                ovens.get(i).setVisible(true);
            }

        }
    }

    private void displayPiring(ArrayList<JLabel> piring) {
        if (!piring.isEmpty()) {
                for (int i = 0; i < piring.size(); i++){
                    piring.get(i).setVisible(true);
                }
        }
    }
}
