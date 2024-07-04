package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Stanza;
import java.io.Serializable;

/**
 *
 * @author Alessandro
 */
public class GestoreMovimento implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final String MSG_DIREZIONE_ERRATA = "Non puoi andare in quella direzione, c'è un muro";
    private static final String MSG_USA_COMANDO_ENTRA = "Per entrare in questa stanza usa il comando 'entra'.";
    private static final String MSG_ALLONTANAMENTO_BANCA = "Ti stai allontanando dalla banca, non puoi andare in quella direzione.";

    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput){
        Stanza stanzaCorrente = descrizione.getStanzaCorrente();
        Stanza nextRoom = null;
        String messaggio = "";

        switch(parserOutput.getComando().getTipo()){
            case NORD:
                if ((stanzaCorrente.getNome().equalsIgnoreCase("Lato Destro")) ||
                    (stanzaCorrente.getNome().equalsIgnoreCase("Lato Sinistro"))) {
                    return "Non puoi andare in quella direzione, c'è una recinzione che non puoi scavalcare";
                }
                if(stanzaCorrente.getNome().equalsIgnoreCase("Esterno dell'ingresso principale")){
                 return "Non puoi andare in quella direzione, ci sono due guardie che sorvegliano l'ingresso, potresti essere arrestato...";
                }
                if ((stanzaCorrente.getNome().equalsIgnoreCase("Corridoio 1")) ||
                    (stanzaCorrente.getNome().equalsIgnoreCase("Corridoio 2"))) {
                    System.out.println("Sei andato avanti nel corridoio, ora vedi nuove porte");
                }
                nextRoom = stanzaCorrente.getNord();
                messaggio = "A nord c'è ";
                break;
            case SUD:
                if (stanzaCorrente.getNome().equalsIgnoreCase("Esterno dell'Ingresso Principale")) {
                    descrizione.setGiocoTerminato(true);
                    return "Sei andato in strada e sei stato investito. Il gioco è finito.";
                }
                if ((stanzaCorrente.getNome().equalsIgnoreCase("Corridoio 3")) ||
                    (stanzaCorrente.getNome().equalsIgnoreCase("Corridoio 2"))) {
                    System.out.println("Sei andato avanti nel corridoio, ora vedi nuove porte");
                }
                nextRoom = stanzaCorrente.getSud();
                messaggio = "A sud c'è ";
                break;
            case EST:
                if ((stanzaCorrente.getNome().equalsIgnoreCase("Bagno"))) {
                    System.out.println("Sei entrato nel corridoio, ora vedi nuove porte");
                }
                nextRoom = stanzaCorrente.getEst();
                messaggio = "A est c'è ";
                break;
            case OVEST:
                 if ((stanzaCorrente.getNome().equalsIgnoreCase("Scale Piano di Sopra")) || 
                    (stanzaCorrente.getNome().equalsIgnoreCase("Magazzino"))||
                    (stanzaCorrente.getNome().equalsIgnoreCase("Ufficio Vicino Corridoio 1"))) {
                    System.out.println("Sei entrato avanti nel corridoio, ora vedi nuove porte");
                }
                nextRoom = stanzaCorrente.getOvest();
                messaggio = "A ovest c'è ";
                break;
            default:
                return "";
        }

        if (nextRoom != null) {
            if (isStanzaSpeciale(stanzaCorrente, nextRoom)) {
                return messaggio + nextRoom.getNome() + ". " + MSG_USA_COMANDO_ENTRA;
            } else {
                descrizione.setStanzaCorrente(nextRoom);
                // Verifica se sei entrato nella Hall o Sala Controllo
                if ("Hall".equalsIgnoreCase(nextRoom.getNome()) && !descrizione.isQuadroElettricoDisattivato()) {
                    System.out.println("Sei stato arrestato perché le telecamere sono attive. Il gioco è terminato.");
                    descrizione.setGiocoTerminato(true);
                }
                if ("Sala Controllo".equalsIgnoreCase(nextRoom.getNome())) {
                    System.out.println("Sei stato catturato dalla guardia nella Sala Controllo. Il gioco è terminato.");
                    descrizione.setGiocoTerminato(true);
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

    private boolean isStanzaSpeciale(Stanza stanzaCorrente, Stanza stanzaDestinazione) {
        String nomeStanzaCorrente = stanzaCorrente.getNome();
        String nomeStanzaDestinazione = stanzaDestinazione.getNome();

        return (nomeStanzaCorrente.equalsIgnoreCase("Lato Destro") && nomeStanzaDestinazione.equalsIgnoreCase("Scale Piano di Sopra")) ||
               (nomeStanzaCorrente.equalsIgnoreCase("Lato Sinistro") && nomeStanzaDestinazione.equalsIgnoreCase("Bagno")) ||
               (nomeStanzaCorrente.equalsIgnoreCase("Scale Piano di Sotto") && 
                ((nomeStanzaDestinazione.equalsIgnoreCase("Ufficio Direttore")) || nomeStanzaDestinazione.equalsIgnoreCase("Caveau"))) ||
               (nomeStanzaCorrente.equalsIgnoreCase("Corridoio 1") && nomeStanzaDestinazione.equalsIgnoreCase("Ufficio Vicino Corridoio 1")) ||
               (nomeStanzaCorrente.equalsIgnoreCase("Scale Piano Di Sopra") && nomeStanzaDestinazione.equalsIgnoreCase("Ufficio Vicino Scale"));
    }
    
    private boolean isEsterno(Stanza stanza) {
        String nomeStanza = stanza.getNome();
        return nomeStanza.equalsIgnoreCase("Angolo destro della banca") ||
               nomeStanza.equalsIgnoreCase("Angolo sinistro della banca") ||
               nomeStanza.equalsIgnoreCase("Lato Destro") ||
               nomeStanza.equalsIgnoreCase("Lato Sinistro");
    }
}
