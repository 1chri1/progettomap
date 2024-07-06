package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;

/**
 * Interfaccia che definisce il metodo di aggiornamento per gestire i comandi nel gioco.
 */
public interface Modifica {
    
    /**
     * Aggiorna lo stato del gioco in base all'output del parser.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando
     * @return messaggio di risposta al giocatore
     */
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput);
}
