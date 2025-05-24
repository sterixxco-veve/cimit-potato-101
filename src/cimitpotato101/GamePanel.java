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
    private String[] customerImages = {
        "/assets/customer1.png",
        "/assets/customer2.png",
        "/assets/customer3.png"
    };
    private Oven[] ovenLogic = new Oven[6];
    private JLabel[] ovenLabels = new JLabel[6];
    private final String[] ovenNames = {"Regular", "Curly", "Chips", "Wedges", "Tornado", "Mashed"};
    private int[][] koordinat = {{450,280}, {600,280}, {450,380}, {600,380}};
    private JLabel[] piringLabels = new JLabel[4];
    private Potato[] arrKentang = {new EmptyPotato(), new EmptyPotato(), new EmptyPotato(), new EmptyPotato()};
    
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
                // Bisa tambahkan efek visual di sini, misal ubah ikon oven jika sudah matang
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

        layeredPane.revalidate();
        layeredPane.repaint();
        
    }
    public void displayOven(ArrayList<JLabel> ovens){
        if (!ovens.isEmpty()) {
            for (int i = 0; i < ovens.size(); i++){
                ovens.get(i).setVisible(true);
            }

        }
    }
    
    public void displayPiring(ArrayList<JLabel> piring){
        if (!piring.isEmpty()) {
            for (int i = 0; i < piring.size(); i++){
                piring.get(i).setVisible(true);
            }

        }
    }
}
