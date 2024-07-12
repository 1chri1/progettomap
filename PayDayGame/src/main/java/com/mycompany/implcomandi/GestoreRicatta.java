package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

/**
 * Classe che gestisce il comando di ricatto nel gioco.
 */
public class GestoreRicatta implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Aggiorna lo stato del gioco in base al comando di ricatto.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando
     * @return messaggio di risposta al giocatore
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() == TipoComandi.RICATTA) {
            // Verifica se la stanza corrente è il caveau
            if (!"caveau".equalsIgnoreCase(descrizione.getStanzaCorrente().getNome())) {
                return "\nNon c'è nessuno che puoi ricattare qui.\n";
            }
            
            // Estrae l'oggetto dal parserOutput
            String oggettoDaRicattare = parserOutput.getOggetto() != null ? parserOutput.getOggetto().getNome() : "";

            // Verifica se l'oggetto è il direttore
            if ("direttore".equalsIgnoreCase(oggettoDaRicattare)) {
                Oggetto documentiRicatto = descrizione.getInventario().stream()
                        .filter(o -> "documenti ricatto".equalsIgnoreCase(o.getNome()))
                        .findFirst()
                        .orElse(null);

                if (documentiRicatto != null) {
                    descrizione.setRicattoDirettore(true);
                    return "\nHai ricattato il direttore con i documenti! Ora il tuo bottino sarà più alto.\n";
                } else {
                    return "\nNon hai i documenti per ricattare il direttore.\n";
                }
            } else {
                return "\nPuoi ricattare solo il direttore.\n";
            }
        }
        return "";
    }
}
