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
            Oggetto documentiRicatto = descrizione.getInventario().stream()
                    .filter(o -> "documenti ricatto".equalsIgnoreCase(o.getNome()))
                    .findFirst()
                    .orElse(null);

            if (documentiRicatto != null) {
                descrizione.setRicattoDirettore(true);
                return "Hai ricattato il direttore con i documenti! Ora il tuo bottino sarà più alto.";
            } else {
                return "Non hai i documenti per ricattare il direttore.";
            }
        }
        return "";
    }
}
