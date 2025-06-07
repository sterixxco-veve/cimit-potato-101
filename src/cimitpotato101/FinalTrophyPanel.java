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
    private MainPanel mainFrame; // Referensi ke MainFrame untuk navigasi
    private Player currentPlayer;
    private SaveSlotData slotData;
    private int gameSlotNumber;

    private JLabel trophyImageLabel;
    private JButton backButtonInstance; // Menyimpan instance tombol untuk referensi jika perlu

    // Definisikan path gambar trofi (sesuaikan jika perlu, misal di folder /assets/)
    private static final String GOLD_TROPHY_PATH = "/assets/Cimit Potato 101 - OOP.jpg";
    private static final String SILVER_TROPHY_PATH = "/assets/Cimit Potato 101 - OOP (1).jpg";
    private static final String BRONZE_TROPHY_PATH = "/assets/Cimit Potato 101 - OOP (2).jpg";
    private static final String BACK_BUTTON_IMG_PATH = "/assets/backButton.png"; // Path dari kode Anda


    
    
    public FinalTrophyPanel(MainPanel mainFrame, Player player, SaveSlotData slotData, int gameSlotNumber) {
        this.mainFrame = mainFrame;
        this.currentPlayer = player;
        this.slotData = slotData;
        this.gameSlotNumber = gameSlotNumber;

        // Atur layout untuk FinalTrophyPanel
        // BorderLayout cocok untuk gambar di tengah dan tombol di bawah.
        // Jika Anda ingin posisi absolut seperti setBounds, gunakan setLayout(null);
        // tapi BorderLayout lebih fleksibel.
        setLayout(new BorderLayout());
        setBackground(new Color(220, 220, 220)); // Warna latar belakang contoh

        trophyImageLabel = new JLabel();
        trophyImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        trophyImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(trophyImageLabel, BorderLayout.CENTER);

        // Tampilkan trofi yang sesuai
        setTrophyImageBasedOnStars(player.getStars());

        // Tambahkan tombol back Anda
        addCustomBackButton();
        
        
        
        
        
        
    }

    // Method untuk update konten jika panel ini di-reuse
    public void updateContent(Player player, SaveSlotData slotData, int gameSlotNumber) {
        this.currentPlayer = player;
        this.slotData = slotData;
        this.gameSlotNumber = gameSlotNumber;
        setTrophyImageBasedOnStars(player.getStars());
        // Tombol back sudah ada, action listenernya akan menggunakan field yang terupdate.
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
                ImageIcon icon = new ImageIcon(imageUrl);
                trophyImageLabel.setIcon(icon);
                trophyImageLabel.setText("<html><div style='text-align: center;'>" + trophyName + "<br>Total Bintang: " + totalStars + "</div></html>");
                trophyImageLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
                trophyImageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                trophyImageLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Atur font jika perlu
            } else {
                trophyImageLabel.setIcon(null);
                trophyImageLabel.setText("Gagal memuat gambar trofi: " + imagePathToLoad);
                System.err.println("Error: Gambar trofi tidak ditemukan di: " + imagePathToLoad);
            }
        } else {
            trophyImageLabel.setIcon(null);
            trophyImageLabel.setText("Selamat! Total Bintang: " + totalStars);
        }
    }

    // Menggunakan dan menyesuaikan method addBackButton dari Anda
    private void addCustomBackButton() {
        backButtonInstance = new JButton();

        URL backUrl = getClass().getResource(BACK_BUTTON_IMG_PATH);
        if (backUrl != null) {
            ImageIcon backIcon = new ImageIcon(backUrl);
            backButtonInstance.setIcon(backIcon);
            // Jika menggunakan BorderLayout, setBounds tidak diperlukan di sini.
            // Ukuran tombol akan diatur oleh icon atau preferensi layout.
            // backBtn.setBounds(10, 430, backIcon.getIconWidth(), backIcon.getIconHeight());
        } else {
            System.err.println("Back button image not found at " + BACK_BUTTON_IMG_PATH + "!");
            backButtonInstance.setText("Kembali ke Menu Level");
            // backBtn.setBounds(10, 430, 200, 50); // Sesuaikan jika layout null
            backButtonInstance.setBackground(new Color(255, 240, 200));
            backButtonInstance.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        }

        backButtonInstance.setContentAreaFilled(false);
        backButtonInstance.setBorderPainted(false);
        backButtonInstance.setFocusPainted(false);
        // backButtonInstance.setOpaque(false); // Hati-hati, ini bisa membuat teks tidak terlihat jika tidak ada ikon
        backButtonInstance.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Action: Kembali ke menu pemilihan level melalui MainFrame
        backButtonInstance.addActionListener(e -> {
            if (mainFrame != null) {
                // Pastikan currentPlayer, slotData, gameSlotNumber adalah yang terbaru
                mainFrame.showLevelSelectMenu(currentPlayer, slotData, gameSlotNumber);
            } else {
                System.err.println("Error: Referensi MainFrame null di FinalTrophyPanel.");
            }
        });

        // Panel untuk menampung tombol, agar bisa diposisikan (misal di bawah)
        JPanel buttonContainerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20)); // FlowLayout.CENTER dengan padding
        buttonContainerPanel.setOpaque(false); // Buat panel kontainer transparan
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
