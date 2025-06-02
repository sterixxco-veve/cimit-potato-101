/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cimitpotato101;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gracia Krisnanda
 */

public class CustomerOrderManager {
    private static final Map<Integer, Integer> customerCountByLevel = new HashMap<>();
    private static final Map<Integer, List<CustomerOrderTemplate>> orderTemplates = new HashMap<>();
    private static final Map<Integer, Integer> levelGoalsOld = new HashMap<>(); // Target gold lama, mungkin tidak terpakai
    private static final Map<Integer, Integer> levelDurations = new HashMap<>(); 
    
    // Struktur baru untuk menyimpan kriteria bintang: Level -> {skor_1_bintang, skor_2_bintang, skor_3_bintang}
    private static final Map<Integer, int[]> starCriteriaByLevel = new HashMap<>();

    static {
        // Inisialisasi customerCountByLevel
        customerCountByLevel.put(1, 8);
        customerCountByLevel.put(2, 10);
        customerCountByLevel.put(3, 12);
        customerCountByLevel.put(4, 14);
        customerCountByLevel.put(5, 16);
        customerCountByLevel.put(6, 18);
        customerCountByLevel.put(7, 20);
        customerCountByLevel.put(8, 22);
        customerCountByLevel.put(9, 24);
        customerCountByLevel.put(10, 26);

        // Inisialisasi orderTemplates
        List<CustomerOrderTemplate> level1Orders = new ArrayList<>();
        level1Orders.add(new CustomerOrderTemplate("Regular", null, "Tomato", false));
        orderTemplates.put(1, level1Orders);

        List<CustomerOrderTemplate> level2Orders = new ArrayList<>();
        level2Orders.add(new CustomerOrderTemplate("Curly", null, "Tomato", false));
        level2Orders.add(new CustomerOrderTemplate("Regular", null, "Mayo", false));
        orderTemplates.put(2, level2Orders);

        List<CustomerOrderTemplate> level3Orders = new ArrayList<>();
        level3Orders.add(new CustomerOrderTemplate("Chips", "Cheese", null, false));
        level3Orders.add(new CustomerOrderTemplate("Curly", null, "Mayo", false));
        level3Orders.add(new CustomerOrderTemplate("Regular", null, "Tomato", true));
        orderTemplates.put(3, level3Orders);

        List<CustomerOrderTemplate> level4Orders = new ArrayList<>();
        level4Orders.add(new CustomerOrderTemplate("Chips", null, "Mayo", false));
        level4Orders.add(new CustomerOrderTemplate("Curly", "Cheese", null, false));
        level4Orders.add(new CustomerOrderTemplate("Chips", null, "Tomato", false));
        orderTemplates.put(4, level4Orders);

        List<CustomerOrderTemplate> level5Orders = new ArrayList<>();
        level5Orders.add(new CustomerOrderTemplate("Wedges", null, "Mayo", false));
        level5Orders.add(new CustomerOrderTemplate("Chips", null, "Mayo", false));
        level5Orders.add(new CustomerOrderTemplate("Curly", null, "Tomato", false));
        orderTemplates.put(5, level5Orders);

        List<CustomerOrderTemplate> level6Orders = new ArrayList<>();
        level6Orders.add(new CustomerOrderTemplate("Wedges", "Cheese", null, false));
        level6Orders.add(new CustomerOrderTemplate("Chips", null, "Tomato", false));
        level6Orders.add(new CustomerOrderTemplate("Wedges", null, "Mayo", false));
        orderTemplates.put(6, level6Orders);

        List<CustomerOrderTemplate> level7Orders = new ArrayList<>();
        level7Orders.add(new CustomerOrderTemplate("Tornado", "Pepperoin", null, false));
        level7Orders.add(new CustomerOrderTemplate("Wedges", "Cheese", null, false));
        level7Orders.add(new CustomerOrderTemplate("Chips", "Pepperoin", null, true));
        orderTemplates.put(7, level7Orders);

        List<CustomerOrderTemplate> level8Orders = new ArrayList<>();
        level8Orders.add(new CustomerOrderTemplate("Tornado", "Cheese", "Mayo", false));
        level8Orders.add(new CustomerOrderTemplate("Wedges", "Pepperoin", null, false));
        level8Orders.add(new CustomerOrderTemplate("Tornado", null, null, true));
        orderTemplates.put(8, level8Orders);

        List<CustomerOrderTemplate> level9Orders = new ArrayList<>();
        level9Orders.add(new CustomerOrderTemplate("Mashed", "Bacon", "Cheese", false));
        level9Orders.add(new CustomerOrderTemplate("Tornado", "Pepperoin", null, false));
        level9Orders.add(new CustomerOrderTemplate("Wedges", "Bacon", null, true));
        orderTemplates.put(9, level9Orders);

        List<CustomerOrderTemplate> level10Orders = new ArrayList<>();
        level10Orders.add(new CustomerOrderTemplate("Mashed", "Cheese", null, false));
        level10Orders.add(new CustomerOrderTemplate("Tornado", "Bacon", null, false));
        level10Orders.add(new CustomerOrderTemplate("Wedges", "Pepperoin", "Mayo", true));
        orderTemplates.put(10, level10Orders);

        // Inisialisasi Durasi per Level (dalam detik)
        levelDurations.put(1, 180); 
        levelDurations.put(2, 170);
        levelDurations.put(3, 160);
        levelDurations.put(4, 150);
        levelDurations.put(5, 140);
        levelDurations.put(6, 130);
        levelDurations.put(7, 120); 
        levelDurations.put(8, 110);
        levelDurations.put(9, 100);
        levelDurations.put(10, 90); 

        // Inisialisasi Kriteria Bintang (Skor Gold yang dibutuhkan)
        // Format: {skor_untuk_1_bintang, skor_untuk_2_bintang, skor_untuk_3_bintang}
        starCriteriaByLevel.put(1, new int[]{30, 40, 50});     
        starCriteriaByLevel.put(2, new int[]{50, 60, 70});     
        starCriteriaByLevel.put(3, new int[]{70, 80, 90});     
        starCriteriaByLevel.put(4, new int[]{90, 100, 110});   
        starCriteriaByLevel.put(5, new int[]{110, 120, 130});  
        starCriteriaByLevel.put(6, new int[]{130, 140, 150});  
        starCriteriaByLevel.put(7, new int[]{150, 160, 170});  
        starCriteriaByLevel.put(8, new int[]{170, 180, 190}); 
        starCriteriaByLevel.put(9, new int[]{190, 200, 210});  
        starCriteriaByLevel.put(10, new int[]{210, 220, 230});
    }

    public static List<CustomerOrderTemplate> getTemplatesForLevel(int level) {
        return orderTemplates.getOrDefault(level, new ArrayList<>());
    }

    public static int getCustomerCountForLevel(int level) {
        return customerCountByLevel.getOrDefault(level, 8); 
    }

    // Goal di sini adalah skor minimal untuk 1 bintang (menyelesaikan level)
    public static int getGoalForLevel(int level) {
        int[] criteria = starCriteriaByLevel.get(level);
        if (criteria != null && criteria.length > 0) {
            return criteria[0]; // Target gold = skor untuk 1 bintang
        }
        // Fallback jika kriteria tidak ada untuk level tersebut (seharusnya tidak terjadi jika semua level diisi)
        return 50; 
    }

    public static int getDurationForLevel(int level) {
        return levelDurations.getOrDefault(level, 180); 
    }

    // Metode baru untuk mendapatkan kriteria bintang
    public static int[] getStarCriteriaForLevel(int level) {
        // Mengembalikan array kriteria, atau array default jika level tidak ditemukan
        return starCriteriaByLevel.getOrDefault(level, new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}); 
    }
    
    
}
