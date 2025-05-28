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
    private static final String[] CUSTOMER_IMAGE_IDS = {"customer1", "customer2", "customer3"}; // Daftar ID gambar customer
    private static final int DEFAULT_PATIENCE_TIME = 60; // Waktu kesabaran default (detik)

    public static List<Customer> generateCustomersForLevel(int level, List<MenuItem> unlockedItems) {
        List<CustomerOrderTemplate> templates = CustomerOrderManager.getTemplatesForLevel(level);
        int customerCount = CustomerOrderManager.getCustomerCountForLevel(level);

        List<Customer> customers = new ArrayList<>();
        Random random = new Random(); // Masih digunakan untuk memilih template dan ID gambar

        if (templates.isEmpty()) {
            System.err.println("Tidak ada template pesanan untuk level " + level + ". Customer tidak dapat dibuat.");
            return customers; 
        }

        for (int i = 0; i < customerCount; i++) {
            CustomerOrderTemplate template = templates.get(random.nextInt(templates.size()));

            MenuItem potato = findPotato(template.getPotato(), unlockedItems);
            if (potato == null) {
                System.out.println("Fallback: Potato '" + template.getPotato() + "' tidak ditemukan di unlockedItems, mencoba Regular Potato.");
                potato = findPotato("regular", unlockedItems); 
                if (potato == null) {
                    System.err.println("Fallback gagal: Regular Potato juga tidak ditemukan. Melewati customer ini.");
                    continue; 
                }
            }

            Topping topping = findTopping(template.getTopping(), unlockedItems);
            Topping sauce = findTopping(template.getSauce(), unlockedItems);

            // Gunakan waktu kesabaran default, tidak lagi random
            int patience = DEFAULT_PATIENCE_TIME; 
            
            String customerImageID = CUSTOMER_IMAGE_IDS[random.nextInt(CUSTOMER_IMAGE_IDS.length)];
            
            // Pastikan constructor Customer di Customer.java menerima customerImageID
            customers.add(new Customer(potato, topping, sauce, patience, customerImageID));
        }

        return customers;
    }

    // findPotato dan findTopping tetap sama, pastikan mereka bekerja dengan baik
    // untuk mencocokkan nama dari template dengan nama di MenuItem
    private static MenuItem findPotato(String name, List<MenuItem> items) {
        if (name == null) return null;
        // Normalisasi nama untuk pencocokan yang lebih baik
        String searchNameClean = name.toLowerCase().replace(" potato", "").trim();

        for (MenuItem item : items) {
            if (item instanceof Potato) {
                String itemNameClean = item.getNama().toLowerCase().replace(" potato", "").trim();
                if (itemNameClean.equalsIgnoreCase(searchNameClean)) {
                    return item;
                }
            }
        }
        // Jika tidak ketemu, coba cocokkan dengan nama asli (jika ada yang tidak pakai " Potato" di template)
        for (MenuItem item : items) {
            if (item instanceof Potato && item.getNama().equalsIgnoreCase(name)) {
                return item;
            }
        }
        System.err.println("Potato '" + name + "' (cleaned: " + searchNameClean + ") not found in unlocked items.");
        return null;
    }

    private static Topping findTopping(String name, List<MenuItem> items) {
        if (name == null) return null;
        for (MenuItem item : items) {
            if (item instanceof Topping && item.getNama().equalsIgnoreCase(name)) {
                return (Topping) item;
            }
        }
        // System.err.println("Topping/Sauce '" + name + "' not found in unlocked items."); // Bisa di-uncomment untuk debug
        return null;
    }
}
