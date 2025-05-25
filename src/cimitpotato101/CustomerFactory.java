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

    public static List<Customer> generateCustomersForLevel(int level, List<MenuItem> unlockedItems) {
        List<CustomerOrderTemplate> templates = CustomerOrderManager.getTemplatesForLevel(level);
        int customerCount = CustomerOrderManager.getCustomerCountForLevel(level);

        List<Customer> customers = new ArrayList<>();
        Random random = new Random();

        if (templates.isEmpty()) {
            System.err.println("Tidak ada template pesanan untuk level " + level + ". Customer tidak dapat dibuat.");
            return customers; // Kembalikan list kosong jika tidak ada template
        }

        for (int i = 0; i < customerCount; i++) {
            CustomerOrderTemplate template = templates.get(random.nextInt(templates.size()));

            MenuItem potato = findPotato(template.getPotato(), unlockedItems);
            if (potato == null) {
                // fallback: skip customer atau assign default "Regular"
                System.out.println("Fallback: Potato '" + template.getPotato() + "' tidak ditemukan di unlockedItems, mencoba Regular Potato.");
                potato = findPotato("Regular", unlockedItems); // Cari "Regular Potato" bukan "Regular"
                if (potato == null) {
                    System.err.println("Fallback gagal: Regular Potato juga tidak ditemukan. Melewati customer ini.");
                    continue; // skip kalau gak ada juga
                }
            }

            Topping topping = findTopping(template.getTopping(), unlockedItems);
            Topping sauce = findTopping(template.getSauce(), unlockedItems);

            int patience = random.nextInt(10) + 20; // bisa sesuaikan sesuai game balance
            
            // Pilih ID gambar customer secara acak
            String customerImageID = CUSTOMER_IMAGE_IDS[random.nextInt(CUSTOMER_IMAGE_IDS.length)];
            
            customers.add(new Customer(potato, topping, sauce, patience, customerImageID));
        }

        return customers;
    }

    private static MenuItem findPotato(String name, List<MenuItem> items) {
        if (name == null) return null;
        // Pastikan pencocokan nama konsisten, misal "Regular Potato" vs "Regular"
        String searchName = name.endsWith(" Potato") ? name : name + " Potato";
        if (name.equalsIgnoreCase("Regular")) searchName = "Regular Potato"; // Khusus untuk Regular

        for (MenuItem item : items) {
            if (item instanceof Potato && item.getNama().equalsIgnoreCase(searchName)) {
                return item;
            }
        }
        // Fallback jika nama asli (tanpa " Potato") juga ada
        for (MenuItem item : items) {
            if (item instanceof Potato && item.getNama().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    private static Topping findTopping(String name, List<MenuItem> items) {
        if (name == null) return null;
        for (MenuItem item : items) {
            if (item instanceof Topping && item.getNama().equalsIgnoreCase(name)) {
                return (Topping) item;
            }
        }
        return null;
    }
}
