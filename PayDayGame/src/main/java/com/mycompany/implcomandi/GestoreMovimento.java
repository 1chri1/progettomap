package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.swing.GameWindow;
import com.mycompany.type.Stanza;
import java.io.Serializable;

/**
 * Classe che gestisce i comandi di movimento nel gioco.
 */
public class GestoreMovimento implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;

    private static final String MSG_DIREZIONE_ERRATA = "\nNon puoi andare in quella direzione, c'è un muro\n";
    private static final String MSG_USA_COMANDO_ENTRA = "\nPer entrare in questa stanza usa il comando 'entra'.\n";
    private static final String MSG_ALLONTANAMENTO_BANCA = "\nTi stai allontanando dalla banca, non puoi andare in quella direzione.\n";

    /**
     * Aggiorna lo stato del gioco in base al comando di movimento.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando
     * @return messaggio di risposta al giocatore
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        Stanza stanzaCorrente = descrizione.getStanzaCorrente();
        Stanza nextRoom = null;
        String messaggio = "";

        switch (parserOutput.getComando().getTipo()) {
            case NORD:
                if (stanzaCorrente.getNome().equalsIgnoreCase("Lato Destro") ||
                    stanzaCorrente.getNome().equalsIgnoreCase("Lato Sinistro")) {
                    return "\nNon puoi andare in quella direzione, c'è una recinzione che non puoi scavalcare\n";
                }
                if (stanzaCorrente.getNome().equalsIgnoreCase("Esterno dell'ingresso principale")) {
                    return "\nNon puoi andare in quella direzione, ci sono due guardie che sorvegliano l'ingresso, potresti essere arrestato...\n";
                }
                if (stanzaCorrente.getNome().equalsIgnoreCase("Corridoio 1") ||
                    stanzaCorrente.getNome().equalsIgnoreCase("Corridoio 2")) {
                    if(!descrizione.isQuadroElettricoDisattivato() || descrizione.isTorciaAccesa()) {
                        GameWindow.appendOutput("\nSei andato avanti nel corridoio, ora vedi nuove porte\n");
                    }
                }
                if (stanzaCorrente.getNome().equalsIgnoreCase("Hall")) {
                    if(!descrizione.isQuadroElettricoDisattivato() || descrizione.isTorciaAccesa()) {
                        GameWindow.appendOutput("\nSei entrato nel corridoio, ora vedi nuove porte\n");
                    }
                }
                nextRoom = stanzaCorrente.getNord();
                messaggio = "\nA nord c'è ";
                break;
            case SUD:
                if (stanzaCorrente.getNome().equalsIgnoreCase("Esterno dell'Ingresso Principale")) {
                    descrizione.setGiocoTerminato(true,5);
                    return "\nSei andato in strada e sei stato investito. Il gioco è finito.";
                }
                if (stanzaCorrente.getNome().equalsIgnoreCase("Corridoio 3") ||
                    stanzaCorrente.getNome().equalsIgnoreCase("Corridoio 2")) {
                    if(!descrizione.isQuadroElettricoDisattivato() || descrizione.isTorciaAccesa()) {
                        GameWindow.appendOutput("\nSei andato avanti nel corridoio, ora vedi nuove porte\n");
                    }
                }
                nextRoom = stanzaCorrente.getSud();
                messaggio = "\nA sud c'è ";
                break;
            case EST:
                if (stanzaCorrente.getNome().equalsIgnoreCase("Bagno")) {
                    GameWindow.appendOutput("\nSei entrato nel corridoio, ora vedi nuove porte\n");
                }
                if (stanzaCorrente.getNome().equalsIgnoreCase("Lato sinistro")) {
                    messaggio = "\nA est c'è l'ingresso per il ";
                } else {
                    messaggio = "\nA est c'è ";
                }
                nextRoom = stanzaCorrente.getEst();
                break;
            case OVEST:
                if (stanzaCorrente.getNome().equalsIgnoreCase("Scale") || 
                    stanzaCorrente.getNome().equalsIgnoreCase("Magazzino") ||
                    stanzaCorrente.getNome().equalsIgnoreCase("Ufficio Vicino Corridoio 1")) {
                    if(!descrizione.isQuadroElettricoDisattivato() || descrizione.isTorciaAccesa()) {
                        GameWindow.appendOutput("\nSei entrato nel corridoio, ora vedi nuove porte\n");
                    }
                }
                if (stanzaCorrente.getNome().equalsIgnoreCase("Lato destro")) {
                    messaggio = "\nA ovest c'è l'ingresso per le ";
                } else {
                    messaggio = "\nA ovest c'è ";
                }
                nextRoom = stanzaCorrente.getOvest();
                break;
            default:
                return "";
        }

        if (nextRoom != null) {
            if (isStanzaSpeciale(stanzaCorrente, nextRoom)) {
                if(nextRoom.getNome().equalsIgnoreCase("Ufficio Vicino Corridoio 1") || nextRoom.getNome().equalsIgnoreCase("Ufficio Vicino Scale")) {
                   return messaggio + "un ufficio" + ". " + MSG_USA_COMANDO_ENTRA;   
 
                } else {
                  return messaggio + nextRoom.getNome() + ". " + MSG_USA_COMANDO_ENTRA;   
                }
            } else {
                descrizione.setStanzaCorrente(nextRoom);
                // Verifica se sei entrato nella Hall o Sala Controllo
                if ("Hall".equalsIgnoreCase(nextRoom.getNome()) && !descrizione.isQuadroElettricoDisattivato()) {
                     GameWindow.appendOutput("\nSei stato arrestato perche' le telecamere sono attive. Il gioco e' terminato.");
                    descrizione.setGiocoTerminato(true,5);
                }
                if ("Sala Controllo".equalsIgnoreCase(nextRoom.getNome())) {
                    descrizione.setGiocoTerminato(true,5);
                }
                return "";
            }
        } else {
            if (isEsterno(stanzaCorrente)) {
                return MSG_ALLONTANAMENTO_BANCA;
            } else {
                return MSG_DIREZIONE_ERRATA;
            }
        }
    }

    /**
     * Verifica se la stanza destinazione è una stanza speciale.
     * 
     * @param stanzaCorrente la stanza corrente
     * @param stanzaDestinazione la stanza destinazione
     * @return true se la stanza è speciale, false altrimenti
     */
    private boolean isStanzaSpeciale(Stanza stanzaCorrente, Stanza stanzaDestinazione) {
        String nomeStanzaCorrente = stanzaCorrente.getNome();
        String nomeStanzaDestinazione = stanzaDestinazione.getNome();

        return (nomeStanzaCorrente.equalsIgnoreCase("Lato Destro") && nomeStanzaDestinazione.equalsIgnoreCase("Scale")) ||
               (nomeStanzaCorrente.equalsIgnoreCase("Lato Sinistro") && nomeStanzaDestinazione.equalsIgnoreCase("Bagno")) ||
               (nomeStanzaCorrente.equalsIgnoreCase("Scale Piano di Sotto") && 
                ((nomeStanzaDestinazione.equalsIgnoreCase("Ufficio Direttore")) || nomeStanzaDestinazione.equalsIgnoreCase("Caveau"))) ||
               (nomeStanzaCorrente.equalsIgnoreCase("Corridoio 1") && nomeStanzaDestinazione.equalsIgnoreCase("Ufficio Vicino Corridoio 1")) ||
               (nomeStanzaCorrente.equalsIgnoreCase("Scale") && nomeStanzaDestinazione.equalsIgnoreCase("Ufficio Vicino Scale"));
    }

    /**
     * Verifica se la stanza è una stanza esterna.
     * 
     * @param stanza la stanza da verificare
     * @return true se la stanza è esterna, false altrimenti
     */
    private boolean isEsterno(Stanza stanza) {
        String nomeStanza = stanza.getNome();
        return nomeStanza.equalsIgnoreCase("Angolo destro della banca") ||
               nomeStanza.equalsIgnoreCase("Angolo sinistro della banca") ||
               nomeStanza.equalsIgnoreCase("Lato Destro") ||
               nomeStanza.equalsIgnoreCase("Lato Sinistro");
    }
}
