package cimitpotato101;


import cimitpotato101.Player;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Gracia Krisnanda
 */
public class SaveObject implements Serializable {
    private ArrayList<Player> players;
    
    public SaveObject() {
        players = new ArrayList<>();
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayerByName(String name) {
        for (Player p : players) {
            if (p.getUsername().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public static void saveListPlayer (ArrayList <Player> listPlayer, String filename) {
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
