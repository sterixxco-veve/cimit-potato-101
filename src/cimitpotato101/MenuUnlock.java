/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cimitpotato101;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Gracia Krisnanda
 */
public class MenuUnlock {
    private static final Map<Integer, List<String>> unlockedPotatoes = new HashMap<>();
    private static final Map<Integer, List<String>> unlockedToppings = new HashMap<>();

    static {
        // Kentang
        unlockedPotatoes.put(1, createList("Regular"));
        unlockedPotatoes.put(2, createList("Curly"));
        unlockedPotatoes.put(3, createList("Chips"));
        unlockedPotatoes.put(5, createList("Wedges"));
        unlockedPotatoes.put(7, createList("Tornado"));
        unlockedPotatoes.put(9, createList("Mashed"));

        // Topping & Sauce
        unlockedToppings.put(1, createList("Tomato"));
        unlockedToppings.put(2, createList("Mayo"));
        unlockedToppings.put(3, createList("Cheese"));
        unlockedToppings.put(7, createList("Pepperoni"));
        unlockedToppings.put(9, createList("Bacon"));
    }

    public static List<MenuItem> getUnlockedItems(int level, Player player) {
        List<MenuItem> unlocked = new ArrayList<>();

        Set<String> potatoNames = new LinkedHashSet<>();
        Set<String> toppingNames = new LinkedHashSet<>();

        for (int i = 1; i <= level; i++) {
            List<String> potatoes = unlockedPotatoes.get(i);
            if (potatoes != null) {
                potatoNames.addAll(potatoes);
            }

            List<String> toppings = unlockedToppings.get(i);
            if (toppings != null) {
                toppingNames.addAll(toppings);
            }
        }

        for (String name : potatoNames) {
            Potato p = new Potato(name, getPotatoPrice(name), getPotatoImage(name));
            p.setUpgradeLevel(player.getUpgradeLevel(name));
            unlocked.add(p);
        }

        for (String name : toppingNames) {
            String type = isSauce(name) ? "sauce" : "topping";
            Topping t = new Topping(name, getToppingPrice(name), type, level, getToppingImage(name));
            t.setUpgradeLevel(player.getUpgradeLevel(name));
            unlocked.add(t);
        }

        return unlocked;
    }

    // === Helper ===

    private static boolean isSauce(String name) {
        return "Tomato".equalsIgnoreCase(name) || "Mayo".equalsIgnoreCase(name);
    }

    private static int getPotatoPrice(String name) {
        if ("Regular".equals(name)) return 5;
        if ("Curly".equals(name)) return 6;
        if ("Chips".equals(name)) return 5;
        if ("Wedges".equals(name)) return 6;
        if ("Tornado".equals(name)) return 8;
        if ("Mashed".equals(name)) return 7;
        return 0;
    }

    private static String getPotatoImage(String name) {
        return "assets/potato_" + name.toLowerCase() + ".png";
    }

    private static int getToppingPrice(String name) {
        if ("Bacon".equals(name)) return 6;
        if ("Cheese".equals(name)) return 4;
        if ("Pepperoni".equals(name)) return 6;
        if ("Tomato".equals(name)) return 2;
        if ("Mayo".equals(name)) return 2;
        return 0;
    }

    private static String getToppingImage(String name) {
        return "assets/topping_" + name.toLowerCase() + ".png";
    }

    private static List<String> createList(String... items) {
        List<String> list = new ArrayList<>();
        for (String item : items) {
            list.add(item);
        }
        return list;
    }
}
