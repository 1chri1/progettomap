package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.thread.TimerGuardia;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

public class GestoreDisattiva implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        if (parserOutput.getComando().getTipo() == TipoComandi.DISATTIVA) {
            Oggetto oggettoDaDisattivare = parserOutput.getOggetto();

            if (descrizione.getStanzaCorrente().getOggetti().isEmpty() && descrizione.getInventario().isEmpty()) {
                return "Non ci sono oggetti in questa stanza o nel tuo inventario.";
            }

            if (oggettoDaDisattivare == null) {
                return "Non capisco cosa vuoi disattivare.";
            }

            boolean oggettoTrovato = descrizione.getStanzaCorrente().getOggetti().contains(oggettoDaDisattivare) ||
                                     descrizione.getInventario().contains(oggettoDaDisattivare);

            if (oggettoTrovato) {
                if (oggettoDaDisattivare.isDisattivabile()) {
                    if ((oggettoDaDisattivare.getNome().equalsIgnoreCase("quadro elettrico") && descrizione.isQuadroElettricoDisattivato()) ||
                        (oggettoDaDisattivare.getNome().equalsIgnoreCase("torcia") && !descrizione.isTorciaAccesa())) {
                        msg.append("L'oggetto è già disattivato.");
                    } else {
                        if (oggettoDaDisattivare.getNome().equalsIgnoreCase("quadro elettrico")) {
                            descrizione.setQuadroElettricoDisattivato(true);
                            msg.append("Hai disattivato: ").append(oggettoDaDisattivare.getDescrizione());
                            System.out.println("Attenzione: è scattato l'allarme.\nHai 5 minuti per completare la rapina.");
                            // Avvia il timer della guardia solo se non è già attivo
                            if (!descrizione.isTimerAttivo()) {
                                descrizione.startTimer(5); // Timer di 5 minuti
                            }
                        } else if (oggettoDaDisattivare.getNome().equalsIgnoreCase("torcia")) {
                            descrizione.setTorciaAccesa(false);
                            msg.append("Hai disattivato: ").append(oggettoDaDisattivare.getDescrizione());
                        }
                    }
                } else {
                    msg.append("Non puoi disattivare questo oggetto.");
                }
            } else {
                msg.append("Non trovi l'oggetto da disattivare.");
            }
        }

        return msg.toString();
    }
}
