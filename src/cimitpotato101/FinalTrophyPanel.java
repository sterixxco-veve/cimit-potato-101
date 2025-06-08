/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 *
 * @author Aspire
 */
public class FinalTrophyPanel extends JPanel {
    private MainPanel mainFrame;
    private Player currentPlayer;
    private SaveSlotData slotData;
    private int gameSlotNumber;

    private JLabel trophyImageLabel;
    private JButton backButtonInstance;

    private static final String GOLD_TROPHY_PATH = "/assets/Cimit Potato 101 - OOP.jpg";
    private static final String SILVER_TROPHY_PATH = "/assets/Cimit Potato 101 - OOP (1).jpg";
    private static final String BRONZE_TROPHY_PATH = "/assets/Cimit Potato 101 - OOP (2).jpg";
    private static final String BACK_BUTTON_IMG_PATH = "/assets/backButton.png";

    public FinalTrophyPanel(MainPanel mainFrame, Player player, SaveSlotData slotData, int gameSlotNumber) {
        this.mainFrame = mainFrame;
        this.currentPlayer = player;
        this.slotData = slotData;
        this.gameSlotNumber = gameSlotNumber;

        // BorderLayout sudah merupakan pilihan yang tepat untuk ini.
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 220)); // Warna latar sedikit krem

        trophyImageLabel = new JLabel();
        trophyImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        trophyImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(trophyImageLabel, BorderLayout.CENTER);

        setTrophyImageBasedOnStars(player.getStars());
        addCustomBackButton();
    }

    public void updateContent(Player player, SaveSlotData slotData, int gameSlotNumber) {
        this.currentPlayer = player;
        this.slotData = slotData;
        this.gameSlotNumber = gameSlotNumber;
        setTrophyImageBasedOnStars(player.getStars());
    }

    private void setTrophyImageBasedOnStars(int totalStars) {
        String imagePathToLoad = null;
        String trophyName = "Pencapaian!";

        if (totalStars >= 25 && totalStars <= 30) {
            imagePathToLoad = GOLD_TROPHY_PATH;
            trophyName = "Juara Emas!";
        } else if (totalStars >= 15 && totalStars <= 24) {
            imagePathToLoad = SILVER_TROPHY_PATH;
            trophyName = "Juara Perak!";
        } else if (totalStars >= 0 && totalStars <= 14) {
            imagePathToLoad = BRONZE_TROPHY_PATH;
            trophyName = "Juara Perunggu!";
        }

        if (imagePathToLoad != null) {
            URL imageUrl = getClass().getResource(imagePathToLoad);
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(970, 570, Image.SCALE_SMOOTH);
                trophyImageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                trophyImageLabel.setText("Gagal memuat: " + imagePathToLoad);
                System.err.println("Error: Gambar trofi tidak ditemukan di: " + imagePathToLoad);
            }

            // Set gambar agar memenuhi seluruh panel
            trophyImageLabel.setBounds(0, 0, 970, 570);
        } else {
            trophyImageLabel.setIcon(null);
            trophyImageLabel.setText("Selamat! Total Bintang: " + totalStars);
        }
    }

    private void addCustomBackButton() {
        backButtonInstance = new JButton();

        URL backUrl = getClass().getResource(BACK_BUTTON_IMG_PATH);
        if (backUrl != null) {
            ImageIcon backIcon = new ImageIcon(backUrl);
            // Kita tidak perlu mengubah ukuran tombol back, biarkan sesuai ukuran ikon aslinya
            backButtonInstance.setIcon(backIcon);
        } else {
            System.err.println("Back button image not found at " + BACK_BUTTON_IMG_PATH + "!");
            backButtonInstance.setText("Kembali");
            backButtonInstance.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        }

        backButtonInstance.setContentAreaFilled(false);
        backButtonInstance.setBorderPainted(false);
        backButtonInstance.setFocusPainted(false);
        backButtonInstance.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backButtonInstance.addActionListener(e -> {
            if (mainFrame != null) {
                mainFrame.showLevelSelectMenu(currentPlayer, slotData, gameSlotNumber);
            } else {
                System.err.println("Error: Referensi MainFrame null di FinalTrophyPanel.");
            }
        });

        // Panel untuk menampung tombol agar posisinya bagus di bawah
        JPanel buttonContainerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        buttonContainerPanel.setOpaque(false);
        buttonContainerPanel.add(backButtonInstance);

        add(buttonContainerPanel, BorderLayout.SOUTH); // Tambahkan panel tombol ke bagian bawah

        // Jika Anda MUTLAK harus menggunakan setBounds untuk tombol:
        // 1. Panel ini (FinalTrophyPanel) harus `setLayout(null);`
        // 2. Anda harus mengatur `setBounds()` untuk `trophyImageLabel` juga.
        // 3. Tambahkan `backButtonInstance` langsung ke panel ini: `add(backButtonInstance);`
        //    dan atur `backButtonInstance.setBounds(10, 430, backIcon.getIconWidth(), backIcon.getIconHeight());`
        //    (atau ukuran teks jika ikon gagal dimuat).
        //    Contoh jika layout null:
        //    setLayout(null);
        //    trophyImageLabel.setBounds(50, 50, 700, 400); // Sesuaikan x, y, width, height
        //    add(trophyImageLabel);
        //    // (kode untuk setup backButtonInstance)
        //    backButtonInstance.setBounds(10, 430, backIcon != null ? backIcon.getIconWidth() : 150, backIcon != null ? backIcon.getIconHeight() : 50);
        //    add(backButtonInstance);
    }

    
}
