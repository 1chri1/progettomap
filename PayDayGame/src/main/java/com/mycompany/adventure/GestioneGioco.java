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

    /**
     * Restituisce l'engine del gioco.
     *
     * @return l'engine del gioco
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * Imposta l'engine del gioco.
     *
     * @param engine l'engine da impostare
     */
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
  
    // Metodi di gestione dello stato del gioco

    /**
     * Verifica se il quadro elettrico è disattivato.
     *
     * @return true se il quadro elettrico è disattivato, false altrimenti
     */
    public abstract boolean isQuadroElettricoDisattivato();

    /**
     * Imposta lo stato del quadro elettrico.
     *
     * @param quadroElettricoDisattivato lo stato del quadro elettrico
     */
    public abstract void setQuadroElettricoDisattivato(boolean quadroElettricoDisattivato);

    /**
     * Verifica se il gioco è terminato.
     *
     * @return true se il gioco è terminato, false altrimenti
     */
    public abstract boolean isGiocoTerminato();

    /**
     * Imposta lo stato di terminazione del gioco.
     *
     * @param giocoTerminato lo stato di terminazione del gioco
     * @param secondi        i secondi per la terminazione
     */
    public abstract void setGiocoTerminato(boolean giocoTerminato, int secondi);

    /**
     * Verifica se la torcia è accesa.
     *
     * @return true se la torcia è accesa, false altrimenti
     */
    public abstract boolean isTorciaAccesa();

    /**
     * Imposta lo stato della torcia.
     *
     * @param torciaAccesa lo stato della torcia
     */
    public abstract void setTorciaAccesa(boolean torciaAccesa);

    /**
     * Verifica se il direttore è ricattato.
     *
     * @return true se il direttore è ricattato, false altrimenti
     */
    public abstract boolean isRicattoDirettore();

    /**
     * Imposta lo stato di ricatto del direttore.
     *
     * @param ricattoDirettore lo stato di ricatto del direttore
     */
    public abstract void setRicattoDirettore(boolean ricattoDirettore);

    /**
     * Verifica se il timer è attivo.
     *
     * @return true se il timer è attivo, false altrimenti
     */
    public abstract boolean isTimerAttivo();

    /**
     * Imposta lo stato del timer.
     *
     * @param timerAttivo lo stato del timer
     */
    public abstract void setTimerAttivo(boolean timerAttivo);

    /**
     * Restituisce il TimerGuardia.
     *
     * @return il TimerGuardia
     */
    public abstract TimerGuardia getTimerGuardia();

    /**
     * Imposta il TimerGuardia.
     *
     * @param timerGuardia il TimerGuardia da impostare
     */
    public abstract void setTimerGuardia(TimerGuardia timerGuardia);

    /**
     * Restituisce il thread del timer.
     *
     * @return il thread del timer
     */
    public abstract Thread getTimerThread();

    /**
     * Imposta il thread del timer.
     *
     * @param timerThread il thread del timer da impostare
     */
    public abstract void setTimerThread(Thread timerThread);

    /**
     * Verifica se il giocatore è uscito dal gioco.
     *
     * @return true se il giocatore è uscito dal gioco, false altrimenti
     */
    public abstract boolean isUscitoDalGioco();

    /**
     * Imposta lo stato di uscita dal gioco.
     *
     * @param uscitoDalGioco lo stato di uscita dal gioco
     */
    public abstract void setUscitoDalGioco(boolean uscitoDalGioco);

    // Metodi di gestione del timer

    /**
     * Avvia il timer con il numero di minuti specificato.
     *
     * @param minuti il numero di minuti per il timer
     */
    public abstract void startTimer(int minuti);

    // Metodi per la serializzazione

    /**
     * Salva la partita corrente su un file.
     *
     * @param filePath il percorso del file su cui salvare la partita
     * @throws IOException in caso di errore durante il salvataggio
     */
    public abstract void salvaPartita(String filePath) throws IOException;

    /**
     * Carica una partita salvata da un file.
     *
     * @param filePath il percorso del file da cui caricare la partita
     * @return la partita caricata
     * @throws IOException            in caso di errore durante il caricamento
     * @throws ClassNotFoundException in caso di classe non trovata durante il caricamento
     */
    public abstract GestioneGioco caricaPartita(String filePath) throws IOException, ClassNotFoundException;

    /**
     * Restituisce l'elenco dei salvataggi disponibili in una directory.
     *
     * @param directory la directory in cui cercare i salvataggi
     * @return lista dei salvataggi disponibili
     */
    public abstract List<String> elencoSalvataggi(String directory);

    /**
     * Gestisce i salvataggi del gioco.
     *
     * @param baseFileName il nome base dei file di salvataggio
     * @param directory    la directory in cui salvare i file
     * @throws IOException in caso di errore durante la gestione dei salvataggi
     */
    public abstract void gestisciSalvataggi(String baseFileName, String directory) throws IOException;

    // Nuovo metodo per ottenere il PrintStream

    /**
     * Restituisce il PrintStream per l'output.
     *
     * @return il PrintStream per l'output
     */
    public abstract PrintStream getOutputStream();

    /**
     * Imposta il PrintStream per l'output.
     *
     * @param outputStream il PrintStream da impostare
     */
    public abstract void setOutputStream(PrintStream outputStream);
}
