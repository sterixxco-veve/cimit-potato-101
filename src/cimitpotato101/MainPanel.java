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
            // Jika panel sudah ada, update content di slotcard
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

    // Metode startGame diubah untuk menerima slot nomer brp
    public void startGame(SaveSlotData slotData, int slotNumber) {
        // 1. bikin atau load Player berdasarkan slotData
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
        player.setGold(slotData.getGold()); 
        player.initializeUpgradeLevels(); 
        return player;
    }
    
    public void showLevelSelectMenu(Player player, SaveSlotData slotData, int slotNumber) {
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof LevelSelectMenu) {
                cardPanel.remove(comp);
                break;
            }
        }
        LevelSelectMenu levelSelectPanel = new LevelSelectMenu(this, player, slotData, slotNumber); //Panel ini dibuat dengan data pemain dan slot saat ini, sehingga bisa nampiliin informasi yang benar (misalnya, level mana saja yang sudah terbuka).
        cardPanel.add(levelSelectPanel, "level");
        cardLayout.show(cardPanel, "level");
        levelSelectPanel.refreshHUD(); //memperbarui tampilan informasi di layar, seperti jumlah emas atau bintang pemain
    }
    
    // buat nampilin panel landing page
    public void showMainMenu() {
        cardLayout.show(cardPanel, "menu");
    }
    
    // buat nampilin panel choose slot
    public void showSlotSelectionScreen() {
        // Panggil refresh() biar dee ke refresh content slotcard e
        slotPanel.refresh(); 

        // Kemudian tampilkan panelnya
        getCardLayout().show(getCardPanel(), "slots");
    }
}

        