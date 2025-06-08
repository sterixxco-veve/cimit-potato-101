/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import javax.swing.*;
import java.awt.*;


public class MainPanel extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private FinalTrophyPanel finalTrophyPanel;
    private LevelSelectMenu levelSelectMenu;
    private SlotPanel slotPanel;
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
        cardPanel.add(new MenuPanel(this), "menu"); 
        cardPanel.add(new SlotPanel(this), "slots");
        
        // Jangan tambahkan GamePanel di sini jika akan dibuat dinamis
        // cardPanel.add(new GamePanel(), "game"); // HAPUS ATAU KOMENTARI BARIS INI
        this.slotPanel = new SlotPanel(this); 
        cardPanel.add(this.slotPanel, "slots");
        
        add(cardPanel);
        setVisible(true);
    }
    
    public void showTrophyScreen(Player player, SaveSlotData slotData, int gameSlotNumber) {
        if (finalTrophyPanel == null) {
            finalTrophyPanel = new FinalTrophyPanel(this, player, slotData, gameSlotNumber);
            cardPanel.add(finalTrophyPanel, "finalTrophyScreen"); // "finalTrophyScreen" adalah nama untuk CardLayout
        } else {
            // Jika panel sudah ada, update kontennya
            finalTrophyPanel.updateContent(player, slotData, gameSlotNumber);
        }
        cardLayout.show(cardPanel, "finalTrophyScreen");
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }

    // Metode startGame diubah untuk menerima slotNumber
    public void startGame(SaveSlotData slotData, int slotNumber) {
        // 1. Buat atau muat Player berdasarkan slotData
        Player gamePlayer = loadOrCreatePlayer(slotData); 

        // 2. Level yang akan dimainkan (dari slotData)
        int levelToPlay = slotData.getLevel(); 

        // 3. Buat instance GamePanel baru dengan data pemain, level, MainPanel, dan slotNumber
        GamePanel newGamePanel = new GamePanel(gamePlayer, levelToPlay, this, slotNumber, slotData);

        // 4. Ganti panel game yang ada di CardLayout dan tampilkan
        String gamePanelId = "game_instance_" + System.currentTimeMillis(); 
        cardPanel.add(newGamePanel, gamePanelId); 
        cardLayout.show(cardPanel, gamePanelId); 
    }

    // Implementasi loadOrCreatePlayer (sesuaikan dengan kebutuhan save/load Anda)
    public Player loadOrCreatePlayer(SaveSlotData slotData) {
        // Untuk tes Level 1, kita bisa buat Player baru setiap kali
        Player player = new Player(slotData.getPlayerName()); 
        player.setCurrentLevel(slotData.getLevel()); 
        player.setStars(slotData.getStars()); 
        player.setGold(slotData.getGold()); // Set gold awal menjadi 0, atau sesuai logika game Anda
        player.initializeUpgradeLevels(); 
        return player;
    }
    
    public void showLevelSelectMenu(Player player, SaveSlotData slotData, int slotNumber) {
    // Hapus panel level lama jika ada
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof LevelSelectMenu) {
                cardPanel.remove(comp);
                break;
            }
        }
        LevelSelectMenu levelSelectPanel = new LevelSelectMenu(this, player, slotData, slotNumber);
        cardPanel.add(levelSelectPanel, "level");
        cardLayout.show(cardPanel, "level");
        levelSelectPanel.refreshHUD();
    }
    
    public void showMainMenu() {
        cardLayout.show(cardPanel, "menu");
    }
    
    public void showSlotSelectionScreen() {
        // Panggil refresh() terlebih dahulu
        slotPanel.refresh(); 

        // Kemudian tampilkan panelnya
        getCardLayout().show(getCardPanel(), "slots");
    }
}

        