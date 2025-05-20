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

    static {
        // Inisialisasi customerCountByLevel (pengganti Map.of)
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
        level7Orders.add(new CustomerOrderTemplate("Tornado", "Pepperoni", null, false));
        level7Orders.add(new CustomerOrderTemplate("Wedges", "Cheese", null, false));
        level7Orders.add(new CustomerOrderTemplate("Chips", "Pepperoni", null, true));
        orderTemplates.put(7, level7Orders);

        List<CustomerOrderTemplate> level8Orders = new ArrayList<>();
        level8Orders.add(new CustomerOrderTemplate("Tornado", "Cheese", "Mayo", false));
        level8Orders.add(new CustomerOrderTemplate("Wedges", "Pepperoni", null, false));
        level8Orders.add(new CustomerOrderTemplate("Tornado", null, null, true));
        orderTemplates.put(8, level8Orders);

        List<CustomerOrderTemplate> level9Orders = new ArrayList<>();
        level9Orders.add(new CustomerOrderTemplate("Mashed", "Bacon", "Cheese", false));
        level9Orders.add(new CustomerOrderTemplate("Tornado", "Pepperoni", null, false));
        level9Orders.add(new CustomerOrderTemplate("Wedges", "Bacon", null, true));
        orderTemplates.put(9, level9Orders);

        List<CustomerOrderTemplate> level10Orders = new ArrayList<>();
        level10Orders.add(new CustomerOrderTemplate("Mashed", "Cheese", null, false));
        level10Orders.add(new CustomerOrderTemplate("Tornado", "Bacon", null, false));
        level10Orders.add(new CustomerOrderTemplate("Wedges", "Pepperoni", "Mayo", true));
        orderTemplates.put(10, level10Orders);
    }

    public static List<CustomerOrderTemplate> getTemplatesForLevel(int level) {
        return orderTemplates.getOrDefault(level, new ArrayList<>());
    }

    public static int getCustomerCountForLevel(int level) {
        return customerCountByLevel.getOrDefault(level, 8);
    }
}
