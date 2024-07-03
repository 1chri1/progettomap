/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

/**
 *
 * @author Alessandro
 */
public class GestoreInventario implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     * @param descrizione
     * @param parserOutput
     * @return 
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() == TipoComandi.INVENTARIO) {
            return descrizione.getInventario().isEmpty() 
            ? "Il tuo inventario è vuoto!" 
            : messaggioInventario(descrizione);
        }
        else
        {
            return "";
        }
        
        
    }

    /**
     * Costruisce il messaggio contenente l'elenco degli oggetti nell'inventario.
     *
     * @param descrizione La descrizione del gioco contenente l'inventario.
     * @return Una stringa contenente l'elenco degli oggetti nell'inventario.
     */
    private String messaggioInventario(GestioneGioco descrizione) {
        StringBuilder msg = new StringBuilder("Nel tuo inventario ci sono:\n");
        for (Oggetto object : descrizione.getInventario()) {
            msg.append(object.getNome()).append(": ").append(object.getDescrizione()).append("\n");
        }
        return msg.toString();
    }
}
