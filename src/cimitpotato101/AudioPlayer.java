/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimitpotato101;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Cella
 */
public class AudioPlayer {
    String filePath;
    public AudioPlayer(){
        this.filePath = "/assets/music.wav";
        playSound();
    }
    
    public synchronized void playSound() {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(AudioPlayer.class.getResourceAsStream(filePath));
                clip.open(inputStream);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                
            } catch (Exception e) {
                System.err.println("Error playing sound: " + e.getMessage());
            }
        }).start();
    }
}