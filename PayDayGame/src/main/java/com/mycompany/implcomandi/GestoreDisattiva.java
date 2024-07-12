package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

/**
 * Classe che gestisce il comando di disattivazione nel gioco.
 */
public class GestoreDisattiva implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Aggiorna lo stato del gioco in base al comando di disattivazione.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando e l'oggetto
     * @return messaggio di risposta al giocatore
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        if (parserOutput.getComando().getTipo() == TipoComandi.DISATTIVA) {
            Oggetto oggettoDaDisattivare = parserOutput.getOggetto();

            // Verifica se ci sono oggetti nella stanza corrente o nell'inventario
            if (descrizione.getStanzaCorrente().getOggetti().isEmpty() && descrizione.getInventario().isEmpty()) {
                return "\nNon ci sono oggetti in questa stanza o nel tuo inventario.\n";
            }

            if (oggettoDaDisattivare == null) {
                return "\nNon capisco cosa vuoi disattivare.\n";
            }

            boolean oggettoTrovato = descrizione.getStanzaCorrente().getOggetti().contains(oggettoDaDisattivare) ||
                                     descrizione.getInventario().contains(oggettoDaDisattivare);

            if (oggettoTrovato) {
                if (oggettoDaDisattivare.isDisattivabile()) {
                    if ((oggettoDaDisattivare.getNome().equalsIgnoreCase("quadro elettrico") && descrizione.isQuadroElettricoDisattivato()) ||
                        (oggettoDaDisattivare.getNome().equalsIgnoreCase("torcia") && !descrizione.isTorciaAccesa())) {
                        msg.append("\nL'oggetto è già disattivato.\n");
                    } else {
                        if (oggettoDaDisattivare.getNome().equalsIgnoreCase("quadro elettrico")) {
                            descrizione.setQuadroElettricoDisattivato(true);
                            msg.append("\nHai disattivato: ").append(oggettoDaDisattivare.getDescrizione());
                            msg.append("\nAttenzione: è scattato l'allarme.\nHai 5 minuti per completare la rapina.\n");
                            // Avvia il timer della guardia solo se non è già attivo
                            if (!descrizione.isTimerAttivo()) {
                                descrizione.startTimer(55); // Timer di 5 minuti
                            }
                        } else if (oggettoDaDisattivare.getNome().equalsIgnoreCase("torcia")) {
                            descrizione.setTorciaAccesa(false);
                            msg.append("\nHai disattivato: ").append(oggettoDaDisattivare.getDescrizione() + "\n");
                        }
                    }
                } else {
                    msg.append("\nNon puoi disattivare questo oggetto.\n");
                }
            } else {
                msg.append("\nNon trovi l'oggetto da disattivare.\n");
            }
        }

        return msg.toString();
    }
}
