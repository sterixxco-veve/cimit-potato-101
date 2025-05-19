/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Aspire
 */
public class Player implements Serializable {
    protected String nama;
    protected int level;
    protected int star;
    protected static int gold;
    protected int stock;
    protected ArrayList<MenuItem> inventory = new ArrayList<>();
    
    public void upgrade(){
        if (star >= 100){
            System.out.println("Leveled Up!");
        }
    }
}
