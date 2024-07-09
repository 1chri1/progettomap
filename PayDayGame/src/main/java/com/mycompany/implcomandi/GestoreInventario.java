package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

/**
 * Classe che gestisce il comando inventario nel gioco.
 */
public class GestoreInventario implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Aggiorna lo stato del gioco in base al comando di inventario.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando
     * @return messaggio di risposta al giocatore
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() == TipoComandi.INVENTARIO) {
            return descrizione.getInventario().isEmpty() 
            ? "Il tuo inventario è vuoto!" 
            : messaggioInventario(descrizione);
        } else {
            return "";
        }
    }

    /**
     * Costruisce il messaggio contenente l'elenco degli oggetti nell'inventario.
     *
     * @param descrizione la descrizione del gioco contenente l'inventario
     * @return una stringa contenente l'elenco degli oggetti nell'inventario
     */
    private String messaggioInventario(GestioneGioco descrizione) {
        StringBuilder msg = new StringBuilder("Nel tuo inventario ci sono:\n");
        for (Oggetto object : descrizione.getInventario()) {
            msg.append(object.getNome()).append(": ").append(object.getDescrizione()).append("\n");
        }
        return msg.toString();
    }
}