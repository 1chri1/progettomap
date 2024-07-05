package com.mycompany.thread;

import com.mycompany.adventure.GestioneGioco;
import java.io.Serializable;

public class TimerGuardia implements Runnable, Serializable {
    private static final long serialVersionUID = 1L;
    private int tempoRimasto; // in secondi
    private boolean running;
    private transient GestioneGioco gioco;

    public TimerGuardia(int minuti, GestioneGioco gioco) {
        this.tempoRimasto = minuti * 60; // Converti minuti in secondi
        this.gioco = gioco;
        this.running = true;
    }

    @Override
    public void run() {
        while (tempoRimasto > 0 && running) {
            try {
                Thread.sleep(1000); // Attende per un secondo (1,000 millisecondi)
                tempoRimasto -= 1; // Decrementa il tempo rimanente di un secondo
                if (tempoRimasto % 60 == 0) {
                    System.out.println("La guardia sta cercando di risolvere il problema. Tempo rimanente: " + (tempoRimasto / 60) + " minuti.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        synchronized (gioco) {
            if (!running) {
                return; // Esci immediatamente se il timer è stato fermato
            }

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
        Thread.currentThread().interrupt(); // Interrompe il thread per fermare immediatamente l'esecuzione
    }

    public int getTempoRimasto() {
        return tempoRimasto;
    }

    public void setGioco(GestioneGioco gioco) {
        this.gioco = gioco;
    }
}
