package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

/**
 * Classe che gestisce il comando di ascolto nel gioco.
 */
public class GestoreAscolta implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Aggiorna lo stato del gioco in base al comando di ascolto.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando e l'oggetto
     * @return messaggio di risposta al giocatore
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        // Verifica se il comando è di tipo "ASCOLTA"
        if (parserOutput.getComando().getTipo() == TipoComandi.ASCOLTA) {
            Oggetto oggetto = parserOutput.getOggetto();
            Oggetto oggettoInventario = parserOutput.getOggettoInventario();
            
            // Verifica se ci sono oggetti nella stanza corrente o nell'inventario
        if (descrizione.getStanzaCorrente().getOggetti().isEmpty() && descrizione.getInventario().isEmpty()) {
            return "Non ci sono oggetti da ascoltare in questa stanza o nel tuo inventario.";
        }

            // Caso 1: Nessun oggetto specificato
            if (oggetto == null) {
                msg.append("Non capisco cosa vuoi ascoltare.");
            }
            // Caso 2: Oggetto specificato e l'oggetto è nell'inventario o nella stanza corrente
            else {
                Oggetto targetOggetto = (oggetto != null) ? oggetto : oggettoInventario;
                if (isOggettoAscoltabile(targetOggetto, "registratore")) {
                    if (targetOggetto.isAscoltabile()) {
                        msg.append("Stai ascoltando: ").append(targetOggetto.getDescrizione()).append("\n");
                        msg.append("'Direttore: Buongiorno, Marco. Hai preparato i documenti per la riunione di domani?'\n");
                        msg.append("'Segretario: Sì, Direttore. Sono pronti sulla sua scrivania.'\n");
                        msg.append("'Direttore: Perfetto. Dovrai anche accedere alla mia cassetta di sicurezza. Il codice è 9872. Assicurati che sia tutto in ordine.'\n");
                        msg.append("'Segretario: Certamente, Direttore. Mi occuperò di tutto subito.'\n");
                        msg.append("----------------------------------------------------------------------------------------------------------------------------------------\n");
                    } else {
                        msg.append("Non puoi ascoltare questo oggetto.");
                    }
                } else {
                    msg.append("Non trovi l'oggetto da ascoltare.");
                }
            }
        }

        return msg.toString();
    }

    /**
     * Verifica se un oggetto è ascoltabile, cioè se il nome dell'oggetto è "registratore" o uno dei suoi alias.
     * 
     * @param oggetto l'oggetto da verificare
     * @param nome il nome o alias da confrontare
     * @return true se l'oggetto è ascoltabile, false altrimenti
     */
    private boolean isOggettoAscoltabile(Oggetto oggetto, String nome) {
        if (oggetto == null) {
            return false;
        }
        if (oggetto.getNome().equalsIgnoreCase(nome)) {
            return true;
        }
        for (String alias : oggetto.getAlias()) {
            if (alias.equalsIgnoreCase(nome)) {
                return true;
            }
        }
        return false;
    }
}
