package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Stanza;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

/**
 * Classe che gestisce il comando di salire nel gioco.
 */
public class GestoreSali implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Aggiorna lo stato del gioco in base al comando di salire.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando
     * @return messaggio di risposta al giocatore
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        if (parserOutput.getComando().getTipo() == TipoComandi.SALI) {
            Stanza stanzaCorrente = descrizione.getStanzaCorrente();
            String nomeStanzaCorrente = stanzaCorrente.getNome();

            if ("Scale Piano di Sotto".equalsIgnoreCase(nomeStanzaCorrente)) {
                Stanza nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano() + 1, "Scale");
                if (nuovaStanza != null) {
                    descrizione.setStanzaCorrente(nuovaStanza);
                    msg.append("Sei salito nelle scale del piano di sopra.");
                } else {
                    msg.append("Le scale del piano di sopra non esistono.");
                }
            } else {
                msg.append("Non puoi salire da qui.");
            }
        }
        return msg.toString();
    }
}