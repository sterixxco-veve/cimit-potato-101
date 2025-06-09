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

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 220)); // Warna latar sedikit krem

        trophyImageLabel = new JLabel();
        // Jika JLabel lebih besar dari gambar yang ditampilkan, 
        // maka gambar akan diposisikan di tengah secara horizontal / vertical        
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
            // mencari lokasi file             
            URL imageUrl = getClass().getResource(imagePathToLoad);
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                trophyImageLabel.setIcon((originalIcon));
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
            backButtonInstance.setIcon(backIcon);
        } else {
            System.err.println("Back button image not found at " + BACK_BUTTON_IMG_PATH + "!");
            backButtonInstance.setText("Kembali");
            backButtonInstance.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        }
        // Membuat area utama tombol menjadi transparan, sehingga tidak ada latar belakang abu-abu standar.
        backButtonInstance.setContentAreaFilled(false);
        // Menghilangkan garis tepi (border) di sekitar tombol.       
        backButtonInstance.setBorderPainted(false);
        // Menghilangkan garis putus2 saat button diklik       
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
        // membuat panel pembungkus ini transparan, sehingga hanya tombolnya yang terlihat.
        buttonContainerPanel.setOpaque(false);
        // memasukkan tombol ke dalam panel pembungkus tersebut.
        buttonContainerPanel.add(backButtonInstance);

        add(buttonContainerPanel, BorderLayout.SOUTH); // Tambahkan panel tombol ke bagian bawah
    }

    
}
