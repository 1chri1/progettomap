package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

/**
 * Classe che gestisce il comando di prendere oggetti nel gioco.
 */
public class GestorePrendi implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Aggiorna lo stato del gioco in base al comando di prendere un oggetto.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando e l'oggetto
     * @return messaggio di risposta al giocatore
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        
        // verifica se il comando è PRENDI
        if (parserOutput.getComando().getTipo() == TipoComandi.PRENDI) {
            // Verifica se ci sono oggetti nella stanza corrente
            if (descrizione.getStanzaCorrente().getOggetti().isEmpty()) {
                return "Non ci sono oggetti in questa stanza.";
            }
            // verifica se l'oggetto è diverso da null
            if (parserOutput.getOggetto() != null) {
                // verifica se l'oggetto può essere raccolto
                if (parserOutput.getOggetto().isPrendibile()) {
                    // aggiunge l'oggetto all'inventario e lo rimuove dalla stanza corrente
                    descrizione.getInventario().add(parserOutput.getOggetto());
                    descrizione.getStanzaCorrente().getOggetti().remove(parserOutput.getOggetto());
                    msg.append("Hai appena raccolto: ").append(parserOutput.getOggetto().getDescrizione());
                    
                    if (haDocumentiRicatto(descrizione)) {
                        mostraDialogoDirettore();
                    }
                } else {
                    msg.append("Non puoi prendere questo oggetto");
                }           
            } else {
                msg.append("Non capisco cosa vuoi prendere");
            }
        }
        return msg.toString();
    }

    /**
     * Verifica se l'inventario contiene i documenti di ricatto.
     * 
     * @param descrizione la descrizione del gioco
     * @return true se l'inventario contiene i documenti di ricatto, false altrimenti
     */
    private boolean haDocumentiRicatto(GestioneGioco descrizione) {
        return descrizione.getInventario().stream().anyMatch(o -> "documenti ricatto".equalsIgnoreCase(o.getNome()));
    }

    /**
     * Mostra il dialogo del direttore quando vengono trovati i documenti di ricatto.
     * 
     * @param descrizione la descrizione del gioco
     */
    private void mostraDialogoDirettore() {
        // Interazioni con l'utente tramite la console
        System.out.println("\nDirettore: Cosa stai facendo con quei documenti?");
        System.out.println("Giocatore: Direttore, ho trovato questi documenti nel caveau. Possono rovinarti.");
        System.out.println("Direttore: Quanto vuoi per il tuo silenzio?");
        System.out.println("Giocatore: Voglio una somma di denaro extra.");
        System.out.println("Direttore: Hai vinto. Prendi i soldi, ma sappi che se scegli di non ricattarmi, potrei aiutarti in futuro.");
        System.out.println("\nOra puoi decidere se ricattare il direttore per ottenere subito i soldi extra \no ignorarlo e potenzialmente avere un alleato in futuro.\n");
    }
}
