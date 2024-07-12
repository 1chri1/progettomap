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
    private static boolean RUNNING;
    private transient final GestioneGioco GIOCO;

    /**
     * Costruttore della classe TimerGuardia.
     *
     * @param minuti il tempo iniziale in minuti
     * @param gioco il riferimento all'oggetto GestioneGioco
     */
    public TimerGuardia(int minuti, GestioneGioco gioco) {
        this.tempoRimasto = minuti * 60; // Converti minuti in secondi
        this.GIOCO = gioco;
        TimerGuardia.RUNNING = true;
    }

    /**
     * Metodo eseguito quando il thread viene avviato.
     */
    @Override
    public void run() {
        while (tempoRimasto > 0 && RUNNING) {
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
            if (GIOCO.isGiocoTerminato()) {
                stop(); // Ferma il timer se il GIOCO è terminato
                return;
            }
        }

        synchronized (GIOCO) {
            if (!RUNNING) {
                return; // Esci immediatamente se il timer è stato fermato
            }

            if (!GIOCO.isGiocoTerminato()) {
                if (tempoRimasto <= 0) {
                    GameWindow.appendOutput("La guardia ti ha trovato! Hai perso.\n");
                    GIOCO.setGiocoTerminato(true,7);
                } else {
                    GameWindow.appendOutput("Sei riuscito a scappare in tempo!\n");
                }
            }
        }
    }

    /**
     * Metodo per fermare il timer.
     */
    public static void stop() {
        RUNNING = false;
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
}
