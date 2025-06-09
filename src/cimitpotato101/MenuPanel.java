/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Aspire
 */
// ini landing page
public class MenuPanel extends JPanel {
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
            
            // biar transparan bg
            playButton.setBorderPainted(false);
            playButton.setContentAreaFilled(false);
            playButton.setFocusPainted(false);
            playButton.setOpaque(false);
            playButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // cursor jdi kek onclicked
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
