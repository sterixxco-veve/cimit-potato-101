package cimitpotato101;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Aspire
 */

public class SlotCard extends JPanel {
    private final int slotNumber;
    private String playerName;
    private int level;
    private int stars;
    private final MainPanel mainFrame;
    private String trophyType;

    // --- PERUBAHAN 1: Jadikan label sebagai fields (variabel instance) ---
    private JLabel playerNameLabel;
    private JLabel levelLabel;
    private JLabel starsLabel;
    private JLabel trophyInfoLabel;

    public SlotCard(int slotNumber, String playerName, int level, int stars, String trophyType, MainPanel mainFrame) {
        this.slotNumber = slotNumber;
        this.playerName = playerName;
        this.level = level;
        this.stars = stars;
        this.trophyType = trophyType;
        this.mainFrame = mainFrame;

        setPreferredSize(new Dimension(220, 320));
        setOpaque(false);
        setLayout(null);

        addSlotComponents();
    }

    private void addSlotComponents() {
        JLabel numberCircle = createLabel(String.valueOf(slotNumber), 90, 10, 40, 40, 18, true);
        numberCircle.setBackground(Color.WHITE);
        numberCircle.setOpaque(true);
        numberCircle.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        add(numberCircle);

        add(createLabel("Save Slot", 40, 60, 140, 20, 14, false));
        
        // Inisialisasi fields label yang sudah dibuat
        playerNameLabel = createLabel(playerName != null && !playerName.isEmpty() ? playerName : "Empty Slot", 40, 90, 140, 30, 20, true);
        levelLabel = createLabel(playerName != null && !playerName.isEmpty() ? "LEVEL " + level : "", 40, 130, 140, 25, 16, false);
        starsLabel = createLabel(playerName != null && !playerName.isEmpty() ? stars + " stars" : "", 40, 160, 140, 20, 14, false);
        add(playerNameLabel);
        add(levelLabel);
        add(starsLabel);
        
        trophyInfoLabel = createLabel("", 40, 185, 140, 20, 14, false);
        trophyInfoLabel.setForeground(new Color(139, 69, 19));
        updateTrophyLabel();
        add(trophyInfoLabel);

        URL playBtnUrl = getClass().getResource("/assets/playButtonSlot.png");
        JButton playBtn = new JButton();
        if (playBtnUrl != null) {
            playBtn.setIcon(new ImageIcon(playBtnUrl));
        } else {
            playBtn.setText("Play ▶");
        }
        playBtn.setBounds(10, 190, 200, 200);
        playBtn.setContentAreaFilled(false);
        playBtn.setBorderPainted(false);
        playBtn.setFocusPainted(false);
        playBtn.setOpaque(false);
        playBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        playBtn.addActionListener(e -> {
            boolean isEmpty = (this.playerName == null || this.playerName.isEmpty() || this.playerName.equals("Empty Slot"));
            SaveSlotData slotDataToUse;

            if (isEmpty) {
                String inputName = JOptionPane.showInputDialog(this, "Enter your name:");
                if (inputName != null && !inputName.trim().isEmpty()) {
                    slotDataToUse = new SaveSlotData(inputName.trim(), 1, 0);
                    SaveSlotUtils.saveSlotData(this.slotNumber, slotDataToUse);
                    refresh(); // Cukup panggil refresh untuk update tampilan
                } else {
                    return;
                }
            } else {
                slotDataToUse = SaveSlotUtils.loadSlotData(this.slotNumber);
            }

            Player playerObj = mainFrame.loadOrCreatePlayer(slotDataToUse);
            mainFrame.showLevelSelectMenu(playerObj, slotDataToUse, this.slotNumber);
        });

        add(playBtn);
    }

    // --- PERUBAHAN 2: Buat method refresh() publik ---
    /**
     * Memperbarui data visual pada card ini dengan memuat ulang dari file.
     */
    public void refresh() {
        SaveSlotData data = SaveSlotUtils.loadSlotData(this.slotNumber);
        
        this.playerName = data.getPlayerName();
        this.level = data.getLevel();
        this.stars = data.getStars();
        this.trophyType = data.getTrophyType();

        boolean isEmpty = (this.playerName == null || this.playerName.isEmpty());
        playerNameLabel.setText(isEmpty ? "Empty Slot" : this.playerName);
        levelLabel.setText(isEmpty ? "" : "LEVEL " + this.level);
        starsLabel.setText(isEmpty ? "" : this.stars + " stars");
        
        updateTrophyLabel();
        repaint();
    }

    private JLabel createLabel(String text, int x, int y, int w, int h, int fontSize, boolean bold) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setBounds(x, y, w, h);
        label.setFont(new Font("Comic Sans MS", bold ? Font.BOLD : Font.PLAIN, fontSize));
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
        float[] dashPattern = {10, 5};
        Stroke dashed = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0);
        g2.setStroke(dashed);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
    }
    
    private void updateTrophyLabel() {
        if (this.level == 10 && this.trophyType != null && !this.trophyType.isEmpty()) {
            trophyInfoLabel.setText("TROPHY: " + this.trophyType);
        } else {
            trophyInfoLabel.setText("");
        }
    }
}