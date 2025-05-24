/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cimitpotato101;

/**
 *
 * @author Gracia Krisnanda
 */
public class Oven {
    private String ovenName;            // Nama oven (misal: "Mashed Oven")
    private String potatoType;          // Jenis kentang yang sedang dimasak
    private long cookStartTime;         // Waktu mulai memasak (dalam ms)
    private long cookDuration;          // Durasi memasak (dalam ms)
    private boolean isOccupied;         // Apakah oven sedang digunakan
    private boolean isDone;             // Apakah masakannya sudah selesai

    public Oven(String ovenName, long cookDuration) {
        this.ovenName = ovenName;
        this.isOccupied = false;
        this.cookDuration = cookDuration;
        this.isDone = false;
    }

    // Mulai masak kentang
    public boolean startCooking(String potatoType) {
        if (isOccupied) return false; // Oven sedang dipakai

        this.potatoType = potatoType;
        this.cookStartTime = System.currentTimeMillis();
//        this.cookDuration = cookDurationMs;
        this.isOccupied = true;
        this.isDone = false;
        return true;
    }

    // Update status: apakah sudah matang?
    public void updateStatus() {
        if (!isOccupied || isDone) return;

        long now = System.currentTimeMillis();
        if (now - cookStartTime >= cookDuration) {
            isDone = true;
        }
    }

    // Ambil kentang matang dari oven
    public String takeOut() {
        if (!isDone) return null;

        String finishedProduct = potatoType; // bisa diubah ke nama aset, dll
        clearOven();
        return finishedProduct;
    }

    // Kosongkan oven setelah digunakan
    private void clearOven() {
        this.potatoType = null;
        this.isOccupied = false;
        this.isDone = false;
    }

    // Getter status
    public boolean isReady() {
        return isOccupied && isDone;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public String getOvenName() {
        return ovenName;
    }

    public String getPotatoType() {
        return potatoType;
    }

    public long getRemainingTimeMs() {
        if (!isOccupied || isDone) return 0;
        long now = System.currentTimeMillis();
        return Math.max(0, cookDuration - (now - cookStartTime));
    }

}
