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
public abstract class Topping implements MenuItem{
    protected String nama;
    protected int value;

    
    @Override
    public String getNama() {
        return nama;
    }

    @Override
    public int getHarga() {
        return value;
    }

    @Override
    public int getWaktuMasak() {
        return 0;
    }
}

