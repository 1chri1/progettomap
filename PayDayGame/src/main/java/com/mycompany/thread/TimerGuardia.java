package com.mycompany.thread;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.swing.GameWindow;  // Aggiungi questa importazione
import java.io.Serializable;

/**
 * Classe che rappresenta il timer della guardia.
 */
public class TimerGuardia implements Runnable, Serializable {
    private static final long serialVersionUID = 1L;
    private int tempoRimasto; // in secondi
    private boolean running;
    private transient GestioneGioco gioco;

    /**
     * Costruttore della classe TimerGuardia.
     *
     * @param minuti il tempo iniziale in minuti
     * @param gioco il riferimento all'oggetto GestioneGioco
     */
    public TimerGuardia(int minuti, GestioneGioco gioco) {
        this.tempoRimasto = minuti * 60; // Converti minuti in secondi
        this.gioco = gioco;
        this.running = true;
    }

    /**
     * Metodo eseguito quando il thread viene avviato.
     */
    @Override
    public void run() {
        while (tempoRimasto > 0 && running) {
            try {
                Thread.sleep(1000); // Attende per un secondo (1.000 millisecondi)
                tempoRimasto -= 1; // Decrementa il tempo rimanente di un secondo
                if (tempoRimasto % 60 == 0) {
                    GameWindow.appendOutput("La guardia sta cercando di risolvere il problema. Tempo rimanente: " + (tempoRimasto / 60) + " minuti.\n");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            if (gioco.isGiocoTerminato()) {
                stop(); // Ferma il timer se il gioco è terminato
                return;
            }
        }

        synchronized (gioco) {
            if (!running) {
                return; // Esci immediatamente se il timer è stato fermato
            }

            if (!gioco.isGiocoTerminato()) {
                if (tempoRimasto <= 0) {
                    GameWindow.appendOutput("La guardia ti ha trovato! Hai perso.\n");
                    gioco.setGiocoTerminato(true);
                } else {
                    GameWindow.appendOutput("Sei riuscito a scappare in tempo!\n");
                }
            }
        }
    }

    /**
     * Metodo per fermare il timer.
     */
    public void stop() {
        running = false;
        Thread.currentThread().interrupt(); // Interrompe il thread per fermare immediatamente l'esecuzione
    }

    /**
     * Restituisce il tempo rimanente.
     *
     * @return il tempo rimanente in secondi
     */
    public int getTempoRimasto() {
        return tempoRimasto;
    }

    /**
     * Imposta il riferimento all'oggetto GestioneGioco.
     *
     * @param gioco il riferimento all'oggetto GestioneGioco da impostare
     */
    public void setGioco(GestioneGioco gioco) {
        this.gioco = gioco;
    }
}
