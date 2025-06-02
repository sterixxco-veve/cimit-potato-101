/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cimitpotato101;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 *
 * @author Gracia Krisnanda
 */
public class EndGamePanel extends JPanel {
    private int starsEarned;
    private int goldEarned;

    public EndGamePanel(int level, int starsEarned, int goldEarned, ActionListener onCollect) {
        this.starsEarned = starsEarned;
        this.goldEarned = goldEarned;
        setLayout(null); // agar bisa custom posisi

        setOpaque(false);

        // Label gold
        JLabel goldLabel = new JLabel("+" + goldEarned);
        goldLabel.setFont(new Font("Arial", Font.BOLD, 24));
        goldLabel.setForeground(Color.ORANGE);
        goldLabel.setBounds(200, 220, 100, 30); // sesuaikan posisi
        add(goldLabel);

        // Tambahkan gambar starsEarned
        for (int i = 0; i < starsEarned; i++) {
            JLabel star = new JLabel(new ImageIcon("assets/star.png")); // pastikan path benar
            star.setBounds(150 + i * 60, 150, 50, 50); // posisi star
            add(star);
        }

        // Tombol Collect
        JButton collectBtn = new JButton(new ImageIcon("assets/collect.png"));
        collectBtn.setBounds(200, 300, 100, 50); // sesuaikan posisi dan ukuran
        collectBtn.setContentAreaFilled(false);
        collectBtn.setBorderPainted(false);
        collectBtn.addActionListener(onCollect);
        add(collectBtn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Gambar background
        g.drawImage(new ImageIcon("assets/winner.png").getImage(), 0, 0, getWidth(), getHeight(), null);
    }
}
