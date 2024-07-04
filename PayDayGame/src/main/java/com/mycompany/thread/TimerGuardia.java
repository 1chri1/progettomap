/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.thread;

import com.mycompany.adventure.GestioneGioco;

public class TimerGuardia implements Runnable {
    private int tempoRimasto; // in secondi
    private boolean running;
    private GestioneGioco gioco;

    public TimerGuardia(int tempoRimasto, GestioneGioco gioco) {
        this.tempoRimasto = tempoRimasto * 60; // Converti minuti in secondi
        this.gioco = gioco;
        this.running = true;
    }

    @Override
    public void run() {
        while (tempoRimasto > 0 && running) {
            try {
                Thread.sleep(60000); // Attende per un minuto (60,000 millisecondi)
                tempoRimasto -= 60; // Decrementa il tempo rimanente di un minuto
                System.out.println("La guardia sta cercando di risolvere il problema. Tempo rimanente: " + (tempoRimasto / 60) + " minuti.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        synchronized (gioco) {
            if (!gioco.isGiocoTerminato()) {
                if (tempoRimasto <= 0) {
                    System.out.println("La guardia ti ha trovato! Hai perso.");
                    gioco.setGiocoTerminato(true);
                } else {
                    System.out.println("Sei riuscito a scappare in tempo!");
                }
            }
        }
    }

    public void stop() {
        running = false;
    }
}
