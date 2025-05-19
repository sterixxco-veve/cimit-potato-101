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
public abstract class Potato implements MenuItem {
    protected String nama;
    protected int harga;
    protected int waktumasak;

    @Override
    public String getNama() {
        return nama;
    }

    @Override
    public int getHarga() {
        return harga;
    }

    @Override
    public int getWaktuMasak() {
        return waktumasak;
    }

    
           
}
