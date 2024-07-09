package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.Stanza;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

/**
 * Classe che gestisce il comando di entrata nel gioco.
 */
public class GestoreEntra implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CHIAVE_DIRETTORE = "chiavi direttore";
    private static final String TESSERINO_CAVEAU = "tesserino caveau";
    private static final String CHIAVE_UFFICI = "chiavi uffici";

    /**
     * Aggiorna lo stato del gioco in base al comando di entrata.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando e l'oggetto
     * @return messaggio di risposta al giocatore
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() != TipoComandi.ENTRA) {
            return "";
        }

        Stanza stanzaCorrente = descrizione.getStanzaCorrente();
        String nomeStanzaCorrente = stanzaCorrente.getNome();
        String nomeStanzaDestinazione = parserOutput.getDestinazione();
        Stanza nuovaStanza = null;
        StringBuilder msg = new StringBuilder();

        // Controllo se siamo in esterno lato destro o sinistro e il comando entra è senza destinazione
        if ((nomeStanzaCorrente.equalsIgnoreCase("Lato destro") || nomeStanzaCorrente.equalsIgnoreCase("Lato sinistro"))
                && (nomeStanzaDestinazione == null || nomeStanzaDestinazione.isEmpty())) {
            if (nomeStanzaCorrente.equalsIgnoreCase("Lato destro")) {
                nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Scale");
            } else if (nomeStanzaCorrente.equalsIgnoreCase("Lato sinistro")) {
                nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Bagno");
            }
            if (nuovaStanza != null) {
                descrizione.setStanzaCorrente(nuovaStanza);
                return "Sei entrato in: " + nuovaStanza.getNome();
            } else {
                return "Non riesci ad entrare in nessuna stanza da qui.";
            }
        }

        // Controllo se siamo nel corridoio 1 e il comando entra è senza destinazione
        if (nomeStanzaCorrente.equalsIgnoreCase("Corridoio 1") && (nomeStanzaDestinazione == null || nomeStanzaDestinazione.isEmpty())) {
            if (controlloInventario(descrizione, CHIAVE_UFFICI)) {
                nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Ufficio Vicino Corridoio 1");
                if (nuovaStanza != null) {
                    descrizione.setStanzaCorrente(nuovaStanza);
                    return "Sei entrato in: " + nuovaStanza.getNome();
                } else {
                    return "Non riesci ad entrare in nessuna stanza da qui.";
                }
            } else {
                msg.append("Non puoi entrare nell'ufficio vicino al corridoio 1. Ti manca la chiave degli uffici.");
                return msg.toString();
            }
        }

        if (nomeStanzaDestinazione == null || nomeStanzaDestinazione.isEmpty()) {
            msg.append("Non capisco dove vuoi andare.");
        } else {
            switch (nomeStanzaCorrente.toLowerCase()) {
                case "lato destro":
                    if (verificaDestinazione(stanzaCorrente.getPiano(), nomeStanzaDestinazione, "Scale")) {
                        nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Scale");
                    } else {
                        msg.append("Non capisco dove vuoi andare.");
                    }
                    break;
                case "lato sinistro":
                    if (verificaDestinazione(stanzaCorrente.getPiano(), nomeStanzaDestinazione, "Bagno")) {
                        nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Bagno");
                    } else {
                        msg.append("Non capisco dove vuoi andare.");
                    }
                    break;
                case "scale piano di sotto":
                    if (verificaDestinazione(stanzaCorrente.getPiano(), nomeStanzaDestinazione, "Ufficio Direttore")) {
                        if (controlloInventario(descrizione, CHIAVE_DIRETTORE)) {
                            nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Ufficio Direttore");
                        } else {
                            msg.append("Non puoi entrare nell'ufficio del direttore. Ti manca la chiave.");
                        }
                    } else if (verificaDestinazione(stanzaCorrente.getPiano(), nomeStanzaDestinazione, "Caveau")) {
                        if (controlloInventario(descrizione, TESSERINO_CAVEAU)) {
                            nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Caveau");
                        } else {
                            msg.append("Non puoi entrare nel caveau. Ti manca il tesserino.");
                        }
                    } else {
                        msg.append("Non capisco dove vuoi andare.");
                    }
                    break;
                case "corridoio 1":
                    if (verificaDestinazione(stanzaCorrente.getPiano(), nomeStanzaDestinazione, "Ufficio Vicino Corridoio 1")) {
                        if (controlloInventario(descrizione, CHIAVE_UFFICI)) {
                            nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Ufficio Vicino Corridoio 1");
                        } else {
                            msg.append("Non puoi entrare nell'ufficio vicino al corridoio 1. Ti manca la chiave degli uffici.");
                        }
                    } else {
                        msg.append("Non capisco dove vuoi andare.");
                    }
                    break;
                case "Scale":
                    if (verificaDestinazione(stanzaCorrente.getPiano(), nomeStanzaDestinazione, "Ufficio Vicino Scale")) {
                        if (controlloInventario(descrizione, CHIAVE_UFFICI)) {
                            nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Ufficio Vicino Scale");
                        } else {
                            msg.append("Non puoi entrare nell'ufficio vicino alle scale del piano di sopra. Ti manca la chiave degli uffici.");
                        }
                    } else {
                        msg.append("Non capisco dove vuoi andare.");
                    }
                    break;
                default:
                    msg.append("Non puoi entrare qui.");
                    return msg.toString();
            }
        }

        if (nuovaStanza != null) {
            descrizione.setStanzaCorrente(nuovaStanza);
        } else if (msg.length() == 0) {
            msg.append("Non capisco dove vuoi andare.");
        }

        return msg.toString();
    }

    /**
     * Verifica se la destinazione corrisponde alla stanza attuale.
     * 
     * @param piano il piano corrente
     * @param destinazione la destinazione
     * @param nomeStanza il nome della stanza
     * @return true se la destinazione corrisponde, false altrimenti
     */
    private boolean verificaDestinazione(int piano, String destinazione, String nomeStanza) {
        Stanza stanza = Stanza.trovaStanza(piano, nomeStanza);
        if (stanza != null && (stanza.getNome().equalsIgnoreCase(destinazione) || stanza.getAlias().contains(destinazione))) {
            return true;
        }
        return false;
    }

    /**
     * Verifica se l'inventario contiene l'oggetto richiesto.
     * 
     * @param descrizione la descrizione del gioco
     * @param itemName il nome dell'oggetto richiesto
     * @return true se l'oggetto è presente, false altrimenti
     */
    private boolean controlloInventario(GestioneGioco descrizione, String itemName) {
        for (Oggetto oggetto : descrizione.getInventario()) {
            if (oggetto.getNome().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }
}