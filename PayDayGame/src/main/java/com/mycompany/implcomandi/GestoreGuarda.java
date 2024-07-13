package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.TipoComandi;
import com.mycompany.type.Oggetto;

import java.io.Serializable;
import java.util.List;

/**
 * Classe che gestisce il comando di guardare nel gioco.
 */
public class GestoreGuarda implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Aggiorna lo stato del gioco in base al comando di guardare.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando
     * @return messaggio di risposta al giocatore
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() == TipoComandi.GUARDA) {
            return messaggioGuarda(descrizione);
        } else {
            return "";
        }
    }

    /**
     * Costruisce il messaggio di risposta per il comando guarda.
     * 
     * @param descrizione la descrizione del gioco
     * @return messaggio di risposta al giocatore
     */
    private String messaggioGuarda(GestioneGioco descrizione) {
        StringBuilder msg = new StringBuilder();
        List<Oggetto> inventario = descrizione.getInventario();

        boolean haTorcia = inventario.stream().anyMatch(o -> "torcia".equalsIgnoreCase(o.getNome()));
        boolean haChiaviDirettore = inventario.stream().anyMatch(o -> "chiavi direttore".equalsIgnoreCase(o.getNome()));
        boolean haChiaviUffici = inventario.stream().anyMatch(o -> "chiavi uffici".equalsIgnoreCase(o.getNome()));
        boolean haRegistratore = inventario.stream().anyMatch(o -> "registratore".equalsIgnoreCase(o.getNome()));
        boolean haSoldi = inventario.stream().anyMatch(o -> "soldi".equalsIgnoreCase(o.getNome()));
        boolean haGioielli = inventario.stream().anyMatch(o -> "gioielli".equalsIgnoreCase(o.getNome()));

        String nomeStanzaCorrente = descrizione.getStanzaCorrente().getNome();

        // Verifica la stanza corrente e l'inventario per determinare il messaggio
        if (nomeStanzaCorrente.equalsIgnoreCase("Magazzino") && haTorcia) {
            msg.append("\nNon c'è niente di interessante qui.\n");
        } else if (nomeStanzaCorrente.equalsIgnoreCase("Ufficio Vicino Corridoio 1") && haRegistratore) {
            msg.append("\nNon c'è niente di interessante qui.\n");
        } else if (nomeStanzaCorrente.equalsIgnoreCase("Portineria")) {
            if (haChiaviDirettore && haChiaviUffici) {
                msg.append("\nNon c'è niente di interessante qui.\n");
            } else if (haChiaviDirettore) {
                msg.append("\nSulla scrivania ci sono ancora le chiavi degli uffici.\n");
            } else if (haChiaviUffici) {
                msg.append("\nSulla scrivania ci sono ancora le chiavi dell'ufficio del direttore.\n");
            } else {
                // Nessuna chiave, mostra il messaggio di guarda standard
                return getGuardaMessaggioStandard(descrizione);
            }
        } else if (nomeStanzaCorrente.equalsIgnoreCase("Caveau")) {
            if (haSoldi && haGioielli) {
                msg.append("\nNon c'è niente di interessante qui.\n");
            } else if (haSoldi) {
                msg.append("\nCi sono ancora i gioielli.\n");
            } else if (haGioielli) {
                msg.append("\nCi sono ancora i soldi.\n");
            } else {
                // Nessun soldi né gioielli, mostra il messaggio di guarda standard
                return getGuardaMessaggioStandard(descrizione);
            }
        } else {
            // Verifica lo stato del quadro elettrico e della torcia
            if (descrizione.isQuadroElettricoDisattivato() && !descrizione.isTorciaAccesa()) {
                msg.append("\nNon riesci a vedere nulla perché è tutto buio.\n");
            } else {
                return getGuardaMessaggioStandard(descrizione);
            }
        }

        return msg.toString();
    }

    /**
     * Restituisce il messaggio di guarda standard per la stanza corrente.
     * 
     * @param descrizione la descrizione del gioco
     * @return messaggio di guarda standard
     */
    private String getGuardaMessaggioStandard(GestioneGioco descrizione) {
        StringBuilder msg = new StringBuilder();
        if (descrizione.getStanzaCorrente().getGuarda() != null) {
            msg.append("\n").append(descrizione.getStanzaCorrente().getGuarda()).append("\n");
        } else {
            msg.append("\nNon c'è nulla di interessante qui.\n");
        }
        return msg.toString();
    }
}
