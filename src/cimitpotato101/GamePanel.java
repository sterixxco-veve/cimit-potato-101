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
import java.util.HashMap;
import java.util.LinkedList; // Menggunakan LinkedList untuk antrian customer
import java.util.List;
import java.util.Map;
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
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


/**
 *
 * @author Aspire
 */

public class GamePanel extends JPanel implements KeyListener {
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
    private ArrayList<Topping> availableToppings = new ArrayList<>();

    private Timer gameTimer; 
    private Timer updateTimer; 
    private boolean timerStarted = false;
    private boolean gameEnded = false; 
    private boolean isPaused = false; // Flag untuk status pause

    private Player currentPlayer;
    private int currentLevelNumber;
    private Level activeLevel;
    private MainPanel mainFrame;
    private int gameSlotNumber; 
    private SaveSlotData slotData;

    // Variabel untuk Customer Display
    private Queue<Customer> customerQueue; 
    private Customer[] activeCustomersInSlots; 
    private JPanel[] customerSlotPanels; 
    private JLabel[] customerImageLabels; 
    private JLabel[] customerOrderLabels; 

    // Variabel untuk Info UI Atas
    private JLabel customersInfoLabel; 
    private JLabel goldInfoLabel;
    private int goldEarnedThisLevel = 0;
    private int totalCustomersForLevel;
    private int customersLeftInLevel; 
    private JLabel timerLabel;


    // Koordinat dan ukuran untuk elemen customer (BERJAJAR KE KANAN)
    private final int CUSTOMER_START_X = 70; 
    private final int CUSTOMER_START_Y = 140;  
    private final int CUSTOMER_SLOT_WIDTH = 270; 
    private final int CUSTOMER_SLOT_HEIGHT = 125; 
    private final int CUSTOMER_SLOT_SPACING_X = 15; 
    
    private final int CUSTOMER_IMAGE_X = 5; 
    private final int CUSTOMER_IMAGE_Y = 5; 
    private final int CUSTOMER_IMAGE_SIZE = 105; 
    
    private final int ORDER_BUBBLE_X_OFFSET = CUSTOMER_IMAGE_X + CUSTOMER_IMAGE_SIZE + 5; 
    private final int ORDER_BUBBLE_Y = 5; 
    private final int ORDER_BUBBLE_WIDTH = CUSTOMER_SLOT_WIDTH - ORDER_BUBBLE_X_OFFSET - 10; 
    private final int ORDER_BUBBLE_HEIGHT = CUSTOMER_SLOT_HEIGHT - 10; 

    private final int MAX_VISIBLE_CUSTOMERS = 3; 
    
    private final Map<String, Integer> toppingSaucePrices = new HashMap<>();
    


    public GamePanel(Player player, int levelNumber, MainPanel mainFrameRef, int slotNumber, SaveSlotData slotData) {
        this.currentPlayer = player;
        this.currentLevelNumber = levelNumber;
        this.mainFrame = mainFrameRef;
        this.gameSlotNumber = slotNumber; 
        this.slotData = slotData;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(970, 570));

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(970, 570));
        layeredPane.setLayout(null);

        add(layeredPane, BorderLayout.CENTER);
        
        timerLabel = new JLabel("Time: 00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.WHITE); // Warna teks putih
        timerLabel.setBackground(new Color(0, 0, 0, 150)); // Latar belakang hitam semi-transparan
        timerLabel.setOpaque(true); // Penting agar background terlihat
        timerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding
        timerLabel.setBounds(20, 20, 150, 30); // Atur posisi X, Y, Lebar, Tinggi
        layeredPane.add(timerLabel, Integer.valueOf(JLayeredPane.MODAL_LAYER));

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

        initializeLevelData(); // Panggil metode untuk inisialisasi data level

        setupTopInfoUI(); 
        setupOvenUI();
        setupPiringUI();
        setupToppingSauceSourcesUI(); 
        setupPauseButton();
        setupCustomerDisplaySlots(); 
        fillCustomerSlotsFromQueue(); 

        // Timer dipindahkan ke startGameTimers() yang dipanggil oleh addNotify atau restartLevel
        // updateTimer = new Timer(...);
        // updateTimer.start();
        initAvailableToppings();
        buildToppingSaucePriceLookup();

        layeredPane.revalidate();
        layeredPane.repaint();
    }
    
//    ini code buat x
    public void keyPressed(KeyEvent e) {
        // Hentikan jika game sudah berakhir untuk mencegah cheat diaktifkan berkali-kali
        if (gameEnded) {
            return;
        }

        char key = e.getKeyChar();

        // Cheat untuk mendapatkan 3 bintang (FULL)
        if (key == 'z' || key == 'Z') {
            System.out.println("Cheat Activated: Level selesai dengan 3 Bintang!");
            endGame(3); // Langsung akhiri ronde dengan hasil 3 bintang
        }
        // Cheat untuk mendapatkan 2 bintang
        else if (key == 'x' || key == 'X') {
            System.out.println("Cheat Activated: Level selesai dengan 2 Bintang!");
            endGame(2); // Langsung akhiri ronde dengan hasil 2 bintang
        }
        // Cheat untuk mendapatkan 1 bintang
        else if (key == 'c' || key == 'C') {
            System.out.println("Cheat Activated: Level selesai dengan 1 Bintang!");
            endGame(1); // Langsung akhiri ronde dengan hasil 1 bintang
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Biarkan kosong jika tidak ada aksi yang diperlukan
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Biarkan kosong jika tidak ada aksi yang diperlukan
    }
        

    private void buildToppingSaucePriceLookup() {
        this.toppingSaucePrices.clear();
        for (Topping t : this.availableToppings) {
            this.toppingSaucePrices.put(t.getNama().toLowerCase(), t.getHarga());
        }
    }

    private void initializeLevelData() {
        this.gameEnded = false;
        this.isPaused = false;
        this.timerStarted = false; // Akan di-set true oleh startGameTimers
        this.arrKentang = new Potato[]{new EmptyPotato(), new EmptyPotato(), new EmptyPotato(), new EmptyPotato()};
        this.goldEarnedThisLevel = 0;

        if (this.currentPlayer.getUpgradeLevels() == null) {
             this.currentPlayer.initializeUpgradeLevels();
        }
        List<MenuItem> unlockedItems = MenuUnlock.getUnlockedItems(this.currentLevelNumber, this.currentPlayer);
        List<Customer> generatedCustomers = CustomerFactory.generateCustomersForLevel(this.currentLevelNumber, unlockedItems);
        this.customerQueue = new LinkedList<>(generatedCustomers); 
        this.totalCustomersForLevel = generatedCustomers.size(); 
        this.customersLeftInLevel = this.totalCustomersForLevel; 
        System.out.println("Initialized level data. Generated " + generatedCustomers.size() + " customers for level " + this.currentLevelNumber + ". Queue size: " + customerQueue.size());

        int goal = CustomerOrderManager.getGoalForLevel(this.currentLevelNumber); 
        int duration = CustomerOrderManager.getDurationForLevel(this.currentLevelNumber);
        System.out.println("Level " + this.currentLevelNumber + " - Target Gold: " + goal + ", Duration: " + duration + "s");
        
        
        
        this.activeLevel = new Level(this.currentLevelNumber, duration, unlockedItems, new ArrayList<>(customerQueue));
        this.ovenLogic = this.activeLevel.getOvens(); // Oven di-reset di sini
        
        int[] thresholds = CustomerOrderManager.getStarCriteriaForLevel(this.currentLevelNumber);
        this.activeLevel.setStarGoldThresholds(thresholds);
        // Reset visual customer slots
        if (activeCustomersInSlots != null) {
            Arrays.fill(activeCustomersInSlots, null);
            if (customerSlotPanels != null) { // Pastikan sudah di-setup
                 for(int i=0; i < MAX_VISIBLE_CUSTOMERS; i++) {
                    updateCustomerSlotVisual(i); // Kosongkan tampilan slot
                }
            }
        }
        updateTopInfoUILabels();
        updatePiringVisuals();
    }


    private void setupTopInfoUI() {
        JPanel topInfoPanel = new JPanel(null); 
        int panelWidth = 220; 
        int panelHeight = 40; 
        topInfoPanel.setBounds((getWidth() > 0 ? (getWidth() - panelWidth) / 2 : (970 - panelWidth) / 2), 20, panelWidth, panelHeight); 
        topInfoPanel.setOpaque(false); 
        
        URL boardUrl = getClass().getResource("/assets/info_board.png"); 
        JLabel boardBgLabel = null; 
        if (boardUrl != null) {
            ImageIcon boardIcon = new ImageIcon(boardUrl);
            Image scaledBoardImage = boardIcon.getImage().getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
            boardBgLabel = new JLabel(new ImageIcon(scaledBoardImage));
            boardBgLabel.setBounds(0, 0, panelWidth, panelHeight); 
            topInfoPanel.add(boardBgLabel); 
        } else {
            System.err.println("Info board image not found: /assets/info_board.png. Panel akan transparan tanpa border.");
            topInfoPanel.setBackground(new Color(0,0,0,0)); 
        }

        JPanel contentPanel = new JPanel(null);
        contentPanel.setBounds(0, 0, panelWidth, panelHeight);
        contentPanel.setOpaque(false); 

        int iconSize = 22; 
        int textOffsetY = (panelHeight - iconSize) / 2; 
        int currentX = 10; 

        JLabel customerIconLabel = new JLabel();
        URL customerIconUrl = getClass().getResource("/assets/customer_icon.png"); 
        if(customerIconUrl != null) {
            ImageIcon custOrigIcon = new ImageIcon(customerIconUrl);
            Image scaledCustIcon = custOrigIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            customerIconLabel.setIcon(new ImageIcon(scaledCustIcon));
        } else {
            System.err.println("Customer icon not found for top info UI.");
        }
        customerIconLabel.setBounds(currentX, textOffsetY, iconSize, iconSize); 
        contentPanel.add(customerIconLabel);
        currentX += iconSize + 5; 

        customersInfoLabel = new JLabel("0", SwingConstants.LEFT); 
        customersInfoLabel.setFont(new Font("Arial", Font.BOLD, 18)); 
        customersInfoLabel.setForeground(Color.DARK_GRAY); 
        customersInfoLabel.setBounds(currentX, textOffsetY, 60, iconSize); 
        contentPanel.add(customersInfoLabel);
        currentX += 60 + 15; 

        JLabel goldIconLabel = new JLabel();
        URL goldIconUrl = getClass().getResource("/assets/gold_coin_icon.png"); 
        if(goldIconUrl != null) {
            ImageIcon goldOrigIcon = new ImageIcon(goldIconUrl);
            Image scaledGoldIcon = goldOrigIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            goldIconLabel.setIcon(new ImageIcon(scaledGoldIcon));
        } else {
            System.err.println("Gold icon not found for top info UI.");
        }
        goldIconLabel.setBounds(currentX, textOffsetY, iconSize, iconSize); 
        contentPanel.add(goldIconLabel);
        currentX += iconSize + 5; 

        goldInfoLabel = new JLabel("0", SwingConstants.LEFT); 
        goldInfoLabel.setFont(new Font("Arial", Font.BOLD, 18)); 
        goldInfoLabel.setForeground(Color.DARK_GRAY); 
        goldInfoLabel.setBounds(currentX, textOffsetY, 80, iconSize); 
        contentPanel.add(goldInfoLabel);
        
        if (boardBgLabel != null) {
             topInfoPanel.add(contentPanel, 0); 
        } else {
            topInfoPanel.add(contentPanel); 
        }
        
        layeredPane.add(topInfoPanel, Integer.valueOf(JLayeredPane.PALETTE_LAYER + 10)); 
        updateTopInfoUILabels(); 
    }

    private void updateTopInfoUILabels() {
        if (customersInfoLabel != null) {
            customersInfoLabel.setText(String.valueOf(customersLeftInLevel)); 
        }
        if (goldInfoLabel != null && currentPlayer != null) {
            goldInfoLabel.setText(String.valueOf(this.goldEarnedThisLevel));
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
                ovenNameLabels[i].setBounds(x + i * 125, y - 18, 100, 20); // Sedikit diturunkan dari y - 20
                layeredPane.add(ovenNameLabels[i], Integer.valueOf(3)); 

                final int ovenIndex = i;
                ovenLabels[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(gameEnded || isPaused) return; // Tambahkan check isPaused
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
                        if(gameEnded || isPaused) return; // Tambahkan check isPaused
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
        if (gameEnded || isPaused || arrKentang[piringIndex] instanceof EmptyPotato) {
            return;
        }

        Potato servedPotato = arrKentang[piringIndex];
        int bestMatchSlot = -1;
        int lowestPatience = Integer.MAX_VALUE;

        for (int i = 0; i < MAX_VISIBLE_CUSTOMERS; i++) {
            if (activeCustomersInSlots[i] != null) {
                Customer targetCustomer = activeCustomersInSlots[i];
                if (checkOrderMatch(servedPotato, targetCustomer)) {
                    if (targetCustomer.getPatienceTime() < lowestPatience) {
                        lowestPatience = targetCustomer.getPatienceTime();
                        bestMatchSlot = i;
                    }
                }
            }
        }

        if (bestMatchSlot != -1) {
            Customer customerToServe = activeCustomersInSlots[bestMatchSlot];
            int goldFromServe = 0;
            goldFromServe += servedPotato.getHarga();
            
            if (servedPotato.getToppings() != null) {
                for (String toppingName : servedPotato.getToppings()) { // toppingName sudah lowercase
                    goldFromServe += this.toppingSaucePrices.getOrDefault(toppingName, 0);
                }
            }
            if (servedPotato.getSauces() != null) {
                for (String sauceName : servedPotato.getSauces()) { // sauceName sudah lowercase
                    goldFromServe += this.toppingSaucePrices.getOrDefault(sauceName, 0);
                }
            }
            this.goldEarnedThisLevel += goldFromServe;
            customersLeftInLevel--; 
            System.out.println("Player gold: " + currentPlayer.getGold() + ", Customers Left: " + customersLeftInLevel);
            
            activeCustomersInSlots[bestMatchSlot] = null; 
            updateCustomerSlotVisual(bestMatchSlot); 
            arrKentang[piringIndex] = new EmptyPotato(); 
            updatePiringVisuals();
            updateTopInfoUILabels(); 
            
            if (customersLeftInLevel <= 0 && customerQueue.isEmpty() && !anyActiveCustomers()) {
                endGame(true); 
            }
            return; 
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
            System.out.println("Jenis kentang salah");
            return false;
        }

//        List<String> itemsOnPlateNormalized = new ArrayList<>();
        String toppingKentang = servedPotato.getToppings().size()>0?servedPotato.getToppings().get(0):"";
        String sausKentang = servedPotato.getSauces().size()>0?servedPotato.getSauces().get(0):"";

//            for(String item : servedPotato.getToppings()){
//                itemsOnPlateNormalized.add(normalizeItemName(item));
//            }
//             for(String item : servedPotato.getSauces()){
//                itemsOnPlateNormalized.add(normalizeItemName(item));
//            }
        

//        String expectedToppingNameNormalized = (customer.getTopping() != null) ? normalizeItemName(customer.getTopping()) : null;
//        String expectedSauceNameNormalized = (customer.getSauce() != null) ? normalizeItemName(customer.getSauce()) : null;

//        int expectedAdditionalItemsCount = 0;
//        if (expectedToppingNameNormalized != null) expectedAdditionalItemsCount++;
//        if (expectedSauceNameNormalized != null) expectedAdditionalItemsCount++;
        
//        boolean toppingMatch = true;
//        if (expectedToppingNameNormalized != null) {
//            toppingMatch = itemsOnPlateNormalized.stream().anyMatch(item -> item.equalsIgnoreCase(expectedToppingNameNormalized));
//            if (!toppingMatch) {
//                System.out.println("Topping salah");
//                return false;
//            }
//        }
        if(!customer.getTopping().equalsIgnoreCase(toppingKentang)){
            System.out.println("Topping salah");
                return false;
        }
        if(!customer.getSauce().equalsIgnoreCase(sausKentang)){
            System.out.println("Saos salah");
                return false;
        }

//        boolean sauceMatch = true;
//        if (expectedSauceNameNormalized != null) {
//            sauceMatch = itemsOnPlateNormalized.stream().anyMatch(item -> item.equalsIgnoreCase(expectedSauceNameNormalized));
//            if (!sauceMatch){ 
//                System.out.println("Saos salah");
//                return false;
//            }
//        }
        
//        if (itemsOnPlateNormalized.size() != expectedAdditionalItemsCount) return false;
        
        return true; 
    }
    
    private void initAvailableToppings() {
        availableToppings.add(new Topping("bacon", 6, "topping", 1, "/assets/bacon.png"));
        availableToppings.add(new Topping("cheese", 4, "topping", 1, "/assets/cheese.png"));
        availableToppings.add(new Topping("pepperoin", 6, "topping", 1, "/assets/pepperoin.png"));
        availableToppings.add(new Topping("mayo", 2, "sauce", 1, "/assets/mayo.png"));
        availableToppings.add(new Topping("tomato", 2, "sauce", 1, "/assets/tomato.png"));
    }


    private void setupToppingSauceSourcesUI() {
    String[] itemAssetNames = {"bacon", "cheese", "pepperoin", "mayo", "tomato"}; 
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
                    if (gameEnded || isPaused) return;
                    for (int j = 0; j < arrKentang.length; j++) {
                        Potato rp = new Potato("", 0, "");
                        if (arrKentang[j] instanceof RegularPotato) {
                            rp = (RegularPotato) arrKentang[j];
                        }
                        else if (arrKentang[j] instanceof ChipsPotato) {
                            rp = (ChipsPotato) arrKentang[j];
                        }
                        else if (arrKentang[j] instanceof CurlyPotato) {
                            rp = (CurlyPotato) arrKentang[j];
                        }
                        else if (arrKentang[j] instanceof WedgesPotato) {
                            rp = (WedgesPotato) arrKentang[j];
                        } else if (arrKentang[j] instanceof TornadoPotato) {
                            rp = (TornadoPotato) arrKentang[j];
                        } else if (arrKentang[j] instanceof MashedPotato) {
                            rp = (MashedPotato) arrKentang[j];
                        }
                        if(!rp.getNama().equals("")){
                            boolean success=false;
                            for (Topping t : availableToppings) {
                                if (t.getNama().equalsIgnoreCase(itemNameForLogic)) {
                                    if(rp.addTopping(t)){
                                        System.out.println("Added " + itemNameForLogic + " to "+rp.getNama()+" on piring " + j + ". Current toppings: " + rp.getToppings());
                                        success=true;
//                                        break;
                                    }
                                }
                            }
                            if(success){
                               updatePiringVisuals(); 
                               break; 
                            }
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
            layeredPane.add(pauseButton, Integer.valueOf(JLayeredPane.MODAL_LAYER + 1)); 

            pauseButton.addActionListener(e -> {
                if(gameEnded) return;
                handlePauseAction();
            });
        } else {
            System.err.println("Pause button image not found: " + pauseImagePath);
        }
    }

    private void handlePauseAction() {
        isPaused = true;
        if (gameTimer != null) gameTimer.stop();
        if (updateTimer != null) updateTimer.stop();

        String[] options = {"Continue", "Restart Level", "Back to Menu"};
        int choice = JOptionPane.showOptionDialog(
            GamePanel.this,
            "Game Paused",
            "Pause Menu",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, 
            options,
            options[0] 
        );

        isPaused = false; // Reset pause flag setelah dialog ditutup

        switch (choice) {
            case 0: // Continue
                if (!gameEnded) { // Hanya resume jika game belum berakhir
                    if (gameTimer != null) gameTimer.start();
                    if (updateTimer != null) updateTimer.start();
                }
                break;
            case 1: // Restart Level
                restartLevel();
                break;
            case 2: // Back to Menu
                goBackToLevel();
                break;
            case JOptionPane.CLOSED_OPTION: // User menutup dialog (dianggap continue)
                 if (!gameEnded) {
                    if (gameTimer != null) gameTimer.start();
                    if (updateTimer != null) updateTimer.start();
                }
                break;
        }
    }

    private void restartLevel() {
        System.out.println("Restarting level " + currentLevelNumber);
        // Hentikan timer yang mungkin masih ada
        if (gameTimer != null) gameTimer.stop();
        if (updateTimer != null) updateTimer.stop();

        initializeLevelData(); // Re-inisialisasi semua data level dan UI dasar

        // Setup ulang UI spesifik game yang mungkin berubah
        setupOvenUI(); // Untuk memastikan nama oven benar jika ada perubahan
        setupPiringUI();
        // setupToppingSauceSourcesUI(); // Biasanya tidak perlu di-reset
        // setupPauseButton(); // Tidak perlu di-reset
        setupCustomerDisplaySlots(); // Setup ulang slot customer
        fillCustomerSlotsFromQueue(); // Isi customer awal
        
        if (this.activeLevel != null && timerLabel != null) {
            int totalSeconds = this.activeLevel.getDuration();
            int initialMinutes = totalSeconds / 60;
            int initialSeconds = totalSeconds % 60;
            timerLabel.setText(String.format("Time: %02d:%02d", initialMinutes, initialSeconds));
        }

        startGameTimers(); // Mulai timer lagi
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void goBackToSlots() {
        gameEnded = true;
        if (gameTimer != null) gameTimer.stop();
        if (updateTimer != null) updateTimer.stop();
        if (mainFrame != null) {
            mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "slots");
        }
    }
    
    private void goBackToLevel(){
        gameEnded = true;
        if (gameTimer != null) gameTimer.stop();
        if (updateTimer != null) updateTimer.stop();
        if (mainFrame != null) {
            mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "level");
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
            customerOrderLabels[i].setFont(new Font("Arial", Font.PLAIN, 13)); 
            customerOrderLabels[i].setVerticalAlignment(SwingConstants.TOP);
            customerOrderLabels[i].setForeground(Color.BLACK);
            customerSlotPanels[i].add(customerOrderLabels[i]);
            
            layeredPane.add(customerSlotPanels[i], Integer.valueOf(JLayeredPane.PALETTE_LAYER + 5)); 
            updateCustomerSlotVisual(i); 
        }
    }
    
    private void fillCustomerSlotsFromQueue() {
        if(gameEnded || isPaused) return; // Tambahkan check isPaused
        for (int i = 0; i < MAX_VISIBLE_CUSTOMERS; i++) {
            if (activeCustomersInSlots[i] == null) { 
                if (!customerQueue.isEmpty()) {
                    activeCustomersInSlots[i] = customerQueue.poll(); 
                    System.out.println("Customer " + activeCustomersInSlots[i].getCustomerImageID() + " masuk ke slot " + i);
                    updateCustomerSlotVisual(i);
                    return; 
                } else {
                    if (activeCustomersInSlots[i] == null) { 
                        updateCustomerSlotVisual(i); 
                    }
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
                    if (newWidth > CUSTOMER_IMAGE_SIZE) { 
                        newWidth = CUSTOMER_IMAGE_SIZE;
                        newHeight = (int) (newWidth / aspectRatio);
                    }
                    if (newHeight > CUSTOMER_IMAGE_SIZE) { 
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

            if (customer.getTopping() != "") {
                orderText.append("+ ").append(customer.getTopping()).append("<br>");
            }
            if (customer.getSauce() != "") {
                orderText.append("+ ").append(customer.getSauce()).append("<br>");
            }
            orderText.append("<hr style='margin:1px 0;'>Patience: <b>").append(customer.getPatienceTime()).append("</b>");
            orderText.append("<hr style='margin:1px 0;'>Emoji: <b>").append(customer.getEmotionEmoji()).append("</b></html>");
            orderLabel.setText(orderText.toString());
            slotPanel.setVisible(true);
        } else if (imageLabel != null && orderLabel != null && slotPanel != null) {
            imageLabel.setIcon(null);
            imageLabel.setText("");
            orderLabel.setText("");
            slotPanel.setVisible(false); 
        }
    }

    private void updateActiveCustomerPatienceAndHandleDeparture() { 
        if(gameEnded || isPaused) return; // Tambahkan check isPaused
        boolean customerChanged = false;
        for (int i = 0; i < MAX_VISIBLE_CUSTOMERS; i++) {
            if (activeCustomersInSlots[i] != null) {
                activeCustomersInSlots[i].decreasePatience(1); 
                if (activeCustomersInSlots[i].isAngry()) {
                    System.out.println("Customer " + activeCustomersInSlots[i].getCustomerImageID() + " di slot " + i + " marah dan pergi!");
                    int goldPenalty = -10;
                    this.goldEarnedThisLevel += goldPenalty;
                    if (this.goldEarnedThisLevel < 0) {
                        this.goldEarnedThisLevel = 0;
                    }
                    
                    if(currentPlayer.getGold() < 0) currentPlayer.setGold(0); 
                    
                    customersLeftInLevel--; 
                    
                    activeCustomersInSlots[i] = null; 
                    customerChanged = true; 
                    updateTopInfoUILabels(); 
                    if (customersLeftInLevel <= 0 && customerQueue.isEmpty() && !anyActiveCustomers()) {
                         endGame(false); 
                         return; 
                    }
                }
                updateCustomerSlotVisual(i); 
            }
        }
    }

    private boolean anyActiveCustomers() {
        for (Customer cust : activeCustomersInSlots) {
            if (cust != null) return true;
        }
        return false;
    }
    
    // PINTU MASUK 1: Untuk Cheat Code dari keyPressed(KeyEvent e)
    public void endGame(int starsFromCheat) {
        // Cheat selalu dianggap berhasil menyelesaikan level
        boolean levelSuccessfullyCompleted = true;

        // Panggil logika utama dengan bintang yang dipaksakan dari cheat
        executeEndGameLogic(starsFromCheat, levelSuccessfullyCompleted);
    }

    // PINTU MASUK 2: Untuk Alur Game Normal (panggil ini dari tempat Anda memanggil endGame sebelumnya)
    public void endGame(boolean allServedSuccessfully) {
        // --- HITUNG BINTANG BERDASARKAN GOLD (Logika asli Anda) ---
        int starsGained = 0;
        if (this.activeLevel != null && this.activeLevel.getStarGoldThresholds() != null) {
            int[] thresholds = this.activeLevel.getStarGoldThresholds();
            if (thresholds.length == 3) {
                if (this.goldEarnedThisLevel >= thresholds[2]) {
                    starsGained = 3;
                } else if (this.goldEarnedThisLevel >= thresholds[1]) {
                    starsGained = 2;
                } else if (this.goldEarnedThisLevel >= thresholds[0]) {
                    starsGained = 1;
                }
            }
        }

        // Tentukan apakah level benar-benar selesai dengan sukses
        boolean levelSuccessfullyCompleted = (allServedSuccessfully || (customersLeftInLevel <= 0 && customerQueue.isEmpty() && !anyActiveCustomers()));

        // Jika tidak selesai (misal waktu habis), bintang harus 0
        if (!levelSuccessfullyCompleted) {
            starsGained = 0;
        }

        // Panggil logika utama dengan bintang yang sudah dihitung
        executeEndGameLogic(starsGained, levelSuccessfullyCompleted);
    }

    private void executeEndGameLogic(int starsGained, boolean levelSuccessfullyCompleted) {
        String trophyAchieved = null;
        if (gameEnded) return;
        gameEnded = true;
        isPaused = false;

        if (gameTimer != null) gameTimer.stop();
        if (updateTimer != null) updateTimer.stop();

        String message;
        int nextLevelToSave = currentLevelNumber;

        // Bintang sudah dihitung/diberikan dari luar, jadi kita bisa langsung pakai 'starsGained'
        System.out.println("EndGame Check: starsGained diterima sebagai = " + starsGained);

        // Perhitungan total bintang sekarang harus hati-hati agar tidak menambah berulang kali jika level diulang
        // Kita akan urus ini saat menyimpan data

        int goldGainedForMessage = this.goldEarnedThisLevel;

        if (levelSuccessfullyCompleted) {
            if (currentLevelNumber < 10) {
                nextLevelToSave = currentLevelNumber + 1;
                message = "Level " + currentLevelNumber + " Selesai! Semua customer telah dilayani.";
            } else { // Level 10 selesai
                message = "Level " + currentLevelNumber + " Selesai! Kamu telah mencapai akhir permainan tahap ini.";
            }
        } else { // Gagal (misal: waktu habis)
            message = "Waktu Habis untuk Level " + currentLevelNumber + "!";
        }

        JOptionPane.showMessageDialog(GamePanel.this, message + "\nGold Diperoleh di Level Ini: " + goldGainedForMessage + "\nBintang Diraih: " + starsGained);

        // --- LOGIKA PENYIMPANAN DATA ---

        // 1. Dapatkan data lama untuk perbandingan
        int prevStarsForThisLevel = slotData.getStarsForLevel(currentLevelNumber);
        int totalStarsBeforeThisLevel = currentPlayer.getStars() - prevStarsForThisLevel;

        // 2. Tentukan bintang baru dan total bintang baru
        int newStarsForThisLevel = Math.max(starsGained, prevStarsForThisLevel);
        int newTotalStars = totalStarsBeforeThisLevel + newStarsForThisLevel;

        // 3. Update data di slotData
        slotData.setStarsForLevel(currentLevelNumber, newStarsForThisLevel);
        slotData.setStars(newTotalStars);

        // 4. Update progress lain
        int maxLevelReachedByPlayer = Math.max(slotData.getLevel(), levelSuccessfullyCompleted ? nextLevelToSave : currentLevelNumber);
        int goldToSave = currentPlayer.getGold() + goldGainedForMessage;
        slotData.setLevel(maxLevelReachedByPlayer);
        slotData.setGold(goldToSave);

        // 5. Update data di objek currentPlayer (untuk sesi ini)
        currentPlayer.setStars(newTotalStars);
        currentPlayer.setGold(goldToSave);
        currentPlayer.setCurrentLevel(maxLevelReachedByPlayer);


        if (currentLevelNumber == 10 && levelSuccessfullyCompleted) {
            if (newTotalStars >= 25) {
                trophyAchieved = "Gold";
            } else if (newTotalStars >= 15) {
                trophyAchieved = "Silver";
            } else { // Bintang di bawah 15
                trophyAchieved = "Bronze";
            }
        }
        slotData.setTrophyType(trophyAchieved);

        // 6. Save ke file
        SaveSlotUtils.saveSlotData(this.gameSlotNumber, slotData);

        // --- NAVIGASI KE LAYAR SELANJUTNYA ---
        if (currentLevelNumber == 10 && levelSuccessfullyCompleted) {
            if (mainFrame != null) {
                mainFrame.showTrophyScreen(currentPlayer, slotData, this.gameSlotNumber);
            } else {
                System.err.println("Error: MainFrame tidak terinisialisasi.");
                JOptionPane.showMessageDialog(GamePanel.this, "Selamat! Anda telah menyelesaikan semua level!", "Game Selesai", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }
        if (mainFrame != null) {
            mainFrame.showLevelSelectMenu(currentPlayer, slotData, this.gameSlotNumber);
        }
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
            case "wedges": return new WedgesPotato(); 
            case "tornado": return new TornadoPotato();
            case "mashed": return new MashedPotato();
            default:
                System.err.println("Unknown potato type for instance creation from oven name: " + potatoOvenName + " (cleaned: " + cleanName + ")");
                return new EmptyPotato();
        }
    }
    
    
    
    
    
    
    
    
    private void startGameTimers() {
        if (gameEnded || timerStarted) { 
            if(timerStarted && gameTimer != null && gameTimer.isRunning()){
                // Sudah berjalan, tidak perlu start ulang kecuali ini restart
            } else if (gameEnded) {
                return;
            }
        }

        if (gameTimer != null && gameTimer.isRunning()) gameTimer.stop();
        if (updateTimer != null && updateTimer.isRunning()) updateTimer.stop();

        timerStarted = true; 

        if (this.activeLevel != null) {
            final int totalSeconds = this.activeLevel.getDuration();
            final int[] timeLeft = {totalSeconds}; 

            gameTimer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (gameEnded || isPaused) { // Tambahkan check isPaused
                        // ((Timer) e.getSource()).stop(); // Jangan stop permanen jika hanya pause
                        return;
                    }
                    timeLeft[0]--;
                    if (timeLeft[0] <= 0) {
                        timerLabel.setText("Time: 00:00");
                        endGame(false); 
                    } else {
                        int minutes = timeLeft[0] / 60;
                        int seconds = timeLeft[0] % 60;
                        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
                    }
                }
            });
            gameTimer.start();
            int initialMinutes = totalSeconds / 60;
            int initialSeconds = totalSeconds % 60;
            timerLabel.setText(String.format("Time: %02d:%02d", initialMinutes, initialSeconds));
            System.out.println("Game timer (re)started for level " + this.activeLevel.getLevelNumber() + " with duration: " + totalSeconds + "s");
        } else {
            System.err.println("Cannot start game timer: activeLevel is null.");
        }

        updateTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameEnded || isPaused) { // Tambahkan check isPaused
                    // ((Timer) e.getSource()).stop(); // Jangan stop permanen jika hanya pause
                    return;
                }
                updateOvenStatusVisuals();
                updatePiringVisuals();
                updateActiveCustomerPatienceAndHandleDeparture();
                fillCustomerSlotsFromQueue();
                updateTopInfoUILabels();
            }
        });
        updateTimer.start();
    }


    @Override
    public void addNotify() {
        super.addNotify();
        
           // Supaya bisa menangkap input keyboard
            setFocusable(true);
            requestFocusInWindow();
            addKeyListener(this);
        if (!timerStarted && !gameEnded && this.activeLevel != null) { 
            startGameTimers();
        } else if (this.activeLevel == null) {
            System.err.println("addNotify called but activeLevel is null. Timers not started.");
        }
    }
}
