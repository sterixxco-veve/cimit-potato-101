/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author Aspire
 */
public class Storage {
    public static void simpanListPlayer(ArrayList<Player> listPlayer, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(listPlayer);
            System.out.println("Progress berhasil disimpan.");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan: " + e.getMessage());
        }
    }
	
	public static ArrayList<Player> loadListPlayer(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (ArrayList<Player>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Gagal load: " + e.getMessage());
            return null;
        }
    }
}
