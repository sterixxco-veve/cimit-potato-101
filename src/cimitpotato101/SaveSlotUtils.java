/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

/**
 *
 * @author Aspire
 */

import java.io.*;

public class SaveSlotUtils {
    // Menyimpan dan memuat data SaveSlotData ke/dari file menggunakan serialization 
    private static final String FILE_PREFIX = "save_slot_";
    private static final String FILE_SUFFIX = ".dat";

    public static void saveSlotData(int slotNumber, SaveSlotData data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_PREFIX + slotNumber + FILE_SUFFIX))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SaveSlotData loadSlotData(int slotNumber) {
        File file = new File(FILE_PREFIX + slotNumber + FILE_SUFFIX);
        if (!file.exists()) {
            return new SaveSlotData("", 1, 0); // default data kosong
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (SaveSlotData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
            return new SaveSlotData("", 1, 0);
        }
    }
}