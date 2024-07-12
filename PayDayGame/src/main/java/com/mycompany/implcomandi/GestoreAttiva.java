package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

/**
 * Classe che gestisce il comando di attivazione nel gioco.
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

        if (parserOutput.getComando().getTipo() == TipoComandi.ATTIVA) {
            Oggetto oggettoDaAttivare = parserOutput.getOggetto();

            // Verifica se ci sono oggetti nella stanza corrente o nell'inventario
            if (descrizione.getStanzaCorrente().getOggetti().isEmpty() && descrizione.getInventario().isEmpty()) {
                return "\nNon ci sono oggetti in questa stanza o nel tuo inventario.\n";
            }

            if (oggettoDaAttivare == null) {
                return "\nNon capisco cosa vuoi attivare.\n";
            }

            boolean oggettoTrovato = descrizione.getStanzaCorrente().getOggetti().contains(oggettoDaAttivare) ||
                                     descrizione.getInventario().contains(oggettoDaAttivare);

            if (oggettoTrovato) {
                if (oggettoDaAttivare.isDisattivabile()) {
                    if ((oggettoDaAttivare.getNome().equalsIgnoreCase("quadro elettrico") && !descrizione.isQuadroElettricoDisattivato()) ||
                        (oggettoDaAttivare.getNome().equalsIgnoreCase("torcia") && descrizione.isTorciaAccesa())) {
                        msg.append("\nL'oggetto è già attivato.\n");
                    } else {
                        if (oggettoDaAttivare.getNome().equalsIgnoreCase("quadro elettrico")) {
                            descrizione.setQuadroElettricoDisattivato(false);
                            msg.append("\nHai attivato: ").append(oggettoDaAttivare.getDescrizione() + "\n");
                        } else if (oggettoDaAttivare.getNome().equalsIgnoreCase("torcia")) {
                            if (descrizione.getInventario().contains(oggettoDaAttivare)) {
                                descrizione.setTorciaAccesa(true);
                                msg.append("\nHai attivato: ").append(oggettoDaAttivare.getDescrizione() + "\n");
                            } else {
                                msg.append("\nDevi prima prendere la torcia per poterla attivare.\n");
                            }
                        }
                    }
                } else {
                    msg.append("\nNon puoi attivare questo oggetto.\n");
                }
            } else {
                msg.append("\nNon trovo l'oggetto da attivare.\n");
            }
        }

        return msg.toString();
    }
}
