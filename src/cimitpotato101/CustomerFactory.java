/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cimitpotato101;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Gracia Krisnanda
 */
public class CustomerFactory {
    public static List<Customer> generateCustomersForLevel(int level, List<MenuItem> unlockedItems) {
        List<CustomerOrderTemplate> templates = CustomerOrderManager.getTemplatesForLevel(level);
        int customerCount = CustomerOrderManager.getCustomerCountForLevel(level);

        List<Customer> customers = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < customerCount; i++) {
            CustomerOrderTemplate template = templates.get(random.nextInt(templates.size()));

            MenuItem potato = findPotato(template.getPotato(), unlockedItems);
            if (potato == null) {
                // fallback: skip customer atau assign default "Regular"
                potato = findPotato("Regular", unlockedItems);
                if (potato == null) continue; // skip kalau gak ada juga
            }

            Topping topping = findTopping(template.getTopping(), unlockedItems);
            Topping sauce = findTopping(template.getSauce(), unlockedItems);

            int patience = random.nextInt(10) + 20; // bisa sesuaikan sesuai game balance
            customers.add(new Customer(potato, topping, sauce, patience));
        }

        return customers;
    }

    private static MenuItem findPotato(String name, List<MenuItem> items) {
        if (name == null) return null;
        return items.stream()
            .filter(i -> i instanceof Potato && i.getNama().equals(name))
            .findFirst()
            .orElse(null);
    }

    private static Topping findTopping(String name, List<MenuItem> items) {
        if (name == null) return null;
        return (Topping) items.stream()
            .filter(i -> i instanceof Topping && i.getNama().equals(name))
            .findFirst()
            .orElse(null);
    }
}
