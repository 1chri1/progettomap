package com.mycompany.adventure;

import com.mycompany.parser.ParserOutput;
import com.mycompany.thread.TimerGuardia;
import com.mycompany.type.Comandi;
import com.mycompany.type.Oggetto;
import com.mycompany.type.Stanza;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe astratta che rappresenta la gestione del gioco.
 * Fornisce i metodi e gli attributi comuni necessari per gestire lo stato del gioco,
 * le stanze, i comandi e l'inventario.
 */
public abstract class GestioneGioco implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Stanza> STANZE = new ArrayList<>();
    private final List<Comandi> COMANDI = new ArrayList<>();
    private final List<Oggetto> INVENTARIO = new ArrayList<>();
    private Stanza stanzaCorrente; // visibile solo nel package a cui appartiene
    transient Engine engine;


    /**
     * Restituisce la lista delle STANZE del gioco.
     *
     * @return lista delle STANZE
     */
    public List<Stanza> getStanze() {
        return STANZE;
    }

    
    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
    /**
     * Restituisce la lista dei COMANDI del gioco.
     *
     * @return lista dei COMANDI
     */
    public List<Comandi> getComandi() {
        return COMANDI;
    }

    /**
     * Restituisce la stanza corrente in cui si trova il giocatore.
     *
     * @return stanza corrente
     */
    public Stanza getStanzaCorrente() {
        return stanzaCorrente;
    }

    /**
     * Imposta la stanza corrente in cui si trova il giocatore.
     *
     * @param stanzaCorrente la nuova stanza corrente
     */
    public void setStanzaCorrente(Stanza stanzaCorrente) {
        this.stanzaCorrente = stanzaCorrente;
    }

    /**
     * Restituisce l'INVENTARIO del giocatore.
     *
     * @return INVENTARIO del giocatore
     */
    public List<Oggetto> getInventario() {
        return INVENTARIO;
    }

    /**
     * Metodo astratto per inizializzare il gioco.
     *
     * @throws Exception in caso di errore durante l'inizializzazione
     */
    public abstract void inizializzazione() throws Exception;

    /**
     * Metodo astratto per gestire il prossimo spostamento nel gioco.
     *
     * @param p   output del parser
     * @param out stream di output
     */
    public abstract void prossimoSpostamento(ParserOutput p, PrintStream out);

    /**
     * Restituisce il messaggio iniziale del gioco.
     *
     * @return messaggio iniziale
     */
    public abstract String messaggioIniziale();

    // Metodi di gestione dello stato del gioco

    public abstract boolean isQuadroElettricoDisattivato();

    public abstract void setQuadroElettricoDisattivato(boolean quadroElettricoDisattivato);

    public abstract boolean isGiocoTerminato();

    public abstract void setGiocoTerminato(boolean giocoTerminato);

    public abstract boolean isTorciaAccesa();

    public abstract void setTorciaAccesa(boolean torciaAccesa);

    public abstract boolean isRicattoDirettore();

    public abstract void setRicattoDirettore(boolean ricattoDirettore);

    public abstract boolean isTimerAttivo();

    public abstract void setTimerAttivo(boolean timerAttivo);

    public abstract TimerGuardia getTimerGuardia();

    public abstract void setTimerGuardia(TimerGuardia timerGuardia);

    public abstract Thread getTimerThread();

    public abstract void setTimerThread(Thread timerThread);

    public abstract boolean isUscitoDalGioco();

    public abstract void setUscitoDalGioco(boolean uscitoDalGioco);

    // Metodi di gestione del timer

    public abstract void startTimer(int minuti);

    // Metodi per la serializzazione

    public abstract void salvaPartita(String filePath) throws IOException;

    public abstract GestioneGioco caricaPartita(String filePath) throws IOException, ClassNotFoundException;

    public abstract List<String> elencoSalvataggi(String directory);

    public abstract void gestisciSalvataggi(String baseFileName, String directory) throws IOException;

    // Nuovo metodo per ottenere il PrintStream
    public abstract PrintStream getOutputStream();
    
    public abstract void setOutputStream(PrintStream outputStream);
}
