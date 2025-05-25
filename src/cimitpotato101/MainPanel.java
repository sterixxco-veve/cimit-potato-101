/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

// Di MainPanel.java
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList; // Pastikan import ini ada jika menggunakan ArrayList untuk Player

public class MainPanel extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    // Tambahkan field ini jika Anda membutuhkannya untuk menyimpan state antar pemanggilan
    // private Player currentPlayerForGame; 
    // private int currentLevelForGame;   

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
        cardPanel.add(new MenuPanel(this), "menu"); //
        cardPanel.add(new SlotPanel(this), "slots"); //
        // Jangan tambahkan GamePanel di sini jika akan dibuat dinamis
        // cardPanel.add(new GamePanel(), "game"); // HAPUS ATAU KOMENTARI BARIS INI

        add(cardPanel);
        setVisible(true);
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }

    // **** TAMBAHKAN METODE INI ****
    public void startGame(SaveSlotData slotData) {
        // 1. Buat atau muat Player berdasarkan slotData
        Player gamePlayer = loadOrCreatePlayer(slotData); 

        // 2. Level yang akan dimainkan (dari slotData, yang sudah diatur ke 1 oleh SlotCard)
        int levelToPlay = slotData.getLevel(); //

        // 3. Buat instance GamePanel baru dengan data pemain dan level
        // Pastikan GamePanel memiliki constructor (Player, int, MainPanel)
        GamePanel newGamePanel = new GamePanel(gamePlayer, levelToPlay, this);

        // 4. Ganti panel game yang ada di CardLayout dan tampilkan
        // Gunakan nama unik jika Anda membuat instance baru setiap kali agar CardLayout bisa menggantinya
        String gamePanelId = "game_instance_" + System.currentTimeMillis(); // Nama unik
        cardPanel.add(newGamePanel, gamePanelId); 
        cardLayout.show(cardPanel, gamePanelId); 
    }

    // **** TAMBAHKAN METODE INI JUGA ****
    // Implementasi loadOrCreatePlayer (sesuaikan dengan kebutuhan save/load Anda)
    private Player loadOrCreatePlayer(SaveSlotData slotData) {
        // Untuk tes Level 1, kita bisa buat Player baru setiap kali
        Player player = new Player(slotData.getPlayerName()); // Menggunakan constructor Player(String nama)
        player.setCurrentLevel(slotData.getLevel()); // Ini akan selalu 1
        player.setStars(slotData.getStars()); //
        player.setGold(100); // Beri gold awal untuk tes
        player.initializeUpgradeLevels(); // Pastikan map upgrade diinisialisasi

        // (Opsional) Jika Anda punya sistem save/load Player yang lebih lengkap, integrasikan di sini.
        // Contoh:
        // ArrayList<Player> allPlayers = SaveObject.loadListPlayer("players.dat");
        // Player existingPlayer = null;
        // if (allPlayers != null) {
        //     for (Player p : allPlayers) {
        //         if (p.getUsername().equalsIgnoreCase(slotData.getPlayerName())) {
        //             existingPlayer = p;
        //             break;
        //         }
        //     }
        // }
        // if (existingPlayer != null) {
        //     // Update data pemain yang ada
        //     existingPlayer.setCurrentLevel(slotData.getLevel());
        //     existingPlayer.setStars(slotData.getStars());
        //     // ... (update gold jika perlu)
        //     player = existingPlayer;
        // } else {
        //     // Pemain baru, tambahkan ke daftar dan simpan (jika perlu)
        //     if (allPlayers == null) {
        //         allPlayers = new ArrayList<>();
        //     }
        //     allPlayers.add(player);
        //     SaveObject.saveListPlayer(allPlayers, "players.dat");
        // }
        return player;
    }
}