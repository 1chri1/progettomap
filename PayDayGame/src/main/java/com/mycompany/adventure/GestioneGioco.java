package com.mycompany.adventure;

import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Comandi;
import com.mycompany.type.Oggetto;
import com.mycompany.type.Stanza;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alessandro
 */
public abstract class GestioneGioco implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Stanza> stanze = new ArrayList<>();
    private final List<Comandi> comandi = new ArrayList<>();
    private final List<Oggetto> inventario = new ArrayList<>();
    private Stanza stanzaCorrente; //visibile solo nel package a cui appartiene

    /**
     *
     * @return
     */
    public List<Stanza> getStanze() {
        return stanze;
    }

    /**
     *
     * @return
     */
    public List<Comandi> getComandi() {
        return comandi;
    }

    /**
     *
     * @return
     */
    public Stanza getStanzaCorrente() {
        return stanzaCorrente;
    }

    /**
     *
     * @return
     */
    public List<Oggetto> getInventario() {
        return inventario;
    }

    /**
     *
     * @param stanzaCorrente
     */
    public void setStanzaCorrente(Stanza stanzaCorrente) {
        this.stanzaCorrente = stanzaCorrente;
    }
    
    /**
     *
     * @throws Exception
     */
    public abstract void inizializzazione() throws Exception;
    
    /**
     *
     * @param p
     * @param out
     */
    public abstract void ProssimoSpostamento(ParserOutput p, PrintStream out);
    
    /**
     *
     * @return
     */
    public abstract String MessaggioIniziale();

    // Metodi astratti da implementare in PayDayGame
    public abstract boolean isQuadroElettricoDisattivato();

    public abstract void setQuadroElettricoDisattivato(boolean quadroElettricoDisattivato);

    public abstract boolean isGiocoTerminato();

    public abstract void setGiocoTerminato(boolean giocoTerminato);
    
    public abstract boolean isTorciaAccesa();

    public abstract void setTorciaAccesa(boolean torciaAccesa);

    public abstract boolean isRicattoDirettore();

    public abstract void setRicattoDirettore(boolean ricattoDirettore);

    // Metodi per la serializzazione
    public abstract void salvaPartita(String filePath) throws IOException;

    public abstract GestioneGioco caricaPartita(String filePath) throws IOException, ClassNotFoundException;

    public abstract List<String> elencoSalvataggi(String directory);

    public abstract void gestisciSalvataggi(String baseFileName, String directory) throws IOException;
}
