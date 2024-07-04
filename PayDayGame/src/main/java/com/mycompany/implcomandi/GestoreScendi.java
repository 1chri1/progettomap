/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Stanza;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

public class GestoreScendi implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;
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
                        msg.append("Sei sceso nelle scale del piano di sotto.");
                    } else {
                        msg.append("Le scale del piano di sotto non esistono.");
                    }
                } else {
                        msg.append("Non puoi scendere da qui.");
                    }
         }
        return msg.toString();
    }
}
