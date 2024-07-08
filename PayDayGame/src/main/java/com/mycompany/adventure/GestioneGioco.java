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

    private final List<Stanza> stanze = new ArrayList<>();
    private final List<Comandi> comandi = new ArrayList<>();
    private final List<Oggetto> inventario = new ArrayList<>();
    private Stanza stanzaCorrente; // visibile solo nel package a cui appartiene

    /**
     * Restituisce la lista delle stanze del gioco.
     *
     * @return lista delle stanze
     */
    public List<Stanza> getStanze() {
        return stanze;
    }

    /**
     * Restituisce la lista dei comandi del gioco.
     *
     * @return lista dei comandi
     */
    public List<Comandi> getComandi() {
        return comandi;
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
     * Restituisce l'inventario del giocatore.
     *
     * @return inventario del giocatore
     */
    public List<Oggetto> getInventario() {
        return inventario;
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
    public abstract void ProssimoSpostamento(ParserOutput p, PrintStream out);

    /**
     * Restituisce il messaggio iniziale del gioco.
     *
     * @return messaggio iniziale
     */
    public abstract String MessaggioIniziale();

    // Metodi di gestione dello stato del gioco

    /**
     * Verifica se il quadro elettrico è disattivato.
     *
     * @return true se il quadro elettrico è disattivato, altrimenti false
     */
    public abstract boolean isQuadroElettricoDisattivato();

    /**
     * Imposta lo stato del quadro elettrico.
     *
     * @param quadroElettricoDisattivato true se il quadro elettrico è disattivato, altrimenti false
     */
    public abstract void setQuadroElettricoDisattivato(boolean quadroElettricoDisattivato);

    /**
     * Verifica se il gioco è terminato.
     *
     * @return true se il gioco è terminato, altrimenti false
     */
    public abstract boolean isGiocoTerminato();

    /**
     * Imposta lo stato del gioco come terminato.
     *
     * @param giocoTerminato true se il gioco è terminato, altrimenti false
     */
    public abstract void setGiocoTerminato(boolean giocoTerminato);

    /**
     * Verifica se la torcia è accesa.
     *
     * @return true se la torcia è accesa, altrimenti false
     */
    public abstract boolean isTorciaAccesa();

    /**
     * Imposta lo stato della torcia.
     *
     * @param torciaAccesa true se la torcia è accesa, altrimenti false
     */
    public abstract void setTorciaAccesa(boolean torciaAccesa);

    /**
     * Verifica se il direttore è ricattato.
     *
     * @return true se il direttore è ricattato, altrimenti false
     */
    public abstract boolean isRicattoDirettore();

    /**
     * Imposta lo stato di ricatto del direttore.
     *
     * @param ricattoDirettore true se il direttore è ricattato, altrimenti false
     */
    public abstract void setRicattoDirettore(boolean ricattoDirettore);

    /**
     * Verifica se il timer è attivo.
     *
     * @return true se il timer è attivo, altrimenti false
     */
    public abstract boolean isTimerAttivo();

    /**
     * Imposta lo stato del timer.
     *
     * @param timerAttivo true se il timer è attivo, altrimenti false
     */
    public abstract void setTimerAttivo(boolean timerAttivo);

    /**
     * Restituisce il timer della guardia.
     *
     * @return il timer della guardia
     */
    public abstract TimerGuardia getTimerGuardia();

    /**
     * Imposta il timer della guardia.
     *
     * @param timerGuardia il timer della guardia da impostare
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
     * @return true se il giocatore è uscito dal gioco, altrimenti false
     */
    public abstract boolean isUscitoDalGioco();

    /**
     * Imposta lo stato di uscita dal gioco.
     *
     * @param uscitoDalGioco true se il giocatore è uscito dal gioco, altrimenti false
     */
    public abstract void setUscitoDalGioco(boolean uscitoDalGioco);

    /**
     * Ferma il timer della guardia.
     */
    public abstract void fermaTimer();

    // Metodi di gestione del timer

    /**
     * Avvia il timer della guardia.
     *
     * @param minuti la durata del timer in minuti
     */
    public abstract void startTimer(int minuti);

    // Metodi per la serializzazione

    /**
     * Salva lo stato attuale del gioco in un file.
     *
     * @param filePath il percorso del file dove salvare il gioco
     * @throws IOException in caso di errore durante il salvataggio
     */
    public abstract void salvaPartita(String filePath) throws IOException;

    /**
     * Carica lo stato del gioco da un file.
     *
     * @param filePath il percorso del file da cui caricare il gioco
     * @return l'istanza di GestioneGioco caricata
     * @throws IOException in caso di errore durante il caricamento
     * @throws ClassNotFoundException se la classe salvata non viene trovata
     */
    public abstract GestioneGioco caricaPartita(String filePath) throws IOException, ClassNotFoundException;

    /**
     * Restituisce l'elenco dei salvataggi disponibili in una directory.
     *
     * @param directory la directory in cui cercare i file di salvataggio
     * @return lista dei nomi dei file di salvataggio
     */
    public abstract List<String> elencoSalvataggi(String directory);

    /**
     * Gestisce i salvataggi, mantenendo solo gli ultimi n salvataggi.
     *
     * @param baseFileName il nome base dei file di salvataggio
     * @param directory    la directory in cui sono salvati i file
     * @throws IOException in caso di errore durante la gestione dei file
     */
    public abstract void gestisciSalvataggi(String baseFileName, String directory) throws IOException;
}
