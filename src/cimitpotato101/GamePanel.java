/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font; // Import Font
import java.awt.Graphics; // Import Graphics untuk speech bubble
import java.awt.Graphics2D; // Import Graphics2D untuk rounded rectangle
import java.awt.Image; // Import Image untuk scaling
import java.awt.RenderingHints; // Import RenderingHints
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays; // Import Arrays
import java.util.LinkedList; // Menggunakan LinkedList untuk antrian customer
import java.util.List;
import java.util.Queue; // Import Queue
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
    // Variabel-variabel yang sudah ada
    private JLabel[] ovenNameLabels = new JLabel[6];
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel;
    private Oven[] ovenLogic = new Oven[6];
    private JLabel[] ovenLabels = new JLabel[6];
    private int[][] koordinat = {{450,275}, {600,275},{450,375}, {600,375}}; // Koordinat Piring
    private JLabel[] piringLabels = new JLabel[4];
    private Potato[] arrKentang = {new EmptyPotato(), new EmptyPotato(), new EmptyPotato(), new EmptyPotato()};
    private int[][] koordinatToppingSources = {{100,330}, {235,335}, {130,280}, {720,340}, {820, 340}};


    private Timer gameTimer; 
    private Timer updateTimer; 
    private boolean timerStarted = false;

    private Player currentPlayer;
    private int currentLevelNumber;
    private Level activeLevel;
    private MainPanel mainFrame;

    // Variabel untuk Customer Display
    private Queue<Customer> customerQueue; 
    private Customer[] activeCustomersInSlots; 
    private JPanel[] customerSlotPanels; 
    private JLabel[] customerImageLabels; 
    private JLabel[] customerOrderLabels; 

    // Variabel untuk Info UI Atas
    private JLabel customersRemainingLabel;
    private JLabel goldLabel;
    private int totalCustomersForLevel;
    private int customersServedCount;


    // Koordinat dan ukuran untuk elemen customer (BERJAJAR KE KANAN)
    private final int CUSTOMER_START_X = 150; 
    private final int CUSTOMER_START_Y = 140;  
    private final int CUSTOMER_SLOT_WIDTH = 220; 
    private final int CUSTOMER_SLOT_HEIGHT = 95; 
    private final int CUSTOMER_SLOT_SPACING_X = 20; 
    
    private final int CUSTOMER_IMAGE_X = 5; 
    private final int CUSTOMER_IMAGE_Y = 5; 
    private final int CUSTOMER_IMAGE_SIZE = 70; // Target ukuran bounding box untuk gambar customer
    
    private final int ORDER_BUBBLE_X_OFFSET = CUSTOMER_IMAGE_X + CUSTOMER_IMAGE_SIZE + 5; 
    private final int ORDER_BUBBLE_Y = 5; 
    private final int ORDER_BUBBLE_WIDTH = CUSTOMER_SLOT_WIDTH - ORDER_BUBBLE_X_OFFSET - 5; 
    private final int ORDER_BUBBLE_HEIGHT = CUSTOMER_SLOT_HEIGHT - 10; 

    private final int MAX_VISIBLE_CUSTOMERS = 3; 


    public GamePanel(Player player, int levelNumber, MainPanel mainFrameRef) {
        this.currentPlayer = player;
        this.currentLevelNumber = levelNumber;
        this.mainFrame = mainFrameRef;
        this.customersServedCount = 0; 

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(970, 570));

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(970, 570));
        layeredPane.setLayout(null);

        add(layeredPane, BorderLayout.CENTER);

        URL bgUrl = getClass().getResource("/assets/gameBG.png");
        if (bgUrl != null) {
            backgroundLabel = new JLabel(new ImageIcon(bgUrl));
            backgroundLabel.setBounds(0, 0, 970, 570);
            layeredPane.add(backgroundLabel, Integer.valueOf(0));
        } else {
            System.err.println("Background image not found: /assets/gameBG.png");
            setBackground(Color.LIGHT_GRAY);
            JLabel errorLabel = new JLabel("Background Image Not Found", SwingConstants.CENTER);
            errorLabel.setBounds(0,0,970,570);
            layeredPane.add(errorLabel, Integer.valueOf(0));
        }

        if (this.currentPlayer.getUpgradeLevels() == null) {
             this.currentPlayer.initializeUpgradeLevels();
        }
        List<MenuItem> unlockedItems = MenuUnlock.getUnlockedItems(this.currentLevelNumber, this.currentPlayer);
        List<Customer> generatedCustomers = CustomerFactory.generateCustomersForLevel(this.currentLevelNumber, unlockedItems);
        this.customerQueue = new LinkedList<>(generatedCustomers); 
        this.totalCustomersForLevel = generatedCustomers.size(); 
        System.out.println("Generated " + generatedCustomers.size() + " customers for level " + this.currentLevelNumber + ". Queue size: " + customerQueue.size());

        BoosterTier booster = BoosterManager.getBoosterForLevel(this.currentLevelNumber);
        int goal = CustomerOrderManager.getGoalForLevel(this.currentLevelNumber); 
        int duration = CustomerOrderManager.getDurationForLevel(this.currentLevelNumber);
        System.out.println("Level " + this.currentLevelNumber + " - Target Gold: " + goal + ", Duration: " + duration + "s");

        this.activeLevel = new Level(this.currentLevelNumber, goal, duration, unlockedItems, new ArrayList<>(customerQueue), booster);
        this.ovenLogic = this.activeLevel.getOvens();

        setupTopInfoUI(); 
        setupOvenUI();
        setupPiringUI();
        setupToppingSauceSourcesUI(); 
        setupPauseButton();
        setupCustomerDisplaySlots(); 
        fillCustomerSlotsFromQueue(); // Panggil sekali di awal untuk mengisi slot jika ada customer

        updateTimer = new Timer(1000, new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                updateOvenStatusVisuals();
                updatePiringVisuals();
                updateActiveCustomerPatience();
                fillCustomerSlotsFromQueue(); // Coba isi slot kosong secara bertahap
                updateTopInfoUILabels(); 
            }
        });
        updateTimer.start();

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void setupTopInfoUI() {
        JPanel topInfoPanel = new JPanel(null); 
        int panelWidth = 250; // Lebar panel info disesuaikan agar lebih pas dengan isi
        int panelHeight = 50; // Tinggi panel info disesuaikan
        // Posisi tengah horizontal dan Y dinaikkan (lebih ke atas)
        topInfoPanel.setBounds((getWidth() > 0 ? (getWidth() - panelWidth) / 2 : (970 - panelWidth) / 2), 25, panelWidth, panelHeight); 
        topInfoPanel.setOpaque(false); // Panel utama transparan
        
        URL boardUrl = getClass().getResource("/assets/info_board.png"); 
        JLabel boardBgLabel = null; // Deklarasi di luar if
        if (boardUrl != null) {
            ImageIcon boardIcon = new ImageIcon(boardUrl);
            Image scaledBoardImage = boardIcon.getImage().getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
            boardBgLabel = new JLabel(new ImageIcon(scaledBoardImage));
            boardBgLabel.setBounds(0, 0, panelWidth, panelHeight); 
            topInfoPanel.add(boardBgLabel); 
        } else {
            System.err.println("Info board image not found: /assets/info_board.png. Using fallback (no border, transparent).");
            // Jika tidak ada gambar papan, panel utama tetap transparan dan tidak ada border fallback
             topInfoPanel.setBackground(new Color(0,0,0,0)); // Sepenuhnya transparan
        }

        // Panel konten untuk ikon dan teks, agar selalu di atas gambar papan jika ada
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBounds(0, 0, panelWidth, panelHeight);
        contentPanel.setOpaque(false); 

        int iconSize = 24; // Ukuran ikon sedikit lebih kecil
        int textOffsetY = (panelHeight - iconSize) / 2; // Untuk vertical centering

        JLabel customerIconLabel = new JLabel();
        URL customerIconUrl = getClass().getResource("/assets/customer_icon.png"); 
        if(customerIconUrl != null) {
            ImageIcon custOrigIcon = new ImageIcon(customerIconUrl);
            Image scaledCustIcon = custOrigIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            customerIconLabel.setIcon(new ImageIcon(scaledCustIcon));
        } else {
            // Tidak ada teks fallback jika ikon tidak ada, hanya angka
            System.err.println("Customer icon not found for top info UI.");
        }
        customerIconLabel.setBounds(10, textOffsetY, iconSize, iconSize); 
        contentPanel.add(customerIconLabel);

        customersRemainingLabel = new JLabel("0/0", SwingConstants.LEFT);
        customersRemainingLabel.setFont(new Font("Arial", Font.BOLD, 16)); 
        customersRemainingLabel.setForeground(Color.DARK_GRAY); 
        customersRemainingLabel.setBounds(10 + iconSize + 5, textOffsetY, 70, iconSize); // Disesuaikan
        contentPanel.add(customersRemainingLabel);

        JLabel goldIconLabel = new JLabel();
        URL goldIconUrl = getClass().getResource("/assets/gold_coin_icon.png"); 
        if(goldIconUrl != null) {
            ImageIcon goldOrigIcon = new ImageIcon(goldIconUrl);
            Image scaledGoldIcon = goldOrigIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            goldIconLabel.setIcon(new ImageIcon(scaledGoldIcon));
        } else {
            System.err.println("Gold icon not found for top info UI.");
        }
        // Posisikan gold di sebelah kanan customer info
        goldIconLabel.setBounds(10 + iconSize + 5 + 70 + 10, textOffsetY, iconSize, iconSize); 
        contentPanel.add(goldIconLabel);

        goldLabel = new JLabel("0", SwingConstants.LEFT);
        goldLabel.setFont(new Font("Arial", Font.BOLD, 16)); 
        goldLabel.setForeground(Color.DARK_GRAY); 
        goldLabel.setBounds(10 + iconSize + 5 + 70 + 10 + iconSize + 5, textOffsetY, 80, iconSize); // Disesuaikan
        contentPanel.add(goldLabel);
        
        // Tambahkan contentPanel ke topInfoPanel. Jika ada boardBgLabel, contentPanel akan di atasnya.
        if (boardBgLabel != null) {
             topInfoPanel.add(contentPanel, 0); // Tambahkan contentPanel di layer 0 dari topInfoPanel (di atas boardBgLabel)
        } else {
            topInfoPanel.add(contentPanel); // Jika tidak ada board, langsung tambahkan contentPanel
        }
        
        layeredPane.add(topInfoPanel, Integer.valueOf(JLayeredPane.PALETTE_LAYER + 10)); // Layer tinggi agar di atas semua
        updateTopInfoUILabels(); 
    }

    private void updateTopInfoUILabels() {
        if (customersRemainingLabel != null) {
            customersRemainingLabel.setText((totalCustomersForLevel - customersServedCount) + "/" + totalCustomersForLevel);
        }
        if (goldLabel != null && currentPlayer != null) {
            goldLabel.setText(String.valueOf(currentPlayer.getGold()));
        }
    }


    private void setupOvenUI() {
        for (int i = 0; i < 6; i++) {
            String ovenImagePath = "/assets/oven.png";
            URL ovenImageUrl = getClass().getResource(ovenImagePath);
            if (ovenImageUrl != null) {
                ImageIcon icon = new ImageIcon(ovenImageUrl);
                ovenLabels[i] = new JLabel(icon);
                int x = 125;
                int y = 470; 
                ovenLabels[i].setBounds(x + i * 125, y, 100, 100);
                layeredPane.add(ovenLabels[i], Integer.valueOf(2));

                String currentOvenName = (this.ovenLogic != null && i < this.ovenLogic.length && this.ovenLogic[i] != null)
                                        ? this.ovenLogic[i].getOvenName()
                                        : "Oven " + (i + 1);
                ovenNameLabels[i] = new JLabel(currentOvenName, SwingConstants.CENTER);
                ovenNameLabels[i].setForeground(Color.WHITE);
                ovenNameLabels[i].setFont(new Font("Arial", Font.BOLD, 12)); 
                ovenNameLabels[i].setBounds(x + i * 125, y - 20, 100, 20); 
                layeredPane.add(ovenNameLabels[i], Integer.valueOf(3)); 

                final int ovenIndex = i;
                ovenLabels[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleOvenClick(ovenIndex);
                    }
                });
            } else {
                 System.err.println("Oven image not found: " + ovenImagePath);
            }
        }
    }

    private void handleOvenClick(int ovenIndex) {
        if (GamePanel.this.ovenLogic == null || ovenIndex >= GamePanel.this.ovenLogic.length || GamePanel.this.ovenLogic[ovenIndex] == null) {
            System.err.println("Error: ovenLogic not initialized or index out of bounds for oven " + ovenIndex);
            return;
        }
        Oven selectedOven = GamePanel.this.ovenLogic[ovenIndex];

        if (!selectedOven.isOccupied()) {
            String potatoNameToCook = selectedOven.getOvenName(); 
            boolean started = selectedOven.startCooking(potatoNameToCook);
            if (started) {
                URL ovenNyalaUrl = getClass().getResource("/assets/ovenNyala.png");
                if (ovenNyalaUrl != null && ovenLabels[ovenIndex] != null) {
                    ovenLabels[ovenIndex].setIcon(new ImageIcon(ovenNyalaUrl));
                }
            }
        } else if (selectedOven.isReady()) {
            for (int piringIdx = 0; piringIdx < arrKentang.length; piringIdx++) {
                if (arrKentang[piringIdx] instanceof EmptyPotato) {
                    String cookedPotatoType = selectedOven.takeOut(); 
                    if (cookedPotatoType != null) {
                        Potato newPotato = createPotatoInstance(cookedPotatoType); 
                        arrKentang[piringIdx] = (newPotato != null) ? newPotato : new EmptyPotato();
                        if (newPotato == null) System.err.println("Unknown potato type from oven: " + cookedPotatoType);
                        
                        URL ovenDefaultUrl = getClass().getResource("/assets/oven.png");
                        if (ovenDefaultUrl != null && ovenLabels[ovenIndex] != null) {
                            ovenLabels[ovenIndex].setIcon(new ImageIcon(ovenDefaultUrl));
                        }
                        updatePiringVisuals(); 
                    }
                    break; 
                }
            }
        } else {
            long remaining = selectedOven.getRemainingTimeMs() / 1000;
            JOptionPane.showMessageDialog(GamePanel.this, "Masih dimasak (" + remaining + " detik lagi)");
        }
    }

    private void setupPiringUI() {
        for (int i = 0; i < 4; i++) {
            String piringImagePath = arrKentang[i].getImagePath();
            URL piringImageUrl = getClass().getResource(piringImagePath);
            if (piringImageUrl != null) {
                ImageIcon icon = new ImageIcon(piringImageUrl);
                piringLabels[i] = new JLabel(icon);
                piringLabels[i].setBounds(koordinat[i][0], koordinat[i][1], 100, 100);
                layeredPane.add(piringLabels[i], Integer.valueOf(2));

                final int piringIndex = i;
                piringLabels[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) { 
                            arrKentang[piringIndex] = new EmptyPotato();
                            updatePiringVisuals(); 
                        } else if (SwingUtilities.isLeftMouseButton(e)) { 
                            tryServePotatoToCustomer(piringIndex);
                        }
                    }
                });
            } else {
                System.err.println("Piring image not found: " + piringImagePath);
            }
        }
    }

    private void tryServePotatoToCustomer(int piringIndex) {
        if (arrKentang[piringIndex] instanceof EmptyPotato) {
            return;
        }

        Potato servedPotato = arrKentang[piringIndex];
        for (int i = 0; i < MAX_VISIBLE_CUSTOMERS; i++) {
            if (activeCustomersInSlots[i] != null) {
                Customer targetCustomer = activeCustomersInSlots[i];
                boolean isMatch = checkOrderMatch(servedPotato, targetCustomer);

                if (isMatch) {
                    JOptionPane.showMessageDialog(this, "Pesanan untuk Customer ("+ targetCustomer.getCustomerImageID() +") cocok!");
                    currentPlayer.addGold(10); 
                    customersServedCount++; 
                    System.out.println("Player gold: " + currentPlayer.getGold() + ", Customers Served: " + customersServedCount);
                    
                    activeCustomersInSlots[i] = null; 
                    updateCustomerSlotVisual(i); 
                    arrKentang[piringIndex] = new EmptyPotato(); 
                    updatePiringVisuals();
                    // fillCustomerSlotsFromQueue(); // Tidak langsung isi, biarkan updateTimer yang mengisi bertahap
                    updateTopInfoUILabels(); 
                    return; 
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Tidak ada customer yang cocok dengan kentang ini.");
    }

    private String normalizeItemName(String name) { 
        if (name == null) return "";
        return name.toLowerCase().replace(" potato", "").trim();
    }

    private boolean checkOrderMatch(Potato servedPotato, Customer customer) {
        if (customer == null || servedPotato == null || customer.getPotato() == null) return false;

        String servedPotatoCleanName = normalizeItemName(servedPotato.getNama());
        String customerExpectedPotatoCleanName = normalizeItemName(customer.getPotato().getNama());

        if (!servedPotatoCleanName.equalsIgnoreCase(customerExpectedPotatoCleanName)) {
            System.out.println("Match fail: Potato type. Served: " + servedPotato.getNama() + " (Clean: " + servedPotatoCleanName + 
                               "), Expected: " + customer.getPotato().getNama() + " (Clean: " + customerExpectedPotatoCleanName + ")");
            return false;
        }

        List<String> itemsOnPlateNormalized = new ArrayList<>();
        if (servedPotato instanceof RegularPotato) {
            for(String item : ((RegularPotato) servedPotato).getToppings()){
                itemsOnPlateNormalized.add(normalizeItemName(item));
            }
        }

        String expectedToppingNameNormalized = (customer.getTopping() != null) ? normalizeItemName(customer.getTopping().getNama()) : null;
        String expectedSauceNameNormalized = (customer.getSauce() != null) ? normalizeItemName(customer.getSauce().getNama()) : null;

        int expectedAdditionalItemsCount = 0;
        if (expectedToppingNameNormalized != null) expectedAdditionalItemsCount++;
        if (expectedSauceNameNormalized != null) expectedAdditionalItemsCount++;
        
        boolean toppingMatch = true;
        if (expectedToppingNameNormalized != null) {
            toppingMatch = itemsOnPlateNormalized.stream().anyMatch(item -> item.equalsIgnoreCase(expectedToppingNameNormalized));
            if (!toppingMatch) {
                System.out.println("Match fail: Expected topping '" + expectedToppingNameNormalized + "' not found on plate. Plate has: " + itemsOnPlateNormalized);
                return false;
            }
        }

        boolean sauceMatch = true;
        if (expectedSauceNameNormalized != null) {
            sauceMatch = itemsOnPlateNormalized.stream().anyMatch(item -> item.equalsIgnoreCase(expectedSauceNameNormalized));
            if (!sauceMatch) {
                System.out.println("Match fail: Expected sauce '" + expectedSauceNameNormalized + "' not found on plate. Plate has: " + itemsOnPlateNormalized);
                return false;
            }
        }
        
        if (itemsOnPlateNormalized.size() != expectedAdditionalItemsCount) {
            System.out.println("Match fail: Item count mismatch. Expected " + expectedAdditionalItemsCount + " additional items. Plate has " + itemsOnPlateNormalized.size() + " items: " + itemsOnPlateNormalized);
            System.out.println("Expected Topping: " + expectedToppingNameNormalized + ", Expected Sauce: " + expectedSauceNameNormalized);
            return false;
        }
        
        System.out.println("Order MATCH! Served: " + servedPotato.getNama() + " with " + itemsOnPlateNormalized +
                           ". Customer wanted: " + customer.getPotato().getNama() +
                           (expectedToppingNameNormalized != null ? " + " + expectedToppingNameNormalized : "") +
                           (expectedSauceNameNormalized != null ? " + " + expectedSauceNameNormalized : ""));
        return true; 
    }

    private void setupToppingSauceSourcesUI() {
        String[] itemAssetNames = {"bacon", "cheese", "pepperoni", "mayo", "tomato"}; 
        for (int i = 0; i < itemAssetNames.length ; i++) {
            String itemPath = "/assets/" + itemAssetNames[i] + ".png"; 
            URL itemUrl = getClass().getResource(itemPath);
            if (itemUrl != null) {
                ImageIcon itemIcon = new ImageIcon(itemUrl);
                JLabel itemLabel = new JLabel(itemIcon);
                itemLabel.setBounds(koordinatToppingSources[i][0], koordinatToppingSources[i][1], 80, 80); 
                layeredPane.add(itemLabel, Integer.valueOf(3));

                final String itemNameForLogic = itemAssetNames[i]; 
                itemLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        for (int j = 0; j < arrKentang.length; j++) {
                            if (arrKentang[j] instanceof RegularPotato) {
                                RegularPotato rp = (RegularPotato) arrKentang[j];
                                rp.addTopping(itemNameForLogic); 
                                System.out.println("Added " + itemNameForLogic + " to Regular Potato on piring " + j + ". Current toppings: " + rp.getToppings());
                                updatePiringVisuals(); 
                                break; 
                            }
                        }
                    }
                });
            } else {
                System.err.println("Item (Topping/Sauce) image not found: " + itemPath);
            }
        }
    }
    
    private void setupPauseButton() {
        String pauseImagePath = "/assets/pauseButton.png";
        URL pauseUrl = getClass().getResource(pauseImagePath);
        if (pauseUrl != null) {
            ImageIcon pauseIcon = new ImageIcon(pauseUrl);
            JButton pauseButton = new JButton(pauseIcon);
            pauseButton.setBounds(850, 20, pauseIcon.getIconWidth(), pauseIcon.getIconHeight());
            pauseButton.setContentAreaFilled(false);
            pauseButton.setBorderPainted(false);
            pauseButton.setFocusPainted(false);
            layeredPane.add(pauseButton, Integer.valueOf(JLayeredPane.MODAL_LAYER + 1)); // Layer tinggi

            pauseButton.addActionListener(e -> {
                if (gameTimer != null && gameTimer.isRunning()) {
                    gameTimer.stop();
                    if (updateTimer != null) updateTimer.stop();
                    JOptionPane.showMessageDialog(GamePanel.this, "Game dijeda. Klik OK untuk melanjutkan.");
                    gameTimer.start();
                    if (updateTimer != null) updateTimer.start();
                } else {
                     JOptionPane.showMessageDialog(GamePanel.this, "Game sudah dijeda atau belum mulai.");
                }
            });
        } else {
            System.err.println("Pause button image not found: " + pauseImagePath);
        }
    }

    private void setupCustomerDisplaySlots() {
        activeCustomersInSlots = new Customer[MAX_VISIBLE_CUSTOMERS];
        customerSlotPanels = new JPanel[MAX_VISIBLE_CUSTOMERS];
        customerImageLabels = new JLabel[MAX_VISIBLE_CUSTOMERS];
        customerOrderLabels = new JLabel[MAX_VISIBLE_CUSTOMERS];

        for (int i = 0; i < MAX_VISIBLE_CUSTOMERS; i++) {
            customerSlotPanels[i] = new JPanel(null); 
            customerSlotPanels[i].setBounds(
                CUSTOMER_START_X + i * (CUSTOMER_SLOT_WIDTH + CUSTOMER_SLOT_SPACING_X), 
                CUSTOMER_START_Y, 
                CUSTOMER_SLOT_WIDTH, 
                CUSTOMER_SLOT_HEIGHT
            );
            customerSlotPanels[i].setOpaque(false); 

            customerImageLabels[i] = new JLabel();
            customerImageLabels[i].setBounds(CUSTOMER_IMAGE_X, CUSTOMER_IMAGE_Y, CUSTOMER_IMAGE_SIZE, CUSTOMER_IMAGE_SIZE);
            customerImageLabels[i].setHorizontalAlignment(SwingConstants.CENTER); 
            customerImageLabels[i].setVerticalAlignment(SwingConstants.CENTER);
            customerSlotPanels[i].add(customerImageLabels[i]);

            customerOrderLabels[i] = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(255, 250, 225, 230)); 
                    g2d.fillRoundRect(0, 0, getWidth() -1 , getHeight() -1 , 20, 20); 
                    g2d.setColor(new Color(100, 100, 100)); 
                    g2d.drawRoundRect(0, 0, getWidth() -1, getHeight() -1, 20, 20);
                    g2d.dispose();
                    super.paintComponent(g); 
                }
            };
            customerOrderLabels[i].setBounds(ORDER_BUBBLE_X_OFFSET, ORDER_BUBBLE_Y, ORDER_BUBBLE_WIDTH, ORDER_BUBBLE_HEIGHT);
            customerOrderLabels[i].setFont(new Font("Arial", Font.PLAIN, 10)); 
            customerOrderLabels[i].setVerticalAlignment(SwingConstants.TOP);
            customerOrderLabels[i].setForeground(Color.BLACK);
            customerSlotPanels[i].add(customerOrderLabels[i]);
            
            layeredPane.add(customerSlotPanels[i], Integer.valueOf(JLayeredPane.PALETTE_LAYER + 5)); // Layer di atas info panel
            updateCustomerSlotVisual(i); 
        }
    }
    
    private void fillCustomerSlotsFromQueue() {
        for (int i = 0; i < MAX_VISIBLE_CUSTOMERS; i++) {
            if (activeCustomersInSlots[i] == null) { 
                if (!customerQueue.isEmpty()) {
                    activeCustomersInSlots[i] = customerQueue.poll(); 
                    System.out.println("Customer " + activeCustomersInSlots[i].getCustomerImageID() + " masuk ke slot " + i);
                    updateCustomerSlotVisual(i);
                    return; // Hanya isi satu slot per panggilan untuk kedatangan bertahap
                } else {
                    // Tidak ada customer lagi di antrian, pastikan slot visual kosong
                    updateCustomerSlotVisual(i); 
                }
            }
        }
    }

    private void updateCustomerSlotVisual(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= MAX_VISIBLE_CUSTOMERS) return;

        Customer customer = activeCustomersInSlots[slotIndex];
        JLabel imageLabel = customerImageLabels[slotIndex];
        JLabel orderLabel = customerOrderLabels[slotIndex];
        JPanel slotPanel = customerSlotPanels[slotIndex];

        if (customer != null && imageLabel != null && orderLabel != null && slotPanel != null) {
            String imagePath = "/assets/" + customer.getCustomerImageID() + ".png";
            URL imgUrl = getClass().getResource(imagePath);
            if (imgUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imgUrl);
                Image img = originalIcon.getImage();
                
                int originalWidth = originalIcon.getIconWidth();
                int originalHeight = originalIcon.getIconHeight();
                int newWidth = originalWidth;
                int newHeight = originalHeight;

                if (originalWidth > CUSTOMER_IMAGE_SIZE || originalHeight > CUSTOMER_IMAGE_SIZE) {
                    float aspectRatio = (float) originalWidth / (float) originalHeight;
                    if (originalWidth > originalHeight) {
                        newWidth = CUSTOMER_IMAGE_SIZE;
                        newHeight = (int) (newWidth / aspectRatio);
                    } else {
                        newHeight = CUSTOMER_IMAGE_SIZE;
                        newWidth = (int) (newHeight * aspectRatio);
                    }
                    if (newWidth > CUSTOMER_IMAGE_SIZE) { // Double check
                        newWidth = CUSTOMER_IMAGE_SIZE;
                        newHeight = (int) (newWidth / aspectRatio);
                    }
                    if (newHeight > CUSTOMER_IMAGE_SIZE) { // Double check
                         newHeight = CUSTOMER_IMAGE_SIZE;
                         newWidth = (int) (newHeight * aspectRatio);
                    }
                }
                
                Image scaledImage = img.getScaledInstance(newWidth > 0 ? newWidth : 1, newHeight > 0 ? newHeight : 1, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage)); 
            } else {
                imageLabel.setIcon(null); 
                imageLabel.setText("?"); 
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imageLabel.setVerticalAlignment(SwingConstants.CENTER);
                System.err.println("Customer image not found: " + imagePath);
            }

            StringBuilder orderText = new StringBuilder("<html><body style='padding:3px;'>"); 
            String potatoName = customer.getPotato() != null ? customer.getPotato().getNama() : "N/A";
            orderText.append("<b>").append(potatoName).append("</b><br>");

            if (customer.getTopping() != null) {
                orderText.append("+ ").append(customer.getTopping().getNama()).append("<br>");
            }
            if (customer.getSauce() != null) {
                orderText.append("+ ").append(customer.getSauce().getNama()).append("<br>");
            }
            orderText.append("<hr style='margin:1px 0;'>Patience: <b>").append(customer.getPatienceTime()).append("</b></html>");
            orderLabel.setText(orderText.toString());
            slotPanel.setVisible(true);
        } else if (imageLabel != null && orderLabel != null && slotPanel != null) {
            imageLabel.setIcon(null);
            imageLabel.setText("");
            orderLabel.setText("");
            slotPanel.setVisible(false); 
        }
    }

    private void updateActiveCustomerPatience() {
        boolean customerChanged = false;
        for (int i = 0; i < MAX_VISIBLE_CUSTOMERS; i++) {
            if (activeCustomersInSlots[i] != null) {
                activeCustomersInSlots[i].decreasePatience(1); 
                if (activeCustomersInSlots[i].isAngry()) {
                    System.out.println("Customer " + activeCustomersInSlots[i].getCustomerImageID() + " di slot " + i + " marah dan pergi!");
                    activeCustomersInSlots[i] = null; 
                    customerChanged = true; 
                }
                updateCustomerSlotVisual(i); 
            }
        }
        // if (customerChanged) { // Tidak perlu panggil fill dari sini jika updateTimer sudah memanggilnya
        //     fillCustomerSlotsFromQueue(); 
        // }
    }

    private void updateOvenStatusVisuals() {
        if (this.ovenLogic == null) return;
        for (int i = 0; i < this.ovenLogic.length; i++) {
            if (this.ovenLogic[i] != null && ovenLabels[i] != null) {
                this.ovenLogic[i].updateStatus(); 
                if (this.ovenLogic[i].isReady()) {
                    ovenLabels[i].setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                } else {
                    ovenLabels[i].setBorder(null);
                }
            }
        }
    }

    private void updatePiringVisuals() {
        for (int i = 0; i < arrKentang.length; i++) {
            if (piringLabels[i] != null && arrKentang[i] != null) {
                String path = arrKentang[i].getImagePath();
                URL url = getClass().getResource(path);
                if (url != null) {
                    piringLabels[i].setIcon(new ImageIcon(url));
                }
            }
        }
    }
    
    private Potato createPotatoInstance(String potatoOvenName) {
        if (potatoOvenName == null) return new EmptyPotato();
        String cleanName = potatoOvenName.toLowerCase().trim(); 
        switch (cleanName) { 
            case "regular": return new RegularPotato(); 
            case "curly": return new CurlyPotato();
            case "chips": return new ChipsPotato();
            // case "wedges": return new WedgesPotato(); 
            case "tornado": return new TornadoPotato();
            case "mashed": return new MashedPotato();
            default:
                System.err.println("Unknown potato type for instance creation from oven name: " + potatoOvenName + " (cleaned: " + cleanName + ")");
                return new EmptyPotato();
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (!timerStarted && this.activeLevel != null) {
            timerStarted = true;
            final int totalSeconds = this.activeLevel.getDuration();
            final int[] timeLeft = {totalSeconds};
            System.out.println("Game timer starting for level " + this.activeLevel.getLevelNumber() + " with duration: " + totalSeconds + "s");

            gameTimer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    timeLeft[0]--;
                    if (timeLeft[0] <= 0) {
                        ((Timer)e.getSource()).stop();
                        if(updateTimer != null) updateTimer.stop(); 
                        JOptionPane.showMessageDialog(GamePanel.this, "Waktu habis untuk Level " + activeLevel.getLevelNumber() + "!");
                        if (mainFrame != null) {
                            mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "slots");
                        }
                    }
                }
            });
            gameTimer.start();
        } else if (this.activeLevel == null) {
            System.err.println("addNotify called but activeLevel is null. Timer not started.");
        }
    }
}
