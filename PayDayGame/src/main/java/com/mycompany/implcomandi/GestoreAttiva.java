package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

/**
 * Classe che gestisce il comando di attivazione degli oggetti nel gioco.
 */
public class GestoreAttiva implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Aggiorna lo stato del gioco in base al comando di attivazione.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando e l'oggetto
     * @return messaggio di risposta al giocatore
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        // Verifica se il comando è di tipo "ATTIVA"
        if (parserOutput.getComando().getTipo() == TipoComandi.ATTIVA) {
            Oggetto oggettoDaAttivare = parserOutput.getOggetto();

            // Verifica se ci sono oggetti nella stanza corrente o nell'inventario
            if (descrizione.getStanzaCorrente().getOggetti().isEmpty() && descrizione.getInventario().isEmpty()) {
                return "Non ci sono oggetti in questa stanza o nel tuo inventario.";
            }
            
            // Verifica se l'oggetto specificato non è null
            if (oggettoDaAttivare == null) {
                return "Non capisco cosa vuoi attivare.";
            }

            // Controlla se l'oggetto è nella stanza corrente o nell'inventario
            boolean oggettoTrovato = descrizione.getStanzaCorrente().getOggetti().contains(oggettoDaAttivare) ||
                                     descrizione.getInventario().contains(oggettoDaAttivare);

            if (oggettoTrovato) {
                if (oggettoDaAttivare.isDisattivabile()) {
                    // Verifica se l'oggetto è già stato attivato
                    if ((oggettoDaAttivare.getNome().equalsIgnoreCase("quadro elettrico") && !descrizione.isQuadroElettricoDisattivato()) ||
                        (oggettoDaAttivare.getNome().equalsIgnoreCase("torcia") && descrizione.isTorciaAccesa())) {
                        msg.append("L'oggetto è già attivato.");
                    } else {
                        // Verifica il tipo di oggetto e aggiorna lo stato di conseguenza
                        if (oggettoDaAttivare.getNome().equalsIgnoreCase("quadro elettrico")) {
                            descrizione.setQuadroElettricoDisattivato(false);
                            msg.append("Hai attivato: ").append(oggettoDaAttivare.getDescrizione());
                        } else if (oggettoDaAttivare.getNome().equalsIgnoreCase("torcia")) {
                            // Verifica se la torcia è nell'inventario
                            if (descrizione.getInventario().contains(oggettoDaAttivare)) {
                                descrizione.setTorciaAccesa(true);
                                msg.append("Hai attivato: ").append(oggettoDaAttivare.getDescrizione());
                            } else {
                                msg.append("Devi prima prendere la torcia per poterla attivare.");
                            }
                        }
                    }
                } else {
                    msg.append("Non puoi attivare questo oggetto.");
                }
            } else {
                msg.append("Non trovi l'oggetto da attivare.");
            }
        }

        return msg.toString();
    }
}
