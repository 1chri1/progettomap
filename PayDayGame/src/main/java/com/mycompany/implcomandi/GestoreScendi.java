package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Stanza;
import com.mycompany.type.TipoComandi;

import java.io.Serializable;

/**
 * Classe che gestisce il comando di scendere nel gioco.
 */
public class GestoreScendi implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Aggiorna lo stato del gioco in base al comando di scendere.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando
     * @return messaggio di risposta al giocatore
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        if (parserOutput.getComando().getTipo() == TipoComandi.SCENDI) {
            Stanza stanzaCorrente = descrizione.getStanzaCorrente();
            String nomeStanzaCorrente = stanzaCorrente.getNome();

            if ("Scale".equalsIgnoreCase(nomeStanzaCorrente)) {
                Stanza nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano() - 1, "Scale Piano di Sotto");
                if (nuovaStanza != null) {
                    descrizione.setStanzaCorrente(nuovaStanza);
                    msg.append("\nSei sceso nelle scale del piano di sotto.\n");
                } else {
                    msg.append("\nLe scale del piano di sotto non esistono.\n");
                }
            } else {
                msg.append("\nNon puoi scendere da qui.\n");
            }
        }
        return msg.toString();
    }
}
